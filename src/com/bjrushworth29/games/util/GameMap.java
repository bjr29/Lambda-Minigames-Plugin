package com.bjrushworth29.games.util;

import com.bjrushworth29.enums.DebugLevel;
import com.bjrushworth29.enums.DefaultWorld;
import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.WorldManager;
import com.bjrushworth29.utils.BuildUtil;
import com.bjrushworth29.utils.Debug;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class GameMap {
	private final String name;
	private final World world;
	private final int minTeams;
	private final int maxTeams;
	private final List<GameType> gameTypes;
	private final List<TeamObject<Location>> spawnPoints;
	private final List<TeamObject<Location>> bedLocations;
	private final List<TeamObject<Location>> pointPitLocations;
	private final HashMap<Vector, Block> map;
	private final Set<Vector> destroy;
	private final int killBelow;

	public GameMap(String name, DefaultWorld world, int minTeams, int maxTeams, List<GameType> gameTypes,
	               List<TeamObject<Location>> spawnPoints, List<TeamObject<Location>> bedLocations,
	               List<TeamObject<Location>> pointPitLocations, int killBelow) {
		this.name = name;
		this.world = WorldManager.getWorld(world);
		this.minTeams = minTeams;
		this.maxTeams = maxTeams;
		this.gameTypes = gameTypes;
		this.spawnPoints = spawnPoints;
		this.bedLocations = bedLocations;
		this.pointPitLocations = pointPitLocations;
		this.killBelow = killBelow;

		int mapScanSize = GameManager.GAME_SQUARE_SIZE / 16;

		Debug.info(DebugLevel.MIN, "Loading map '%s' into memory", name);

		map = BuildUtil.getBlocksInArea(
				this.world,
				new Vector(-mapScanSize, 50, -mapScanSize),
				new Vector(mapScanSize, 150, mapScanSize)
		);

		destroy = map.keySet();

		Debug.info(DebugLevel.MIN, "Loaded map '%s' into memory", name);
	}

	public void createMap(Vector offset) {
		BuildUtil.placeAll(map, WorldManager.getWorld(DefaultWorld.GAMES), offset);
	}

	public void destroy(Vector offset) {
		BuildUtil.clearAll(destroy, WorldManager.getWorld(DefaultWorld.GAMES), offset);
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
