package com.bjrushworth29.events;

import com.bjrushworth29.games.util.Game;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.PlayerConstraintManager;
import com.bjrushworth29.utils.Debug;
import com.bjrushworth29.utils.PlayerGameData;
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

		if (!PlayerConstraintManager.getAppliedConstraints(player).takesAnyDamage() ||
			(!PlayerConstraintManager.getAppliedConstraints(player).takesFallDamage() && event.getCause() == EntityDamageEvent.DamageCause.FALL)
		) {
			event.setCancelled(true);
		}

		Game game = GameManager.getPlayerGame(player);

		if (game == null) {
			return;
		}

		PlayerGameData gameConstraints = game.getPlayerGameConstraints(player);

		if (gameConstraints.hasSpawnProtection()) {
			event.setCancelled(true);
			return;
		}

		if (player.getHealth() - event.getFinalDamage() <= 0) {
			event.setCancelled(true);

			game.handleDeathOrLeave(player, false);
		}
	}

	@EventHandler
	private void handler(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player damager)) {
			return;
		}

		if (!(PlayerConstraintManager.getAppliedConstraints(player).pvp() && PlayerConstraintManager.getAppliedConstraints(damager).pvp())) {
			event.setCancelled(true);
			return;
		}

		Game game = GameManager.getPlayerGame(player);

		if (game != GameManager.getPlayerGame(damager)) {
			Debug.warn("Players '%s' and '%s' attempted to attack one another but are in separate games", player, damager);

			return;
		}

		if (game == null) {
			return;
		}

		PlayerGameData playerGameConstraints = game.getPlayerGameConstraints(player);
		PlayerGameData damagerGameConstraints = game.getPlayerGameConstraints(damager);

		if (playerGameConstraints.hasSpawnProtection()) {
			event.setCancelled(true);
			return;

		} else if (damagerGameConstraints.hasSpawnProtection()) {
			damagerGameConstraints.removeSpawnProtection();
		}

		if (player.getHealth() - event.getFinalDamage() <= 0) {
			event.setCancelled(true);

			game.handleDeathOrLeave(player, false);
		}
	}
}
