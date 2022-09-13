package com.bjrushworth29.events;

import com.bjrushworth29.managers.InventoryLoadoutManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorld implements Listener {
	@EventHandler
	private void handler(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();

		PlayerUtil.reset(player);
		InventoryLoadoutManager.giveInventoryLoadout(player, InventoryLoadoutManager.getDefaultLoadout(worldName));
		PlayerConstraintManager.applyConstraints(player, worldName);
	}
}
