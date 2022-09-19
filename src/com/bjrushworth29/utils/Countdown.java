package com.bjrushworth29.utils;

import com.bjrushworth29.LambdaMinigames;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class Countdown implements Runnable {
	private Integer id;

	private final int totalSeconds;
	private int seconds;

	public Consumer<Countdown> tick;
	public Runnable completed;

	public Countdown(int seconds, Consumer<Countdown> tick, Runnable completed) {
		this.totalSeconds = seconds;
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
				Bukkit.getScheduler().cancelTask(id);
			}

			return;
		}

		if (tick != null) {
			tick.accept(this);
		}

		seconds--;
	}

	public int getSeconds() {
		return seconds;
	}

	public boolean isRunning() {
		return Bukkit.getScheduler().isCurrentlyRunning(id);
	}

	public void start() {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(LambdaMinigames.getPlugin(), this, 0L, 20L);
	}

	public void restart() {
		seconds = totalSeconds;
		Bukkit.getScheduler().cancelTask(id);
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}
}
