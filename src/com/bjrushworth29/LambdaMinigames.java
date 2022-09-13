package com.bjrushworth29;

import com.bjrushworth29.events.*;
import com.bjrushworth29.items.SelectGameItem;
import com.bjrushworth29.screens.SelectGameScreen;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class LambdaMinigames extends JavaPlugin {
	@Override
	public void onEnable() {
		initAll();
	}

	@Override
	public void onDisable() {

	}

	private void initAll() {
		initEvents();
		initItemEvents();
		initScreenEvents();
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
