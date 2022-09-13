package com.bjrushworth29.managers;

import com.bjrushworth29.enums.InventoryRows;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ScreenManager {
	private static final HashMap<String, Inventory> SCREENS = new HashMap<>();

	static {
		ItemStack[] content;

		content = new ItemStack[InventoryRows.Three.getValue()];
		content[InventoryRows.One.getValue() + 2] = ItemManager.getItem("SG Screen - Test Game");

		SCREENS.put("Select Game", create(content));

		System.out.print("Initialised inventory screens");
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
