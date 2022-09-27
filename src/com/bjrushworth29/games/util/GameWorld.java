package com.bjrushworth29.games.util;

import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.WorldManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.List;

public record GameWorld(String name, int minTeams, int maxTeams, List<GameType> gameTypes,
						List<TeamObject<Location>> spawnPoints, List<TeamObject<Location>> bedLocations,
						List<TeamObject<Location>> pointPitLocations, int killBelow) {

	public World createSessionWorld() {
		String session_world = String.format("%d_active_game_%s", System.currentTimeMillis(), name);

		WorldManager.copyWorld(
				new File(String.format("game_%s", name)),
				new File(session_world)
		);

		return WorldManager.getWorld(session_world);
	}
}
