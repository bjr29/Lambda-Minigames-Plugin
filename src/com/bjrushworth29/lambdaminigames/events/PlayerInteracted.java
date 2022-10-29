package com.bjrushworth29.lambdaminigames.events;

import com.bjrushworth29.lambdaminigames.managers.PlayerConstraintManager;
import com.bjrushworth29.lambdaminigames.utils.PlayerConstraints;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteracted implements Listener {
	@EventHandler
	private void handler(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		PlayerConstraints constraints = PlayerConstraintManager.getAppliedConstraints(player);

		event.setCancelled(
				(!constraints.canInteractBlocks() && event.hasBlock())
				||
				(!constraints.canInteractItems() && event.hasItem())
		);
	}
}
