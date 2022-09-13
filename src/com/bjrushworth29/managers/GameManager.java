package com.bjrushworth29.managers;

import com.bjrushworth29.enums.Constraints;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.utils.Game;
import com.bjrushworth29.utils.GameWorld;
import com.bjrushworth29.utils.TeamObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameManager {
	private static final HashMap<String, Game> GAMES = new HashMap<>();
	private static final ArrayList<GameWorld> GAME_WORLDS = new ArrayList<>();
	private static final ArrayList<Game> ACTIVE_GAMES = new ArrayList<>();
	private static final HashMap<Game, ArrayList<Player>> QUEUES = new HashMap<>();

	private static final Random RANDOM = new Random();

	static {
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

		ArrayList<Player> players = QUEUES.get(game);

		players.add(player);

		if (players.size() >= game.getMaxPlayers()) {
			createGameSession(game, players.toArray(new Player[0]));

			QUEUES.remove(game);
		}
	}

	public static void leaveQueue(Player player, String gameName) {
		QUEUES.get(GAMES.get(gameName)).remove(player);
	}

	public static void removePlayerFromAnyGame(Player player) {
		for (Game game : ACTIVE_GAMES) {
			game.removePlayer(player);
		}
	}

	private static void createGameWorld() {
		GAME_WORLDS.add(new GameWorld(
				"test_game_world",
				0,
				0,
				GameType.TEST,
				new TeamObject<Location>[] {
						new Location(WorldManager.getWorld("test_game_world"), 0, 64, 0), // TODO: FIX
						null,
						false,
						true
				},
				null,
				null
		));
	}

	private static void createGame() {
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

	public static void destroyGame(Game game) {
		ACTIVE_GAMES.remove(game);
	}
}
