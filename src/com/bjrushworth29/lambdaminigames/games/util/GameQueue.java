package com.bjrushworth29.lambdaminigames.games.util;

import com.bjrushworth29.lambdaminigames.utils.Countdown;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class GameQueue {
	private final ArrayList<Player> players;
	private final int minPlayers;

	private Countdown countdown;

	public GameQueue(int queueTime, int minPlayers) {
		this.minPlayers = minPlayers;
		this.players = new ArrayList<>();

		this.setCountdown(new Countdown(
				queueTime,
				(timer) -> {
					int seconds = timer.getSeconds();

					if (seconds % 5 == 0) {
						for (Player player : getPlayers()) {
							player.sendMessage(String.format("Game starting in %d seconds", seconds));
						}
					}

					if (players.size() < minPlayers) {
						timer.reset();
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
