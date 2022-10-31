package com.bjrushworth29.lambdaminigames;

import com.bjrushworth29.lambdaminigames.commands.*;
import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
import com.bjrushworth29.lambdaminigames.enums.DefaultWorld;
import com.bjrushworth29.lambdaminigames.events.*;
import com.bjrushworth29.lambdaminigames.items.LeaveQueueItem;
import com.bjrushworth29.lambdaminigames.items.SelectGameItem;
import com.bjrushworth29.lambdaminigames.managers.GameManager;
import com.bjrushworth29.lambdaminigames.managers.WorldManager;
import com.bjrushworth29.lambdaminigames.screens.SelectGameScreen;
import com.bjrushworth29.lambdaminigames.utils.Debug;
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
		getCommand("addId").setExecutor(new AddGameIdCommand());
		getCommand("changeKnockback").setExecutor(new ChangeKnockbackCommand());
		getCommand("enableRemoveGame").setExecutor(new EnableRemoveGameCommand());
		getCommand("endGame").setExecutor(new EndGameCommand());
		getCommand("leaveQueue").setExecutor(new LeaveQueueCommand());
		getCommand("lobby").setExecutor(new LobbyCommand());
	}

	private void initEvents() {
		initEventClass(new CreatureSpawned());
		initEventClass(new PlayerChangedWorld());
		initEventClass(new PlayerDamaged());
		initEventClass(new PlayerDropItem());
		initEventClass(new PlayerHungerChanged());
		initEventClass(new PlayerInteracted());
		initEventClass(new PlayerJoinedServer());
		initEventClass(new PlayerLeftServer());
		initEventClass(new PlayerMoved());
		initEventClass(new PlayerVelocity());
		initEventClass(new WeatherChanged());
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
