package com.bjrushworth29.managers;

import com.bjrushworth29.enums.*;
import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.games.util.GameQueue;
import com.bjrushworth29.games.util.GameWorld;
import com.bjrushworth29.games.util.TeamObject;
import com.bjrushworth29.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {
	private static final HashMap<String, Game> GAMES = new HashMap<>();
	private static final ArrayList<GameWorld> GAME_WORLDS = new ArrayList<>();
	private static final ArrayList<Game> ACTIVE_GAMES = new ArrayList<>();
	private static final HashMap<Game, GameQueue> QUEUES = new HashMap<>();

	private static final Random RANDOM = new Random();

	static {
		createGames();
		createGameWorlds();

		Debug.info(DebugLevel.MIN, "Initialised games");
	}

	public static void createGameSession(Game game) {
		GameWorld[] selection = GAME_WORLDS.stream()
				.filter(world -> world.getGameTypes().contains(game.getGameType()))
				.toArray(GameWorld[]::new);
		GameWorld gameWorldSettings = selection[RANDOM.nextInt(selection.length)];

		game.init(gameWorldSettings);
	}

	private static void startGame(Game game, ArrayList<Player> players) {
		Game newGame = new Game(game);

		createGameSession(newGame);

		newGame.setJoiningPlayers(players.size());

		for (Player player : players) {
			newGame.addPlayer(player);
		}

		ACTIVE_GAMES.add(newGame);

		QUEUES.remove(game);
	}

	public static void enterQueue(Player player, String gameName) {
		if (getPlayerGame(player) != null || getPlayerGameQueue(player) != null) {
			player.sendMessage(ChatColor.RED + "Can't enter queue as you're already in a queue or game!");

			return;
		}

		Game game = GAMES.get(gameName);

		if (!QUEUES.containsKey(game)) {
			QUEUES.put(game, new GameQueue(game.getMaxPlayers() * 2));
		}

		GameQueue gameQueue = QUEUES.get(game);
		ArrayList<Player> players = gameQueue.getPlayers();

		players.add(player);

		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have entered the queue!");
		InventoryLoadoutManager.giveInventoryLoadout(player, InventoryLoadoutManager.getDefaultLoadout(DefaultInventoryLoadout.HUB_QUEUED));

		if (players.size() >= game.getMaxPlayers()) {
			startGame(game, players);

		} else if (!gameQueue.getCountdown().isRunning() && players.size() >= game.getMinPlayers()) {
			gameQueue.getCountdown().setCompleted(() -> startGame(game, players));
		}
	}

	public static void leaveQueue(Player player) {
		GameQueue queue = getPlayerGameQueue(player);

		if (queue == null) {
			Debug.warn("Player '%s' attempted to leave queue whilst not in a queue!", player.getName());

			return;
		}

		InventoryLoadoutManager.giveInventoryLoadout(player, InventoryLoadoutManager.getDefaultLoadout(DefaultInventoryLoadout.HUB));
		player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You have left the queue!");

		ArrayList<Player> players = queue.getPlayers();

		players.remove(player);

		Game game = getQueueGame(queue);
		assert game != null;

		if (players.size() < game.getMinPlayers() && queue.getCountdown().isRunning()) {
			queue.getCountdown().restart();
		}
	}

	private static Game getQueueGame(GameQueue queue) {
		for (Map.Entry<Game, GameQueue> entry : QUEUES.entrySet()) {
			if (queue == entry.getValue()) {
				return entry.getKey();
			}
		}

		return null;
	}

	public static Game getPlayerGame(Player player) {
		for (Game game : ACTIVE_GAMES) {
			if (game.containsPlayer(player)) {
				return game;
			}
		}

		return null;
	}

	public static GameQueue getPlayerGameQueue(Player player) {
		for (GameQueue queue : QUEUES.values()) {
			if (queue.getPlayers().contains(player)) {
				return queue;
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
				DefaultWorld.SUMO.toString(),
				0,
				0,
				Arrays.asList(GameType.SUMO),
				Arrays.asList(
						new TeamObject<>(
							new Location(WorldManager.getWorld(DefaultWorld.SUMO), -6, 100, 0), null, false, true
						),
						new TeamObject<>(
							new Location(WorldManager.getWorld(DefaultWorld.SUMO), 6, 100, 0), null, false, true
						)
				),
				null,
				null,
				90
		));
	}

	private static void createGames() {
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
