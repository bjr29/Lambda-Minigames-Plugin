package com.bjrushworth29.events;

import com.bjrushworth29.managers.PlayerConstraintManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamaged implements Listener {
	@EventHandler
	private void handler(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}

		if (!PlayerConstraintManager.getAppliedConstraints(player).takesFallDamage() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void handler(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player damager)) {
			return;
		}

		if (!(PlayerConstraintManager.getAppliedConstraints(player).pvp() && PlayerConstraintManager.getAppliedConstraints(damager).pvp())) {
			event.setCancelled(true);
		}
	}
}
