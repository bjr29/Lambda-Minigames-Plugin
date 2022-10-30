package com.bjrushworth29.lambdaminigames.managers;

import com.bjrushworth29.lambdaminigames.commands.AddGameIdCommand;
import com.bjrushworth29.lambdaminigames.commands.EnableRemoveGameCommand;
import com.bjrushworth29.lambdaminigames.enums.*;
import com.bjrushworth29.lambdaminigames.games.util.Game;
import com.bjrushworth29.lambdaminigames.games.util.GameMap;
import com.bjrushworth29.lambdaminigames.games.util.GameQueue;
import com.bjrushworth29.lambdaminigames.games.util.TeamObject;
import com.bjrushworth29.lambdaminigames.utils.Countdown;
import com.bjrushworth29.lambdaminigames.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class GameManager {
	public static final int GAME_SPACES_SQUARED = 300;
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

		int squareId = getFirstEmptyGameSquareId() + AddGameIdCommand.id;
		Vector mapOffset = getGameSquare(squareId);

		Debug.info(DebugLevel.FULL, "Initialising game with map position ID '%s' and offset '%s'", squareId, mapOffset);

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
		int x = id % GAME_SPACES_SQUARED;
		int z = (int) Math.floor((float) id / GAME_SPACES_SQUARED);

		Vector offset = new Vector(GAME_SPACES_SQUARED / 2, 0, GAME_SPACES_SQUARED / 2);

		return new Vector(x, 0, z).subtract(offset).multiply(GAME_SQUARE_SIZE);
	}

	private static void startGame(Game game, ArrayList<Player> players) {
		if (game.getMinPlayers() > players.size()) {
			Debug.info(DebugLevel.FULL, "Cancelled game before starting");

			Countdown countdown = QUEUES.get(game).getCountdown();
			countdown.reset();

			return;
		}

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
			QUEUES.put(game, new GameQueue(game.getMaxPlayers() * 2, game.getMinPlayers()));
		}

		GameQueue gameQueue = QUEUES.get(game);
		gameQueue.getPlayers().add(player);

		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have entered the queue!");

		InventoryLoadoutManager.giveLoadout(player, InventoryLoadoutManager.getLoadout(DefaultInventoryLoadout.HUB_QUEUED));

		Countdown countdown = gameQueue.getCountdown();

		Debug.info(DebugLevel.FULL, "Game queue size: '%s', countdown: '%s'", gameQueue.getPlayers().size(), countdown.isRunning());

		if (gameQueue.getPlayers().size() >= game.getMaxPlayers()) {
			startGame(game, gameQueue.getPlayers());

		} else if (!countdown.isRunning() && gameQueue.getPlayers().size() >= game.getMinPlayers()) {
			Debug.info(DebugLevel.FULL, "Starting game queue");

			countdown.setCompleted(() -> startGame(game, gameQueue.getPlayers()));
			countdown.start();
		}
	}

	public static void leaveQueue(Player player) {
		GameQueue queue = getPlayerGameQueue(player);

		if (queue == null) {
			Debug.warn("Player '%s' attempted to leave queue whilst not in a queue!", player.getName());
			return;
		}

		InventoryLoadoutManager.giveLoadout(player, InventoryLoadoutManager.getLoadout(DefaultInventoryLoadout.HUB));
		player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You have left the queue!");

		ArrayList<Player> players = queue.getPlayers();

		players.remove(player);

		Game game = getQueueGame(queue);
		assert game != null;

		if (players.size() < game.getMinPlayers() && queue.getCountdown().isRunning()) {
			queue.getCountdown().reset();
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
		// Crashes with for each loop
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < ACTIVE_GAMES.size(); i++) {
			Game game = ACTIVE_GAMES.get(i);

			game.removePlayer(player);
		}

		leaveQueue(player);
	}

	private static void createGameMaps() {
		GAME_MAPS.add(new GameMap(
				"Sumo Platform",
				DefaultWorld.SUMO,
				0,
				0,
				Arrays.asList(GameType.SUMO, GameType.LAST_STANDING),
				Arrays.asList(
						new TeamObject<>(
							new Location(WorldManager.getWorld(DefaultWorld.GAMES), -5.5, 100, 0.5)
									.setDirection(new Vector(90 / Math.PI, 0, 0)),
								null,
								false,
								true
						),
						new TeamObject<>(
							new Location(WorldManager.getWorld(DefaultWorld.GAMES), 6.5, 100, 0.5)
									.setDirection(new Vector(-90 / Math.PI, 0, 0)),
								null,
								false,
								true
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
				null,
				2,
				2,
				false,
				false,
				1,
				0
		));

		GAMES.put("duels", new Game(
				GameType.LAST_STANDING,
				Constraints.PVP_DEFAULT,
				InventoryLoadoutManager.getLoadout(DefaultInventoryLoadout.DUELS),
				2,
				2,
				false,
				false,
				1,
				0
		));
	}

	public static void removeActiveGame(Game game, boolean force) {
		if (!EnableRemoveGameCommand.state && !force) {
			return;
		}

		ACTIVE_GAMES.remove(game);
		TAKEN_GAME_SQUARES.remove(game.getSquareId());

		game.getMap().destroy(game.getMapOffset());
	}

	public static void removeActiveGames() {
		for (Game game : ACTIVE_GAMES) {
			removeActiveGame(game, true);
		}
	}

	public static void applyDebugSettings() {
		for (String gameName : GAMES.keySet()) {
			Game game = GAMES.get(gameName);

			GAMES.put(gameName, new Game(
					game.getGameType(),
					game.getGameConstraints(),
					game.getInventoryLoadout(),
					1,
					game.getMaxPlayers(),
					game.canUseTeams(),
					game.getCanRejoin(),
					game.getStartingLives(),
					game.getSpawnProtectionDuration()
			));
		}

		Debug.info(DebugLevel.MIN, "Applied game debug settings");
	}
}
