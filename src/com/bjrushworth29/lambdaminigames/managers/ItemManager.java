package com.bjrushworth29.lambdaminigames.managers;

import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
import com.bjrushworth29.lambdaminigames.enums.Item;
import com.bjrushworth29.lambdaminigames.utils.Debug;
import com.bjrushworth29.lambdaminigames.utils.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemManager {
	private static final HashMap<String, ItemStack> ITEMS = new HashMap<>();

	static {
		createUtilItems();
		createScreenItems();
		createGameItems();

		Debug.info(DebugLevel.MIN, "Initialised items");
	}

	private static void createGameItems() {
		ITEMS.put("diamondSword", create(Material.DIAMOND_SWORD, true));
		ITEMS.put("infinityBow", create(Material.BOW, true, Collections.singletonList(new Tuple<>(Enchantment.ARROW_INFINITE, 1))));
		ITEMS.put("instantSoup", create(Material.MUSHROOM_SOUP, "Instant Soup"));
	}

	private static void createScreenItems() {
		ITEMS.put("screenSumoGame", create(Material.WOOL, ChatColor.BLUE + "Sumo"));
		ITEMS.put("screenDuelsGame", create(Material.DIAMOND_SWORD, ChatColor.BLUE + "Regular Duels"));
		ITEMS.put("screenDuelsRodGame", create(Material.FISHING_ROD, ChatColor.BLUE + "Rod Duels"));
		ITEMS.put("screenDuelsBowGame", create(Material.BOW, ChatColor.BLUE + "Bow Duels"));
		ITEMS.put("screenDuelsSoupGame", create(Material.MUSHROOM_SOUP, ChatColor.BLUE + "Soup Duels"));
	}

	private static void createUtilItems() {
		ITEMS.put("selectGame", create(Material.COMPASS, ChatColor.BLUE + "Select Game"));
		ITEMS.put("leaveQueue", create(Material.REDSTONE, ChatColor.RED + "Leave Queue"));
	}

	public static ItemStack getItem(Item item) {
		return getItem(item.toString());
	}

	public static ItemStack getItem(String name) {
		ItemStack item = ITEMS.get(name);

		if (item == null) {
			Debug.warn("Attempted to get item '%s' but it doesn't exist", name);
		}

		return item;
	}

	public static boolean equalMeta(ItemStack a, ItemStack b) {
		return Objects.equals(a.getItemMeta().getDisplayName(), b.getItemMeta().getDisplayName());
	}

	public static ItemStack create(Material material) {
		return create(material, null, false, 1, Collections.emptyList());
	}

	public static ItemStack create(Material material, String displayName) {
		return create(material, displayName, false, 1, Collections.emptyList());
	}

	public static ItemStack create(Material material, int stackSize) {
		return create(material, null, false, stackSize, Collections.emptyList());
	}

	public static ItemStack create(Material material, boolean unbreakable) {
		return create(material, null, unbreakable, 1, Collections.emptyList());
	}

	public static ItemStack create(Material material, boolean unbreakable, List<Tuple<Enchantment, Integer>> enchantments) {
		return create(material, null, unbreakable, 1, enchantments);
	}

	public static ItemStack create(Material material, String displayName, boolean unbreakable, int stackSize, List<Tuple<Enchantment, Integer>> enchantments) {
		ItemStack item = new ItemStack(material, stackSize);
		ItemMeta meta = item.getItemMeta();

		if (displayName != null) {
			meta.setDisplayName(displayName);
		}

		meta.spigot().setUnbreakable(unbreakable);

		for (Tuple<Enchantment, Integer> enchantment : enchantments) {
			meta.addEnchant(enchantment.getA(), enchantment.getB(), true);
		}

		item.setItemMeta(meta);

		return item;
	}
}
