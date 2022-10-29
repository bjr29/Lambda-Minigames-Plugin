package com.bjrushworth29.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerGameData {
	private boolean spawnProtection = false;
	private boolean inGame = true;
	private boolean isSpectator = false;
	private int lives = 0;
	private Countdown countdown;
	private Location spawnPosition;
	private boolean applyKnockback = false;
	private Player previousAttacker;

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
		Debug.info("Spawn location: %s", spawnLocation);
		spawnPosition = spawnLocation;
	}

	public Location getSpawnLocation() {
		return spawnPosition;
	}

	public boolean shouldApplyKnockback() {
		return applyKnockback;
	}

	public void setApplyKnockback(boolean applyKnockback) {
		this.applyKnockback = applyKnockback;
	}

	public Player getPreviousAttacker() {
		return previousAttacker;
	}

	public void setPreviousAttacker(Player previousAttacker) {
		this.previousAttacker = previousAttacker;
	}
}
