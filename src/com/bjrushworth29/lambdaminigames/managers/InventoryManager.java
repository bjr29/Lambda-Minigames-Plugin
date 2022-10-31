package com.bjrushworth29.lambdaminigames.managers;

import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
import com.bjrushworth29.lambdaminigames.enums.DefaultInventoryLoadout;
import com.bjrushworth29.lambdaminigames.enums.InventoryRows;
import com.bjrushworth29.lambdaminigames.enums.Item;
import com.bjrushworth29.lambdaminigames.utils.Debug;
import com.bjrushworth29.lambdaminigames.utils.InventoryLoadout;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
	private static final HashMap<String, InventoryLoadout> LOADOUTS = new HashMap<>();

	static {
		HashMap<Integer, ItemStack> hotbar;
		HashMap<Integer, ItemStack> inventory;
		HashMap<Integer, ItemStack> armor;

		// Hub
		hotbar = new HashMap<>();
		hotbar.put(0, ItemManager.getItem(Item.SELECT_GAME));

		add(DefaultInventoryLoadout.HUB.toString(), new InventoryLoadout(hotbar));

		// Hub (Queued)
		hotbar = new HashMap<>();
		hotbar.put(0, ItemManager.getItem(Item.LEAVE_QUEUE));

		add(DefaultInventoryLoadout.HUB_QUEUED.toString(), new InventoryLoadout(hotbar));

		// Duels
		hotbar = new HashMap<>();
		hotbar.put(0, ItemManager.getItem(Item.DIAMOND_SWORD));
		hotbar.put(1, ItemManager.create(Material.GOLDEN_APPLE, 4));

		armor = new HashMap<>();
		armor.put(3, ItemManager.create(Material.DIAMOND_HELMET));
		armor.put(2, ItemManager.create(Material.DIAMOND_CHESTPLATE));
		armor.put(1, ItemManager.create(Material.DIAMOND_LEGGINGS));
		armor.put(0, ItemManager.create(Material.DIAMOND_BOOTS));

		add(DefaultInventoryLoadout.DUELS.toString(), new InventoryLoadout(hotbar, armor));

		// Duels Rod
		hotbar = new HashMap<>();
		hotbar.put(0, ItemManager.getItem(Item.DIAMOND_SWORD));
		hotbar.put(1, ItemManager.create(Material.GOLDEN_APPLE, 4));
		hotbar.put(2, ItemManager.create(Material.FISHING_ROD));

		armor = new HashMap<>();
		armor.put(3, ItemManager.create(Material.DIAMOND_HELMET));
		armor.put(2, ItemManager.create(Material.DIAMOND_CHESTPLATE));
		armor.put(1, ItemManager.create(Material.DIAMOND_LEGGINGS));
		armor.put(0, ItemManager.create(Material.DIAMOND_BOOTS));

		add(DefaultInventoryLoadout.DUELS_ROD.toString(), new InventoryLoadout(hotbar, armor));

		// Duels Bow
		hotbar = new HashMap<>();
		hotbar.put(0, ItemManager.getItem(Item.INFINITY_BOW));
		hotbar.put(1, ItemManager.create(Material.GOLDEN_APPLE, 2));
		hotbar.put(8, ItemManager.create(Material.ARROW));

		armor = new HashMap<>();
		armor.put(3, ItemManager.create(Material.LEATHER_HELMET));
		armor.put(2, ItemManager.create(Material.LEATHER_CHESTPLATE));
		armor.put(1, ItemManager.create(Material.LEATHER_LEGGINGS));
		armor.put(0, ItemManager.create(Material.LEATHER_BOOTS));

		// Duels Soup
		hotbar = new HashMap<>();
		hotbar.put(0, ItemManager.getItem(Item.DIAMOND_SWORD));
		hotbar.put(1, ItemManager.create(Material.GOLDEN_APPLE, 4));

		for (int i = 2; i < 9; i++) {
			hotbar.put(i, ItemManager.getItem(Item.INSTANT_SOUP));
		}

		inventory = new HashMap<>();

		for (int i = 0; i < InventoryRows.THREE.getValue(); i++) {
			inventory.put(i, ItemManager.getItem(Item.INSTANT_SOUP));
		}

		armor = new HashMap<>();
		armor.put(3, ItemManager.create(Material.DIAMOND_HELMET));
		armor.put(2, ItemManager.create(Material.DIAMOND_CHESTPLATE));
		armor.put(1, ItemManager.create(Material.DIAMOND_LEGGINGS));
		armor.put(0, ItemManager.create(Material.DIAMOND_BOOTS));

		add(DefaultInventoryLoadout.DUELS_BOW.toString(), new InventoryLoadout(hotbar, armor));

		Debug.info(DebugLevel.MIN, "Initialised inventory loadouts");
	}

	public static void add(String name, InventoryLoadout inventoryLoadout) {
		LOADOUTS.put(name, inventoryLoadout);
	}

	public static InventoryLoadout get(DefaultInventoryLoadout inventoryLoadout) {
		return get(inventoryLoadout.toString());
	}

	public static InventoryLoadout get(String name) {
		InventoryLoadout loadout = LOADOUTS.get(name);

		if (loadout == null) {
			Debug.warn("Attempted to get default loadout '%s' but doesn't exist", name);
		}

		return loadout;
	}

	public static void give(Player player, InventoryLoadout loadout) {
		PlayerInventory inventory = player.getInventory();

		if (loadout == null) {
			inventory.clear();

			return;
		}

		inventory.setContents(loadout.getInventory());
		inventory.setArmorContents(loadout.getArmor());

		String loadoutName = "<UNKNOWN>";

		for (int i = 0; i < LOADOUTS.size(); i++) {
			if (LOADOUTS.values().toArray()[i] == loadout) {
				loadoutName = (String) LOADOUTS.entrySet().toArray(new Map.Entry[0])[i].getKey();
			}
		}

		Debug.info(DebugLevel.FULL, "Player '%s' is being given inventory loadout '%s'", player.getName(), loadoutName);
	}
}
