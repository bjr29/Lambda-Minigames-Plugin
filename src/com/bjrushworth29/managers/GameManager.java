package com.bjrushworth29.managers;

import com.bjrushworth29.enums.Constraints;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.games.util.GameQueue;
import com.bjrushworth29.games.util.GameWorld;
import com.bjrushworth29.games.util.TeamObject;
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

		System.out.print("Initialised games");
	}

	public static void createGameSession(Game game, Player[] players) {
		GameWorld[] selection = (GameWorld[]) GAME_WORLDS.stream()
				.filter(world -> world.gameTypes().contains(game.getGameType()))
				.toArray();
		GameWorld gameWorldSettings = selection[RANDOM.nextInt(selection.length - 1)];

		game.init(players, gameWorldSettings);
	}

	public static void enterQueue(Player player, String gameName) {
		Game game = GAMES.get(gameName);

		if (!QUEUES.containsKey(game)) {
			QUEUES.put(game, new GameQueue(game.getMaxPlayers() * 2));
		}

		GameQueue gameQueue = QUEUES.get(game);
		ArrayList<Player> players = gameQueue.getPlayers();

		players.add(player);

		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have entered queue!");

		if (players.size() >= game.getMaxPlayers()) {
			startGame(game, players);

		} else if (!gameQueue.countdown.isRunning() && players.size() >= game.getMinPlayers()) {
			gameQueue.countdown.completed = () -> startGame(game, players);
		}
	}

	private static void startGame(Game game, ArrayList<Player> players) {
		createGameSession(game, players.toArray(new Player[0]));

		QUEUES.remove(game);
	}

	public static void leaveQueue(Player player, String gameName) {
		QUEUES.get(GAMES.get(gameName)).getPlayers().remove(player);
	}

	public static void removePlayerFromAnyGame(Player player) {
		for (Game game : ACTIVE_GAMES) {
			game.removePlayer(player);
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
		GAMES.put("Test Game", new Game(
				GameType.TEST,
				Constraints.PVP_DEFAULT,
				1,
				1,
				false,
				false
		));

		GAMES.put("Sumo", new Game(
				GameType.Sumo,
				Constraints.PVP_SUMO,
				2,
				2,
				false,
				false
		));
	}

	public static void removeActiveGame(Game game) {
		ACTIVE_GAMES.remove(game);
	}
}
