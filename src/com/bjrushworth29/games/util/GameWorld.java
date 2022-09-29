package com.bjrushworth29.games.util;

import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.WorldManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.List;

public final class GameWorld {
	private final String name;
	private final int minTeams;
	private final int maxTeams;
	private final List<GameType> gameTypes;
	private final List<TeamObject<Location>> spawnPoints;
	private final List<TeamObject<Location>> bedLocations;
	private final List<TeamObject<Location>> pointPitLocations;
	private final int killBelow;

	public GameWorld(String name, int minTeams, int maxTeams, List<GameType> gameTypes,
					 List<TeamObject<Location>> spawnPoints, List<TeamObject<Location>> bedLocations,
					 List<TeamObject<Location>> pointPitLocations, int killBelow) {
		this.name = name;
		this.minTeams = minTeams;
		this.maxTeams = maxTeams;
		this.gameTypes = gameTypes;
		this.spawnPoints = spawnPoints;
		this.bedLocations = bedLocations;
		this.pointPitLocations = pointPitLocations;
		this.killBelow = killBelow;
	}

	public String name() {
		return name;
	}

	public int minTeams() {
		return minTeams;
	}

	public int maxTeams() {
		return maxTeams;
	}

	public World createSessionWorld() {
		String session_world = String.format("%d_active_game_%s", System.currentTimeMillis(), name);

		WorldManager.copyWorld(
				new File(String.format("game_%s", name)),
				new File(session_world)
		);

		return WorldManager.getWorld(session_world);
	}

	public List<GameType> getGameTypes() {
		return gameTypes;
	}

	public List<TeamObject<Location>> getSpawnPoints() {
		return spawnPoints;
	}

	public List<TeamObject<Location>> getBedLocations() {
		return bedLocations;
	}

	public List<TeamObject<Location>> getPointPitLocations() {
		return pointPitLocations;
	}

	public int getKillBelow() {
		return killBelow;
	}
}
