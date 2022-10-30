package com.bjrushworth29.lambdaminigames.managers;

import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
import com.bjrushworth29.lambdaminigames.enums.DefaultInventoryLoadout;
import com.bjrushworth29.lambdaminigames.enums.Item;
import com.bjrushworth29.lambdaminigames.utils.Debug;
import com.bjrushworth29.lambdaminigames.utils.InventoryLoadout;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryLoadoutManager {
	private static final HashMap<String, InventoryLoadout> LOADOUTS = new HashMap<>();

	static {
		final ItemStack[] hotbarTemplate = new ItemStack[9];
		final ItemStack[] inventoryTemplate = new ItemStack[27];
		final ItemStack[] armorTemplate = new ItemStack[4];

		ItemStack[] hotbar;
		ItemStack[] inventory;
		ItemStack[] armor;

		// Hub
		hotbar = hotbarTemplate.clone();
		hotbar[0] = ItemManager.getItem("selectGame");

		addLoadout(DefaultInventoryLoadout.HUB.toString(), new InventoryLoadout(hotbar));

		// Hub (Queued)
		hotbar = hotbarTemplate.clone();
		hotbar[0] = ItemManager.getItem("leaveQueue");

		addLoadout(DefaultInventoryLoadout.HUB_QUEUED.toString(), new InventoryLoadout(hotbar));

		// Duels
		hotbar = hotbarTemplate.clone();
		hotbar[0] = ItemManager.getItem(Item.DIAMOND_SWORD);
		hotbar[1] = ItemManager.create(Material.GOLDEN_APPLE, 4);

		armor = armorTemplate.clone();
		armor[0] = ItemManager.create(Material.DIAMOND_HELMET);
		armor[1] = ItemManager.create(Material.DIAMOND_CHESTPLATE);
		armor[2] = ItemManager.create(Material.DIAMOND_LEGGINGS);
		armor[3] = ItemManager.create(Material.DIAMOND_BOOTS);

		addLoadout(DefaultInventoryLoadout.DUELS.toString(), new InventoryLoadout(hotbar, armor));

		Debug.info(DebugLevel.MIN, "Initialised inventory loadouts");
	}

	public static void addLoadout(String name, InventoryLoadout inventoryLoadout) {
		LOADOUTS.put(name, inventoryLoadout);
	}

	public static InventoryLoadout getLoadout(DefaultInventoryLoadout inventoryLoadout) {
		return getLoadout(inventoryLoadout.toString());
	}

	public static InventoryLoadout getLoadout(String name) {
		InventoryLoadout loadout = LOADOUTS.get(name);

		if (loadout == null) {
			Debug.warn("Attempted to get default loadout '%s' but doesn't exist", name);
		}

		return loadout;
	}

	public static void giveLoadout(Player player, InventoryLoadout loadout) {
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
