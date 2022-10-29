package com.bjrushworth29.lambdaminigames.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerUtil {
	public static void reset(Player player) {
		player.resetMaxHealth();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().clear();

		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}
}
