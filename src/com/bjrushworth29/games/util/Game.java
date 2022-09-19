package com.bjrushworth29.games.util;

import com.bjrushworth29.enums.Constraints;
import com.bjrushworth29.enums.GameState;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.managers.WorldManager;
import com.bjrushworth29.utils.Countdown;
import com.bjrushworth29.utils.Debug;
import com.bjrushworth29.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class Game {
	private final GameType gameType;
	private final Constraints gameConstraints;

	private final int maxPlayers;
	private final int minPlayers;

	private final boolean useTeams;
	private final boolean canRejoin;
	private World world;

	private GameWorld worldSettings;
	private GameState gameState;

	private final ArrayList<Player> players;

	private int joiningPlayers;
	private int cancelledPlayers;
	private ArrayList<Team> teams;

	public Game(GameType gameType, Constraints gameConstraints, int maxPlayers, int minPlayers, boolean useTeams, boolean canRejoin) {
		this.maxPlayers = maxPlayers;
		this.minPlayers = minPlayers;
		this.players = new ArrayList<>();
		this.useTeams = useTeams;
		this.gameType = gameType;
		this.gameConstraints = gameConstraints;
		this.canRejoin = canRejoin;
	}

	@SuppressWarnings("CopyConstructorMissesField")
	public Game(Game game) {
		this.maxPlayers = game.getMaxPlayers();
		this.minPlayers = game.getMinPlayers();
		this.players = new ArrayList<>(getMaxPlayers());
		this.useTeams = game.canUseTeams();
		this.gameType = game.getGameType();
		this.gameConstraints = game.getGameConstraints();
		this.canRejoin = game.getCanRejoin();
	}

	public void init(GameWorld worldSettings) {
		this.worldSettings = worldSettings;

		world = worldSettings.createSessionWorld();
		world.loadChunk(0, 0);

		gameState = GameState.WAITING;
	}

	public void addPlayer(Player player) {
		if (gameState != GameState.WAITING || (players.size() >= maxPlayers)) {
			Debug.warn(
					"Player '%s' attempted to join game (id: '%h') '%s' but was refused. Game state: '%s', can rejoin: '%b', game full: '%b'",
					player.getName(), hashCode(), gameType, gameState, canRejoin, players.size() >= maxPlayers
			);

			return;
		}

//		if (useTeams) {
//			// TODO
//		}

		players.add(player);

		PlayerConstraintManager.applyConstraints(player, Constraints.WAITING);
		WorldManager.teleportToSpawn(player, world);

		if (players.size() >= joiningPlayers - cancelledPlayers) {
			start();
		}
	}

	public void removePlayer(Player removedPlayer) {
		if (!players.contains(removedPlayer)) {
			Debug.warn("Attempted to remove '%s' from a game they're not in", removedPlayer);
			return;
		}

		for (Player gamePlayer : players) {
			if (gamePlayer != removedPlayer) {
				gamePlayer.sendMessage(String.format("%s has left", removedPlayer.getDisplayName()));
			}
		}

		players.remove(removedPlayer);

		if (players.size() == 0) {
			cancel();
		}

		cancelledPlayers++;

		if (gameState == GameState.WAITING && players.size() - cancelledPlayers < minPlayers) {
			cancel();
		}
	}

	private void cancel() {
		for (Player player : players) {
			WorldManager.teleportToSpawn(player, Bukkit.getWorld("hub"));

			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME CANCELLED: Not enough players to start!");
		}

		GameManager.removeActiveGame(this);
		WorldManager.deleteWorld(world, true);
	}

	public void end(Player winner) {
		Title title = new Title(String.format(ChatColor.GREEN + "%s has won!", winner.getName()), "", 0, 5, 0);

		for (Player player : players) {
			new Countdown(
					5,
					null,
					() -> WorldManager.teleportToSpawn(player, Bukkit.getWorld("hub"))
			).start();

			title.send(player);
		}

		GameManager.removeActiveGame(this);

		new Countdown(
				5,
				null,
				() -> WorldManager.deleteWorld(world, true)
		).start();
	}

	private void start() {
		if (gameState != GameState.WAITING) {
			Debug.warn("Failed to start game '%s' while in state '%s'", world.getName(), gameState.toString());

			return;
		}

		gameState = GameState.STARTING;

		new Countdown(5,
				(timer) -> {
					Title title = new Title(String.format(ChatColor.GOLD + "Game starting in %s seconds", timer.getSeconds()), "", 0, 1, 0);

					for (Player player : players) {
						player.sendMessage(String.format(ChatColor.GOLD + "" + ChatColor.BOLD + "Game starts in: %d!", timer.getSeconds()));
						title.send(player);
					}
				},
				() -> {
					for (Player player : players) {
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Game started!");
						PlayerConstraintManager.applyConstraints(player, gameConstraints);
					}
				}
		).start();
	}

	public GameType getGameType() {
		return gameType;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

	public Player[] getPlayers() {
		return players.toArray(new Player[0]);
	}

	public boolean containsPlayer(Player player) {
		return players.stream().anyMatch(x -> x.getUniqueId() == player.getUniqueId());
	}

	public Constraints getGameConstraints() {
		return gameConstraints;
	}

	public boolean canUseTeams() {
		return useTeams;
	}

	public boolean getCanRejoin() {
		return canRejoin;
	}
}

