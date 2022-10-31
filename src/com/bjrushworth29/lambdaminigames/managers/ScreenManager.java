package com.bjrushworth29.lambdaminigames.managers;

import com.bjrushworth29.lambdaminigames.enums.InventoryRows;
import com.bjrushworth29.lambdaminigames.enums.ScreenItem;
import com.bjrushworth29.lambdaminigames.utils.Debug;
import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ScreenManager {
	private static final HashMap<String, Inventory> SCREENS = new HashMap<>();

	static {
		ItemStack[] content;

		content = new ItemStack[InventoryRows.THREE.getValue()];
		content[InventoryRows.ONE.getValue() + 1] = ItemManager.getItem(ScreenItem.SUMO_GAME.toString());
		content[InventoryRows.ONE.getValue() + 2] = ItemManager.getItem(ScreenItem.DUELS_GAME.toString());
		content[InventoryRows.ONE.getValue() + 3] = ItemManager.getItem(ScreenItem.DUELS_ROD.toString());
		content[InventoryRows.ONE.getValue() + 4] = ItemManager.getItem(ScreenItem.DUELS_BOW.toString());

		SCREENS.put("selectGame", create(content));

		Debug.info(DebugLevel.MIN, "Initialised inventory screens");
	}

	public static Inventory create(ItemStack[] contents) {
		Inventory screen = Bukkit.createInventory(null, contents.length);
		screen.setContents(contents);

		return screen;
	}

	public static Inventory getDefaultScreen(String name) {
		return SCREENS.get(name);
	}
}
