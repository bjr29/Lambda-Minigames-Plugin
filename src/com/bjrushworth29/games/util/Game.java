package com.bjrushworth29.games.util;

import com.bjrushworth29.enums.*;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.managers.WorldManager;
import com.bjrushworth29.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
	private final GameType gameType;
	private final Constraints gameConstraints;

	private final int maxPlayers;
	private final int minPlayers;
	private final int startingLives;
	private final int spawnProtectionDuration;

	private final boolean useTeams;
	private final boolean canRejoin;
	private GameState gameState;

	private Vector mapOffset;
	private int gameSquareId;
	private GameMap mapSettings;

	private final HashMap<Player, PlayerGameData> players;

	private int joiningPlayers;
	private ArrayList<Team> teams;

	public Game(GameType gameType,
				Constraints gameConstraints,
				int minPlayers,
				int maxPlayers,
	            boolean useTeams,
				boolean canRejoin,
				int startingLives,
				int spawnProtectionLength) {
		this.maxPlayers = maxPlayers;
		this.minPlayers = minPlayers;
		this.startingLives = startingLives;
		this.spawnProtectionDuration = spawnProtectionLength;
		this.players = new HashMap<>();
		this.useTeams = useTeams;
		this.gameType = gameType;
		this.gameConstraints = gameConstraints;
		this.canRejoin = canRejoin;
	}

	@SuppressWarnings("CopyConstructorMissesField")
	public Game(Game game) {
		this.maxPlayers = game.getMaxPlayers();
		this.minPlayers = game.getMinPlayers();
		this.startingLives = game.getStartingLives();
		this.spawnProtectionDuration = game.getSpawnProtectionDuration();
		this.players = new HashMap<>();
		this.useTeams = game.canUseTeams();
		this.gameType = game.getGameType();
		this.gameConstraints = game.getGameConstraints();
		this.canRejoin = game.getCanRejoin();
	}

	public void init(GameMap worldSettings, Vector mapOffset, int gameSquareId) {
		this.mapSettings = worldSettings;
		this.mapOffset = mapOffset;
		this.gameSquareId = gameSquareId;

		worldSettings.createMap(mapOffset);

		gameState = GameState.WAITING;
	}

	public void addPlayer(Player player) {
		if (gameState != GameState.WAITING || players.size() >= maxPlayers) {
			Debug.warn(
					"Player '%s' attempted to join game (id: '%h') '%s' but was refused. Game state: '%s', " +
							"can rejoin: '%b', game full: '%b'",
					player.getName(), hashCode(), gameType, gameState, canRejoin, players.size() >= maxPlayers
			);

			return;
		}

//		if (useTeams) {
//			// TODO
//		}

		PlayerGameData playerGameData = new PlayerGameData();
		playerGameData.setLives(startingLives);
		playerGameData.giveSpawnProtection(spawnProtectionDuration);

		players.put(player, playerGameData);

		PlayerConstraintManager.applyConstraints(player, Constraints.WAITING);

		List<TeamObject<Location>> spawnPoints = mapSettings.getSpawnPoints();

		Location location = mapSettings.getSpawnPoints()
				.get(getPlayers().size() % spawnPoints.size())
				.getObject()
				.add(mapOffset);

		player.teleport(location);
		Debug.info(DebugLevel.FULL, "Teleported player '%s' into game", player.getName());

		players.get(player).setSpawnLocation(location);

		if (players.size() >= joiningPlayers) {
			start();
		}
	}

	public void removePlayer(Player player) {
		if (!players.containsKey(player)) {
			return;
		}

		for (Player gamePlayer : getPlayers()) {
			if (gamePlayer != player) {
				gamePlayer.sendMessage(String.format("%s has left", player.getDisplayName()));
			}
		}

		if (gameState == GameState.WAITING || gameState == GameState.STARTING) {
			players.remove(player);

			Debug.info(DebugLevel.FULL, "Player '%s' has cancelled, %s players remain", player.getName(), getPlayers().size());

		} else {
			handleDeathOrLeave(player, true);
		}

		if ((getPlayers().size() == 0 || getPlayers().size() < minPlayers)
				&& (gameState == GameState.WAITING || gameState == GameState.STARTING)) {
			Debug.info(DebugLevel.FULL, "Cancelled game");
			cancel();
		}
	}

	private void cancel() {
		gameState = GameState.CANCELLED;

		for (Player player : getPlayers()) {
			WorldManager.teleportToSpawn(player, WorldManager.getWorld(DefaultWorld.HUB));

			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME CANCELLED: Not enough players to start!");
		}

		GameManager.removeActiveGame(this);
	}

	private void start() {
		if (gameState != GameState.WAITING) {
			Debug.warn("Failed to start game '%s' while in state '%s'", mapSettings.name(), gameState.toString());

			return;
		}

		gameState = GameState.STARTING;
		Debug.info(DebugLevel.FULL, "Starting game");

		new Countdown(
				5,
				(timer) -> {
					if (gameState == GameState.CANCELLED) {
						timer.stop();
						return;
					}

					Title title = new Title(
							String.format(ChatColor.GOLD + "Game starting in %s seconds", timer.getSeconds()),
							"",
							0,
							1,
							0
					);

					for (Player player : getPlayers()) {
						player.sendMessage(String.format(ChatColor.GOLD + "" + ChatColor.BOLD + "Game starts in: %d!", timer.getSeconds()));
						title.send(player);
					}
				},
				() -> {
					gameState = GameState.PLAYING;

					for (Player player : getPlayers()) {
						player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Game started!");
						PlayerConstraintManager.applyConstraints(player, gameConstraints);
					}

					Debug.info(DebugLevel.FULL, "Started game");
				}
		).start();
	}

	public void end(Player winner) {
		gameState = GameState.FINISHED;

		Debug.info(DebugLevel.FULL, "Ended game");

		if (winner == null) {
			GameManager.removeActiveGame(this);

			for (Player player : getPlayers()) {
				WorldManager.teleportToSpawn(player, WorldManager.getWorld(DefaultWorld.HUB));
			}

			return;
		}

		Title title = new Title(
				String.format(ChatColor.GREEN + "%s has won!", winner.getName()),
				"",
				0,
				5,
				1
		);

		for (Player player : getPlayers()) {
			new Countdown(
					5,
					null,
					() -> {
						if (getPlayers().contains(player)) {
							WorldManager.teleportToSpawn(player, WorldManager.getWorld(DefaultWorld.HUB));
						}
					}
			).start();

			title.send(player);
		}

		new Countdown(
				5,
				null,
				() -> GameManager.removeActiveGame(this)
		).start();
	}

	public void handleDeathOrLeave(Player player, boolean leave) {
		PlayerUtil.reset(player);

		PlayerGameData playerData = players.get(player);

		playerData.setInGame(!leave);

		if (gameState != GameState.PLAYING) {
			return;
		}

		if (playerData.getLives() > 0) {
			Debug.info(DebugLevel.FULL, "Player '%s' has lost a life", player.getName());
			playerData.decrementLives();
		}

		switch (gameType) {
			case BED_DESTRUCTION:
			case BED_LAST_STANDING:
				if (playerData.getLives() != 0) {
					new Countdown(
							3,
							timer -> {
								new Title(
										String.format("Respawning in %d seconds", timer.getSeconds()),
										"",
										0,
										1,
										0
								).send(player);

								if (!getPlayers().contains(player)) {
									timer.stop();
								}
							},
							() -> {
								if (!getPlayers().contains(player)) {
									return;
								}

								player.teleport(playerData.getSpawnLocation());
								PlayerConstraintManager.applyConstraints(player, gameConstraints);
							}
					).start();

					break;
				}

				PlayerConstraintManager.applyConstraints(player, Constraints.SPECTATOR);

				break;

			case PIT:
				// TODO
				break;

			case LAST_STANDING:
			case SUMO:
				PlayerConstraintManager.applyConstraints(player, Constraints.SPECTATOR);

				break;
		}

		if (playerData.getLives() == 0 && gameState == GameState.PLAYING && playerData.isInGame()) {
			PlayerConstraintManager.applyConstraints(player, Constraints.SPECTATOR);
			player.teleport(playerData.getSpawnLocation());

			checkEndState();

			Debug.info(DebugLevel.FULL, "Player '%s' has lost all lives", player.getName());

		} else if (!playerData.isInGame()) {
			Debug.info("Player '%s' has left the game", player.getName());

			checkEndState();
		}
	}

	public void handleMovement(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (event.getTo().getY() <= mapSettings.getKillBelow()) {
			handleDeathOrLeave(player, false);
		}
	}

	private void checkEndState() {
		if (getAlivePlayers().size() == 1) {
			end(getAlivePlayers().get(0));

		} else if (getAlivePlayers().size() == 0) {
			end(null);
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

	public List<Player> getPlayers() {
		return players
				.keySet()
				.stream()
				.filter(player -> players.get(player).isInGame())
				.collect(Collectors.toList());
	}

	public List<Player> getAlivePlayers() {
		return players
				.keySet()
				.stream()
				.filter(player -> {
					PlayerGameData playerData = players.get(player);

					return playerData.isInGame() && !playerData.isSpectator() && player.getGameMode() != GameMode.SPECTATOR;
				})
				.collect(Collectors.toList());
	}

	public boolean containsPlayer(Player player) {
		return getPlayers()
				.stream()
				.anyMatch(x -> x.getUniqueId() == player.getUniqueId());
	}

	public Constraints getGameConstraints() {
		return gameConstraints;
	}

	public PlayerGameData getPlayerGameConstraints(Player player) {
		return players.get(player);
	}

	public boolean canUseTeams() {
		return useTeams;
	}

	public boolean getCanRejoin() {
		return canRejoin;
	}

	public int getStartingLives() {
		return startingLives;
	}

	public int getSpawnProtectionDuration() {
		return spawnProtectionDuration;
	}

	public void setJoiningPlayers(int joiningPlayers) {
		this.joiningPlayers = joiningPlayers;
	}

	public Integer getSquareId() {
		return gameSquareId;
	}

	public Vector getMapOffset() {
		return mapOffset;
	}

	public GameMap getMap() {
		return mapSettings;
	}
}

