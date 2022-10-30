package com.bjrushworth29.lambdaminigames.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChanged implements Listener {
	@EventHandler
	private void handler(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
}
