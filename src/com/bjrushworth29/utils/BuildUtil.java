package com.bjrushworth29.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BuildUtil {
	public static HashMap<Vector, Block> getBlocksInArea(World world, Vector a, Vector b) {
		// TODO: Get block data https://www.spigotmc.org/threads/copy-blocks-in-area.487490/
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
			Block sourceBlock = entry.getValue();

			Location location = entry.getKey().add(offset).toLocation(world);

			world.getChunkAt(location.getBlockX(), location.getBlockZ()).load();

			Block newBlock = world.getBlockAt(location);
			newBlock.setType(sourceBlock.getType());

			BlockState newBlockState = newBlock.getState();
			BlockState sourceBlockState = sourceBlock.getState();

			newBlockState.setData(sourceBlockState.getData());
			newBlockState.update(true);
		}
	}

	public static void clearAll(Set<Vector> destroy, World world, Vector offset) {
		for (Vector vector : destroy) {
			world.getBlockAt(vector.toLocation(world)).setType(Material.AIR);
		}
	}
}
