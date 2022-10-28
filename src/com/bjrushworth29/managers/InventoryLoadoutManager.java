package com.bjrushworth29.managers;

import com.bjrushworth29.enums.DebugLevel;
import com.bjrushworth29.enums.DefaultInventoryLoadout;
import com.bjrushworth29.utils.Debug;
import com.bjrushworth29.utils.InventoryLoadout;
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

		// Hub
		ItemStack[] hubHotbar = hotbarTemplate.clone();
		hubHotbar[0] = ItemManager.getItem("selectGame");

		LOADOUTS.put(DefaultInventoryLoadout.HUB.toString(), new InventoryLoadout(hubHotbar));

		// Hub (Queued)
		ItemStack[] hubQueuedHotbar = hotbarTemplate.clone();
		hubQueuedHotbar[0] = ItemManager.getItem("leaveQueue");

		LOADOUTS.put(DefaultInventoryLoadout.HUB_QUEUED.toString(), new InventoryLoadout(hubQueuedHotbar));

		Debug.info(DebugLevel.MIN, "Initialised inventory loadouts");
	}

	public static InventoryLoadout getDefaultLoadout(DefaultInventoryLoadout inventoryLoadout) {
		return getDefaultLoadout(inventoryLoadout.toString());
	}

	public static InventoryLoadout getDefaultLoadout(String name) {
		InventoryLoadout loadout = LOADOUTS.get(name);

		if (loadout == null) {
			Debug.warn("Attempted to get default loadout '%s' but doesn't exist", name);
		}

		return loadout;
	}

	public static void giveInventoryLoadout(Player player, InventoryLoadout loadout) {
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
