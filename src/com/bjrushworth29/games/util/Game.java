package com.bjrushworth29.games.util;

import com.bjrushworth29.enums.Constraints;
import com.bjrushworth29.enums.DefaultWorld;
import com.bjrushworth29.enums.GameState;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.managers.WorldManager;
import com.bjrushworth29.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;

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

	private World world;
	private GameWorld worldSettings;

	private final HashMap<Player, PlayerGameData> players;

	private int joiningPlayers;
	private int cancelledPlayers;
	private ArrayList<Team> teams;

	public Game(GameType gameType,
				Constraints gameConstraints,
				int maxPlayers,
				int minPlayers,
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

	public void init(GameWorld worldSettings) {
		this.worldSettings = worldSettings;

		world = worldSettings.createSessionWorld();
		world.loadChunk(0, 0);

		gameState = GameState.WAITING;
	}

	public void addPlayer(Player player) {
		if (gameState != GameState.WAITING || (players.size() >= maxPlayers)) {
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
		WorldManager.teleportToSpawn(player, world);

		if (players.size() >= joiningPlayers - cancelledPlayers) {
			start();
		}
	}

	public void removePlayer(Player player) {
		if (!players.containsKey(player)) {
			Debug.warn("Attempted to remove '%s' from a game they're not in", player);
			return;
		}

		for (Player gamePlayer : getPlayers()) {
			if (gamePlayer != player) {
				gamePlayer.sendMessage(String.format("%s has left", player.getDisplayName()));
			}
		}

		if (gameState == GameState.WAITING) {
			players.remove(player);
			cancelledPlayers++;

		} else {
			handleDeathOrLeave(player, true);
		}

		if (getPlayers().size() == 0 || (gameState == GameState.WAITING && getPlayers().size() - cancelledPlayers < minPlayers)) {
			cancel();
		}
	}

	private void cancel() {
		for (Player player : getPlayers()) {
			WorldManager.teleportToSpawn(player, WorldManager.getWorld(DefaultWorld.HUB));

			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GAME CANCELLED: Not enough players to start!");
		}

		GameManager.removeActiveGame(this);
	}

	public void delete() {
		WorldManager.deleteWorld(world, true);
	}

	private void start() {
		if (gameState != GameState.WAITING) {
			Debug.warn("Failed to start game '%s' while in state '%s'", world.getName(), gameState.toString());

			return;
		}

		gameState = GameState.STARTING;

		new Countdown(5,
				(timer) -> {
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
					for (Player player : getPlayers()) {
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Game started!");
						PlayerConstraintManager.applyConstraints(player, gameConstraints);
					}
				}
		).start();
	}

	public void end(Player winner) {
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

		PlayerGameData data = players.get(player);

		data.setInGame(!leave);

		if (data.getLives() > 0) {
			data.decrementLives();
		}

		switch (gameType) {
			case BED_DESTRUCTION:
			case BED_LAST_STANDING:
				if (data.getLives() != 0) {
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

								WorldManager.teleportToSpawn(player, world);
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

				if (getPlayers().size() == 1) {
					end(getPlayers().get(0));
				}

				break;
		}

		if (data.getLives() == 0) {
			PlayerConstraintManager.applyConstraints(player, Constraints.SPECTATOR);
		}
	}

	public void handleMovement(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (event.getTo().getY() <= worldSettings.getKillBelow()) {
			handleDeathOrLeave(player, false);
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

	public boolean containsPlayer(Player player) {
		return getPlayers().stream().anyMatch(x -> x.getUniqueId() == player.getUniqueId());
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
}

