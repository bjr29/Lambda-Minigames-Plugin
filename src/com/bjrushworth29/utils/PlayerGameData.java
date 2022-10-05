package com.bjrushworth29.utils;

import org.bukkit.Location;

public class PlayerGameData {
	private boolean spawnProtection = false;
	private boolean inGame = true;
	private boolean isSpectator = false;
	private int lives = 0;
	private Countdown countdown;
	private Location spawnPosition;

	public boolean hasSpawnProtection() {
		return spawnProtection;
	}

	public void giveSpawnProtection(int duration) {
		if (duration == 0) {
			return;
		}

		spawnProtection = true;

		countdown = new Countdown(
				duration,
				null,
				() -> spawnProtection = false
		);

		countdown.start();
	}

	public void removeSpawnProtection() {
		spawnProtection = false;
		countdown = null;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;

		if (lives == 0) {
			setSpectator(true);
		}
	}

	public void decrementLives() {
		setLives(getLives() - 1);
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public boolean isSpectator() {
		return isSpectator;
	}

	public void setSpectator(boolean spectator) {
		isSpectator = spectator;
	}

	public void setSpawnLocation(Location spawnLocation) {
		spawnPosition = spawnLocation;
	}

	public Location getSpawnLocation() {
		return spawnPosition;
	}
}
