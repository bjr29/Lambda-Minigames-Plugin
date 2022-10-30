package com.bjrushworth29.lambdaminigames.managers;

import com.bjrushworth29.lambdaminigames.enums.Item;
import com.bjrushworth29.lambdaminigames.utils.Debug;
import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
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

		ITEMS.put("screenSumoGame", create(Material.WOOL, ChatColor.BLUE + "Sumo"));
		ITEMS.put("screenDuelsGame", create(Material.DIAMOND_SWORD, ChatColor.BLUE + "Duels"));

		ITEMS.put("diamondSword", create(Material.DIAMOND_SWORD, true));

		Debug.info(DebugLevel.MIN, "Initialised items");
	}

	public static ItemStack getItem(Item item) {
		return getItem(item.toString());
	}

	public static ItemStack getItem(String name) {
		ItemStack item = ITEMS.get(name);

		if (item == null) {
			Debug.warn("Attempted to get item '%s' but it doesn't exist", item);
		}

		return item;
	}

	public static boolean equalMeta(ItemStack a, ItemStack b) {
		return Objects.equals(a.getItemMeta().getDisplayName(), b.getItemMeta().getDisplayName());
	}

	public static ItemStack create(Material material) {
		return create(material, false, null, 1);
	}

	public static ItemStack create(Material material, String name) {
		return create(material, false, name, 1);
	}

	public static ItemStack create(Material material,  int stackSize) {
		return create(material, false, null, stackSize);
	}

	public static ItemStack create(Material material, boolean unbreakable) {
		return create(material, unbreakable, null, 1);
	}

	public static ItemStack create(Material material, boolean unbreakable, String displayName, int stackSize) {
		ItemStack item = new ItemStack(material, stackSize);
		ItemMeta meta = item.getItemMeta();

		if (displayName != null) {
			meta.setDisplayName(displayName);
		}

		meta.spigot().setUnbreakable(unbreakable);

		item.setItemMeta(meta);

		return item;
	}
}
