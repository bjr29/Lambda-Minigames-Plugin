package com.bjrushworth29.lambdaminigames.events;

import com.bjrushworth29.lambdaminigames.enums.Constraints;
import com.bjrushworth29.lambdaminigames.enums.DefaultInventoryLoadout;
import com.bjrushworth29.lambdaminigames.enums.DefaultWorld;
import com.bjrushworth29.lambdaminigames.games.util.Game;
import com.bjrushworth29.lambdaminigames.managers.GameManager;
import com.bjrushworth29.lambdaminigames.managers.InventoryLoadoutManager;
import com.bjrushworth29.lambdaminigames.utils.PlayerUtil;
import com.bjrushworth29.lambdaminigames.managers.PlayerConstraintManager;
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

		if (worldName.equals(DefaultWorld.HUB.toString())) {
			Game game = GameManager.getPlayerGame(player);

			if (game != null) {
				game.removePlayer(player);
			}

			InventoryLoadoutManager.giveInventoryLoadout(player, InventoryLoadoutManager.getDefaultLoadout(DefaultInventoryLoadout.HUB));
			PlayerConstraintManager.applyConstraints(player, Constraints.HUB);
		}
	}
}
