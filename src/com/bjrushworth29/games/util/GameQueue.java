package com.bjrushworth29.games.util;

import com.bjrushworth29.utils.Countdown;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class GameQueue {
	private final ArrayList<Player> players;

	private Countdown countdown;

	public GameQueue(int queueTime) {
		this.players = new ArrayList<>();

		this.setCountdown(new Countdown(
				queueTime,
				(timer) -> {
					int seconds = timer.getSeconds();

					if (seconds % 5 == 0) {
						for (Player player : players) {
							player.sendMessage(String.format("Game starting in %d seconds", seconds));
						}
					}
				},
				() -> {}
		));
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Countdown getCountdown() {
		return countdown;
	}

	public void setCountdown(Countdown countdown) {
		this.countdown = countdown;
	}
}
