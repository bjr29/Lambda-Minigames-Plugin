package com.bjrushworth29.lambdaminigames.items;

import com.bjrushworth29.lambdaminigames.enums.Item;
import com.bjrushworth29.lambdaminigames.managers.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InstantSoupItem implements Listener {
	@EventHandler
	private void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (item == null || !ItemManager.equalMeta(item, ItemManager.getItem(Item.INSTANT_SOUP))) {
			return;
		}

		player.setHealth(player.getHealth() + 3);
		player.setItemInHand(null);
	}
}
