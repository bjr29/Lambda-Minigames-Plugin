package com.bjrushworth29.managers;

import com.bjrushworth29.enums.*;
import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.games.util.GameMap;
import com.bjrushworth29.games.util.GameQueue;
import com.bjrushworth29.games.util.TeamObject;
import com.bjrushworth29.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class GameManager {
	public static final int GAME_SPACES_SQUARED = 1000;
	public static final int GAME_SQUARE_SIZE = 2000;

	private static final HashMap<String, Game> GAMES = new HashMap<>();
	private static final ArrayList<GameMap> GAME_MAPS = new ArrayList<>();
	private static final ArrayList<Game> ACTIVE_GAMES = new ArrayList<>();
	private static final HashMap<Game, GameQueue> QUEUES = new HashMap<>();
	private static final ArrayList<Integer> TAKEN_GAME_SQUARES = new ArrayList<>();

	private static final Random RANDOM = new Random();

	static {
		createGames();
		createGameMaps();

		Debug.info(DebugLevel.MIN, "Initialised games");
	}

	public static void createGameSession(Game game) {
		GameMap[] selection = GAME_MAPS.stream()
				.filter(map -> map.getGameTypes().contains(game.getGameType()))
				.toArray(GameMap[]::new);
		GameMap gameMapSettings = selection[RANDOM.nextInt(selection.length)];

		int squareId = getFirstEmptyGameSquareId();
		Vector mapOffset = getGameSquare(squareId);

		game.init(gameMapSettings, mapOffset, squareId);

		TAKEN_GAME_SQUARES.add(squareId);
	}

	private static int getFirstEmptyGameSquareId() {
		int size = TAKEN_GAME_SQUARES.size();

		for (int i = 0; i < size; i++) {
			int gameSquare = TAKEN_GAME_SQUARES.get(i);

			if (gameSquare != i) {
				return i;
			}
		}

		return size;
	}

	private static Vector getGameSquare(int id) {
		return new Vector(GAME_SPACES_SQUARED / id, 0, GAME_SPACES_SQUARED % id).multiply(GAME_SQUARE_SIZE);
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

	private static void createGameMaps() {
		GAME_MAPS.add(new GameMap(
				"Sumo Platform",
				DefaultWorld.SUMO,
				0,
				0,
				Arrays.asList(GameType.SUMO),
				Arrays.asList(
						new TeamObject<>(
							new Location(WorldManager.getWorld(DefaultWorld.GAMES), -6, 100, 0), null, false, true
						),
						new TeamObject<>(
							new Location(WorldManager.getWorld(DefaultWorld.GAMES), 6, 100, 0), null, false, true
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
		TAKEN_GAME_SQUARES.remove(game.getGameSquareId());

		World world = WorldManager.getWorld(DefaultWorld.GAMES);

	}

	public static void removeActiveGames() {
		for (Game game : ACTIVE_GAMES) {
			removeActiveGame(game);
		}
	}
}
