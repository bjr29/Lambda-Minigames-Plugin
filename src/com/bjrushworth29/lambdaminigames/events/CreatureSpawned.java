package com.bjrushworth29.lambdaminigames.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawned implements Listener {
	@EventHandler
	private void handler(CreatureSpawnEvent event) {
		event.getEntity().remove();
	}
}
