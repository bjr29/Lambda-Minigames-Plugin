package com.bjrushworth29.lambdaminigames.events;

import com.bjrushworth29.lambdaminigames.managers.PlayerConstraintManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {
    @EventHandler
    private void handler(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(!PlayerConstraintManager.getAppliedConstraints(player).canDropItems());
    }
}
