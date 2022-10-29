package com.bjrushworth29.lambdaminigames.utils;

import com.bjrushworth29.lambdaminigames.LambdaMinigames;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class Countdown implements Runnable {
	private Integer id;

	private final int totalSeconds;
	private int seconds;

	public Consumer<Countdown> tick;
	private Runnable completed;

	public Countdown(int seconds, Consumer<Countdown> tick, Runnable completed) {
		this.totalSeconds = seconds;
		this.seconds = seconds;
		this.tick = tick;
		this.setCompleted(completed);
	}

	@Deprecated
	@Override
	public void run() {
		if (seconds <= 0) {
			getCompleted().run();

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
		try {
			return Bukkit.getScheduler().isCurrentlyRunning(id);

		} catch (Exception ignored) {
			return false;
		}
	}

	public void start() {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(LambdaMinigames.getPlugin(), this, 0L, 20L);
	}

	public void reset() {
		seconds = totalSeconds;
		Bukkit.getScheduler().cancelTask(id);
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}

	public Runnable getCompleted() {
		return completed;
	}

	public void setCompleted(Runnable completed) {
		this.completed = completed;
	}
}
