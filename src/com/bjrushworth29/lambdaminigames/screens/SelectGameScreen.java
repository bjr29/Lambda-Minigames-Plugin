package com.bjrushworth29.lambdaminigames.screens;

import com.bjrushworth29.lambdaminigames.enums.GameName;
import com.bjrushworth29.lambdaminigames.enums.ScreenItem;
import com.bjrushworth29.lambdaminigames.managers.GameManager;
import com.bjrushworth29.lambdaminigames.managers.ItemManager;
import com.bjrushworth29.lambdaminigames.managers.ScreenManager;
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

		if (clicked == null || clicked.getItemMeta() == null || !inventory.equals(ScreenManager.getDefaultScreen("selectGame"))) {
			return;
		}

		event.setCancelled(true);
		player.closeInventory();

		checkButtonPress(ScreenItem.SUMO_GAME, clicked, player, GameName.SUMO);
		checkButtonPress(ScreenItem.DUELS_GAME, clicked, player, GameName.DUELS);
		checkButtonPress(ScreenItem.DUELS_ROD, clicked, player, GameName.DUELS_ROD);
		checkButtonPress(ScreenItem.DUELS_BOW, clicked, player, GameName.DUELS_BOW);
	}

	private void checkButtonPress(ScreenItem buttonName, ItemStack clicked, Player player, GameName gameMode) {
		ItemStack sumoGameButton = ItemManager.getItem(buttonName.toString());

		if (ItemManager.equalMeta(clicked, sumoGameButton)) {
			GameManager.enterQueue(player, gameMode.toString());
		}
	}
}
