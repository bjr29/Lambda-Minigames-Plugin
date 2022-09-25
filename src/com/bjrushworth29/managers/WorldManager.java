package com.bjrushworth29.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WorldManager {
	public static World getWorld(String worldName) {
		return Bukkit.createWorld(new WorldCreator(worldName));
	}

	public static void teleportToSpawn(Player player, String worldName) {
		teleportToSpawn(player, getWorld(worldName));
	}

	public static void teleportToSpawn(Player player, World world) {
		player.teleport(getSpawnLocation(world));
	}

	public static Location getSpawnLocation(World world) {
		Location spawnLocation = world.getSpawnLocation();
		return world.getHighestBlockAt(spawnLocation).getLocation().add(new Vector(0.5, 0, 0.5));
	}

	// All methods below are copied and pasted from: https://bukkit.org/threads/unload-delete-copy-worlds.182814/
	public static void unloadWorld(World world) {
		if(world != null) {
			Bukkit.getServer().unloadWorld(world, false);
		}
	}

	public static void deleteWorld(World world, boolean unloadWorld) {
		if (unloadWorld) {
			unloadWorld(world);
		}

		deleteWorld(world.getWorldFolder());
	}

	public static void deleteWorld(File worldFolder) {
		if (worldFolder.exists()) {
			File[] files = worldFolder.listFiles();

			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);

				} else {
					files[i].delete();
				}
			}
		}

		worldFolder.delete();
	}

	public static void copyWorld(File originalWorldFolder, File clonedWorldFolder){
		try {
			ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat"));

			if (!ignore.contains(originalWorldFolder.getName())) {
				if (originalWorldFolder.isDirectory()) {
					if (!clonedWorldFolder.exists()) {
						clonedWorldFolder.mkdirs();
					}

					String[] files = originalWorldFolder.list();

					for (String file : files) {
						File srcFile = new File(originalWorldFolder, file);
						File destFile = new File(clonedWorldFolder, file);

						copyWorld(srcFile, destFile);
					}

				} else {
					InputStream in = new FileInputStream(originalWorldFolder);
					OutputStream out = new FileOutputStream(clonedWorldFolder);

					byte[] buffer = new byte[1024];

					int length;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}

					in.close();
					out.close();
				}
			}

		} catch (IOException ignored) {

		}
	}
}
