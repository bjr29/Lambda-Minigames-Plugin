package com.bjrushworth29.managers;

import com.bjrushworth29.enums.Constraints;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.games.util.GameQueue;
import com.bjrushworth29.games.util.GameWorld;
import com.bjrushworth29.games.util.TeamObject;
import com.bjrushworth29.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameManager {
	private static final HashMap<String, Game> GAMES = new HashMap<>();
	private static final ArrayList<GameWorld> GAME_WORLDS = new ArrayList<>();
	private static final ArrayList<Game> ACTIVE_GAMES = new ArrayList<>();
	private static final HashMap<Game, GameQueue> QUEUES = new HashMap<>();

	private static final Random RANDOM = new Random();

	static {
		createGames();
		createGameWorlds();

		Debug.info("Initialised games");
	}

	public static void createGameSession(Game game) {
		GameWorld[] selection = GAME_WORLDS.stream()
				.filter(world -> world.gameTypes().contains(game.getGameType()))
				.toList()
				.toArray(new GameWorld[0]);
		GameWorld gameWorldSettings = selection[RANDOM.nextInt(selection.length)];

		game.init(gameWorldSettings);
	}

	private static void startGame(Game game, ArrayList<Player> players) {
		Game newGame = new Game(game);

		createGameSession(newGame);

		for (Player player : players) {
			newGame.addPlayer(player);
		}

		ACTIVE_GAMES.add(newGame);

		QUEUES.remove(game);
	}

	public static void enterQueue(Player player, String gameName) {
		if (getPlayerGame(player) != null) {
			player.sendMessage(ChatColor.RED + "Can't enter queue as you're already in a queue or game");

			return;
		}

		Game game = GAMES.get(gameName);

		if (!QUEUES.containsKey(game)) {
			QUEUES.put(game, new GameQueue(game.getMaxPlayers() * 2, game.getGameType()));
		}

		GameQueue gameQueue = QUEUES.get(game);
		ArrayList<Player> players = gameQueue.getPlayers();

		players.add(player);

		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have entered the queue!");

		if (players.size() >= game.getMaxPlayers()) {
			startGame(game, players);

		} else if (!gameQueue.countdown.isRunning() && players.size() >= game.getMinPlayers()) {
			gameQueue.countdown.completed = () -> startGame(game, players);
		}
	}

	public static void leaveQueue(Player player) {
		Game game = getPlayerGame(player);

		if (game == null) {
			Debug.warn("Player '%s' attempted to leave queue whilst not in a queue!", player.getName());

			return;
		}

		InventoryLoadoutManager.giveInventoryLoadout(player, InventoryLoadoutManager.getDefaultLoadout("hub"));

		GameQueue queue = QUEUES.get(game);
		ArrayList<Player> players = queue.getPlayers();

		players.remove(player);

		if (players.size() < game.getMinPlayers()) {
			queue.countdown.restart();
		}
	}

	public static Game getPlayerGame(Player player) {
		for (Game game : ACTIVE_GAMES) {
			if (game.containsPlayer(player)) {
				return game;
			}
		}

		return null;
	}

	public static void removePlayer(Player player) {
		for (Game game : ACTIVE_GAMES) {
			game.removePlayer(player);
		}

		for (GameQueue queue : QUEUES.values()) {
			queue.getPlayers().remove(player);
		}
	}

	private static void createGameWorlds() {
		GAME_WORLDS.add(new GameWorld(
				"test_game",
				0,
				0,
				List.of(GameType.TEST),
				List.of(new TeamObject<>(
						new Location(WorldManager.getWorld("test_game"), 0, 64, 0), null, false, true)
				),
				null,
				null
		));
	}

	private static void createGames() {
		GAMES.put("testGame", new Game(
				GameType.TEST,
				Constraints.PVP_DEFAULT,
				1,
				1,
				false,
				false,
				1,
				1
		));

		GAMES.put("sumo", new Game(
				GameType.SUMO,
				Constraints.PVP_SUMO,
				2,
				2,
				false,
				false,
				1,
				0
		));
	}

	public static void removeActiveGame(Game game) {
		ACTIVE_GAMES.remove(game);
		game.delete();
	}

	public static void removeActiveGames() {
		for (Game game : ACTIVE_GAMES) {
			removeActiveGame(game);
		}
	}
}
