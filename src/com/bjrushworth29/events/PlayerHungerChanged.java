package com.bjrushworth29.events;

import com.bjrushworth29.managers.PlayerConstraintManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerChanged implements Listener {
	@EventHandler
	private void handler(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();

		if (!PlayerConstraintManager.getAppliedConstraints(player).hungerEnabled()) {
			event.setCancelled(true);
		}
	}
}
