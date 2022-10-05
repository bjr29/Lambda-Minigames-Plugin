package com.bjrushworth29;

import com.bjrushworth29.commands.ChangeKnockbackCommand;
import com.bjrushworth29.commands.EndGameCommand;
import com.bjrushworth29.commands.LeaveQueueCommand;
import com.bjrushworth29.commands.LobbyCommand;
import com.bjrushworth29.enums.DebugLevel;
import com.bjrushworth29.enums.DefaultWorld;
import com.bjrushworth29.events.*;
import com.bjrushworth29.items.LeaveQueueItem;
import com.bjrushworth29.items.SelectGameItem;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.managers.WorldManager;
import com.bjrushworth29.screens.SelectGameScreen;
import com.bjrushworth29.utils.Debug;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class LambdaMinigames extends JavaPlugin {
	private static JavaPlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;

		initAll();
	}

	@Override
	public void onDisable() {
		GameManager.removeActiveGames();
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

	private void initAll() {
		preloadWorlds();
		registerCommands();
		initEvents();
		initItemEvents();
		initScreenEvents();
	}

	private void preloadWorlds() {
		WorldManager.getWorld(DefaultWorld.GAMES);

		Debug.info(DebugLevel.MIN, "Preloaded game world");
	}

	private void registerCommands() {
		getCommand("changeKnockback").setExecutor(new ChangeKnockbackCommand());
		getCommand("endGame").setExecutor(new EndGameCommand());
		getCommand("leaveQueue").setExecutor(new LeaveQueueCommand());
		getCommand("lobby").setExecutor(new LobbyCommand());
	}

	private void initEvents() {
		initEventClass(new PlayerChangedWorld());
		initEventClass(new PlayerDamaged());
		initEventClass(new PlayerDropItem());
		initEventClass(new PlayerHungerChanged());
		initEventClass(new PlayerInteracted());
		initEventClass(new PlayerJoinedServer());
		initEventClass(new PlayerLeftServer());
		initEventClass(new PlayerMoved());
	}

	private void initItemEvents() {
		initEventClass(new SelectGameItem());
		initEventClass(new LeaveQueueItem());
	}

	private void initScreenEvents() {
		initEventClass(new SelectGameScreen());
	}

	private void initEventClass(Listener eventClass) {
		getServer().getPluginManager().registerEvents(eventClass, this);
	}
}
