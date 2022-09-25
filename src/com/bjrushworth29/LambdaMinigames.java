package com.bjrushworth29;

import com.bjrushworth29.commands.EndGameCommand;
import com.bjrushworth29.commands.LobbyCommand;
import com.bjrushworth29.events.*;
import com.bjrushworth29.items.SelectGameItem;
import com.bjrushworth29.managers.GameManager;
import com.bjrushworth29.screens.SelectGameScreen;
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
		registerCommands();
		initEvents();
		initItemEvents();
		initScreenEvents();
	}

	private void registerCommands() {
		getCommand("endgame").setExecutor(new EndGameCommand());
		getCommand("lobby").setExecutor(new LobbyCommand());
	}

	private void initEvents() {
		initEventClass(new PlayerChangedWorld());
		initEventClass(new PlayerDamaged());
		initEventClass(new PlayerHungerChanged());
		initEventClass(new PlayerInteracted());
		initEventClass(new PlayerJoinedServer());
		initEventClass(new PlayerLeftServer());
		initEventClass(new PlayerMoved());
	}

	private void initItemEvents() {
		initEventClass(new SelectGameItem());
	}

	private void initScreenEvents() {
		initEventClass(new SelectGameScreen());
	}

	private void initEventClass(Listener eventClass) {
		getServer().getPluginManager().registerEvents(eventClass, this);
	}
}
