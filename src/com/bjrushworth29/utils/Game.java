package com.bjrushworth29.utils;

import com.bjrushworth29.enums.Constraints;
import com.bjrushworth29.enums.GameState;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.managers.WorldManager;
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

	private ArrayList<Player> players;

	private int joiningPlayers;
	private int cancelledPlayers;
	private ArrayList<Team> teams;

	public Game(GameType gameType, Constraints gameConstraints, int maxPlayers, int minPlayers, boolean useTeams, boolean canRejoin) {
		this.maxPlayers = maxPlayers;
		this.minPlayers = minPlayers;
		this.useTeams = useTeams;
		this.gameType = gameType;
		this.gameConstraints = gameConstraints;
		this.canRejoin = canRejoin;
	}

	public void init(Player[] players, GameWorld worldSettings) {
		this.worldSettings = worldSettings;
		joiningPlayers = players.length;

		world = worldSettings.createSessionWorld();

		for (Player player : players) {
			addPlayer(player);
		}
	}

	public void addPlayer(Player player) {
		if ((gameState != GameState.WAITING && !canRejoin) || (players.size() >= maxPlayers)) {
			return;
		}

//		if (useTeams) {
//			// TODO
//		}

		players.add(player);

		PlayerConstraintManager.applyConstraints(player, Constraints.WAITING);

		if (players.size() >= joiningPlayers - cancelledPlayers) {
			start();
		}
	}

	public void removePlayer(Player removedPlayer) {
		if (!players.contains(removedPlayer)) {
			return;
		}

		for (Player gamePlayer : players) {
			gamePlayer.sendMessage(String.format("%s has left", removedPlayer.getDisplayName()));
		}

		players.remove(removedPlayer);

		cancelledPlayers++;

		if (gameState == GameState.WAITING && players.size() < minPlayers) {
			cancelGame();
		}
	}

	private void cancelGame() {
		for (Player player : players) {
			WorldManager.teleportToSpawn(player, Bukkit.getWorld("hub"));
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME CANCELLED: Players left!");
		}

		GameManager.destroyGame(this);
	}

	private void start() {
		if (gameState != GameState.WAITING) {
			System.out.printf("Failed to start '%s' while in state '%s'", world.getName(), gameState.toString());

			return;
		}

		gameState = GameState.STARTING;

		// TODO: Start countdown

		for (Player player : players) {
			PlayerConstraintManager.applyConstraints(player, gameConstraints);
		}
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
}

