package com.bjrushworth29.screens;

import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.ItemManager;
import com.bjrushworth29.managers.ScreenManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SelectGameScreen implements Listener {
	@EventHandler
	private void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getClickedInventory();
		ItemStack clicked = event.getCurrentItem();

		if (clicked.getItemMeta() == null || !inventory.equals(ScreenManager.getDefaultScreen("selectGame"))) {
			return;
		}

		event.setCancelled(true);
		player.closeInventory();

		ItemStack testGameButton = ItemManager.getItem("screenTestGame");

		if (ItemManager.equalMeta(clicked, testGameButton)) {
			GameManager.enterQueue(player, "testGame");
		}
	}
}
