package com.bjrushworth29.utils;

import com.bjrushworth29.enums.GameType;
import com.bjrushworth29.managers.WorldManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;

public record GameWorld(String name, int minTeams, int maxTeams, ArrayList<GameType> gameTypes,
						TeamObject<Location>[] spawnPoints, TeamObject<Location>[] bedLocations,
						TeamObject<Location>[] pointPitLocations) {

	public World createSessionWorld() {
		String session_world = String.format("active_game_%s_%d", name, System.currentTimeMillis());

		WorldManager.copyWorld(
				new File(String.format("game_%s", name)),
				new File(session_world)
		);

		return WorldManager.getWorld(session_world);
	}
}
