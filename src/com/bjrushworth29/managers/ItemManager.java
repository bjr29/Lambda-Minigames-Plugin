package com.bjrushworth29.managers;

import com.bjrushworth29.utils.Debug;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Objects;

public class ItemManager {
	private static final HashMap<String, ItemStack> ITEMS = new HashMap<>();

	static {
		ITEMS.put("selectGame", create(Material.COMPASS, ChatColor.BLUE + "Select Game"));
		ITEMS.put("leaveQueue", create(Material.REDSTONE, ChatColor.RED + "Leave Queue"));

		ITEMS.put("screenTestGame", create(Material.DIAMOND_SWORD, ChatColor.BLUE + "Test Game"));
		ITEMS.put("screenSumoGame", create(Material.WOOL, ChatColor.BLUE + "Sumo"));

		Debug.info("Initialised items");
	}

	public static ItemStack getItem(String name) {
		ItemStack item = ITEMS.get(name);

		if (item == null) {
			Debug.warn("Attempted to get item '%s' but it doesn't exist");
		}

		return item;
	}

	public static boolean equalMeta(ItemStack a, ItemStack b) {
		return Objects.equals(a.getItemMeta().getDisplayName(), b.getItemMeta().getDisplayName());
	}

	private static ItemStack create(Material material, String name) {
		return create(material, name, 1);
	}

	private static ItemStack create(Material material, String displayName, int stackSize) {
		ItemStack item = new ItemStack(material, stackSize);
		ItemMeta meta = item.getItemMeta();

		if (displayName != null) {
			meta.setDisplayName(displayName);
		}

		item.setItemMeta(meta);

		return item;
	}
}
