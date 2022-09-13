package com.bjrushworth29.items;

import com.bjrushworth29.managers.ItemManager;
import com.bjrushworth29.managers.ScreenManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectGameItem implements Listener {
	@EventHandler
	private void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (item == null || !ItemManager.equalMeta(item, ItemManager.getItem("Select Game"))) {
			return;
		}

		player.openInventory(ScreenManager.getDefaultScreen("Select Game"));
	}
}
