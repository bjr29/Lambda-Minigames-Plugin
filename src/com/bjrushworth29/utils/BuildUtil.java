package com.bjrushworth29.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Directional;
import org.bukkit.material.Openable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BuildUtil {
	public static HashMap<Vector, Block> getBlocksInArea(World world, Vector a, Vector b) {
		HashMap<Vector, Block> blocks = new HashMap<>();

		for (int z = a.getBlockZ(); z < b.getBlockZ(); z++) {
			for (int y = a.getBlockY(); y < b.getBlockY(); y++) {
				for (int x = a.getBlockX(); x < b.getBlockX(); x++) {
					Block block = world.getBlockAt(x, y, z);

					if (block.getType() != Material.AIR) {
						blocks.put(new Vector(x, y, z), block);
					}
				}
			}
		}

		return blocks;
	}

	public static void placeAll(HashMap<Vector, Block> blocks, World world, Vector offset) {
		for (Map.Entry<Vector, Block> entry : blocks.entrySet()) {
			Block block = entry.getValue();

			Block blockAt = world.getBlockAt(entry.getKey().add(offset).toLocation(world));
			blockAt.setType(block.getType());

			BlockState blockAtState = blockAt.getState();
			BlockState blockState = block.getState();

			if (blockState instanceof Directional) {
				((Directional) blockAtState).setFacingDirection(((Directional) blockState).getFacing());
			}

			if (blockState instanceof Openable) {
				((Openable) blockAtState).setOpen(((Openable) blockState).isOpen());
			}
		}
	}
}
