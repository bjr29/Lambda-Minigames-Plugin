package com.bjrushworth29.managers;

import com.bjrushworth29.utils.InventoryLoadout;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class InventoryLoadoutManager {
	private static final HashMap<String, InventoryLoadout> LOADOUTS = new HashMap<>();

	static {
		final ItemStack[] hotbarTemplate = new ItemStack[9];
		final ItemStack[] inventoryTemplate = new ItemStack[27];
		final ItemStack[] armorTemplate = new ItemStack[4];

		ItemStack[] hubHotbar = hotbarTemplate.clone();
		hubHotbar[0] = ItemManager.getItem("Select Game");

		LOADOUTS.put("hub", new InventoryLoadout(hubHotbar));

		System.out.print("Initialised inventory loadouts");
	}

	public static InventoryLoadout getDefaultLoadout(String name) {
		System.out.print(String.format("Inventory loadout '%s' is being given to", name));

		return LOADOUTS.get(name);
	}

	public static void giveInventoryLoadout(Player player, InventoryLoadout loadout) {
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(loadout.getInventory());
		inventory.setArmorContents(loadout.getArmor());

		System.out.print(String.format("'%s'!", player.getName()));
	}
}
