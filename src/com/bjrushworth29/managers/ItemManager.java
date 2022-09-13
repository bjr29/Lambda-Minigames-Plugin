package com.bjrushworth29.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Objects;

public class ItemManager {
	private static final HashMap<String, ItemStack> ITEMS = new HashMap<>();

	static {
		ITEMS.put("Select Game", create(Material.COMPASS, ChatColor.BLUE + "Select Game"));

		ITEMS.put("SG Screen - Test Game", create(Material.DIAMOND_SWORD, ChatColor.BLUE + "Test Game"));

		System.out.print("Initialised items");
	}

	public static ItemStack getItem(String name) {
		return ITEMS.get(name);
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
