package com.bjrushworth29.utils;

import com.bjrushworth29.LambdaMinigames;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class Countdown implements Runnable {
	private Integer id;

	private int seconds;
	private boolean running;

	public Consumer<Countdown> tick;
	public Runnable completed;

	public Countdown(int seconds, Consumer<Countdown> tick, Runnable completed) {
		this.seconds = seconds;
		this.tick = tick;
		this.completed = completed;
	}

	@Deprecated
	@Override
	public void run() {
		if (seconds <= 0) {
			completed.run();

			if (id != null) {
				running = false;
				Bukkit.getScheduler().cancelTask(id);
			}

			return;
		}

		tick.accept(this);

		seconds--;
	}

	public int getSeconds() {
		return seconds;
	}

	public boolean isRunning() {
		return running;
	}

	public void start() {
		running = true;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(LambdaMinigames.getPlugin(), this, 0L, 20L);
	}
}
