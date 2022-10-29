package com.bjrushworth29.lambdaminigames.utils;

import org.bukkit.GameMode;

public final class PlayerConstraints {
	private final boolean pvp;
	private final GameMode gameMode;
	private final boolean canMove;
	private final boolean canInteractBlocks;
	private final boolean canInteractItems;
	private final boolean takesAnyDamage;
	private final boolean takesFallDamage;
	private final boolean hungerEnabled;
	private final boolean canDropItems;

	public PlayerConstraints(
			boolean pvp,
			GameMode gameMode,
			boolean canMove,
			boolean canInteractBlocks,
			boolean canInteractItems,
			boolean takesAnyDamage,
			boolean takesFallDamage,
			boolean hungerEnabled,
			boolean canDropItems
	) {
		this.pvp = pvp;
		this.gameMode = gameMode;
		this.canMove = canMove;
		this.canInteractBlocks = canInteractBlocks;
		this.canInteractItems = canInteractItems;
		this.takesAnyDamage = takesAnyDamage;
		this.takesFallDamage = takesFallDamage;
		this.hungerEnabled = hungerEnabled;
		this.canDropItems = canDropItems;
	}

	public boolean pvp() {
		return pvp;
	}

	public GameMode gameMode() {
		return gameMode;
	}

	public boolean canMove() {
		return canMove;
	}

	public boolean canInteractBlocks() {
		return canInteractBlocks;
	}

	public boolean canInteractItems() {
		return canInteractItems;
	}

	public boolean takesAnyDamage() {
		return takesAnyDamage;
	}

	public boolean takesFallDamage() {
		return takesFallDamage;
	}

	public boolean hungerEnabled() {
		return hungerEnabled;
	}

	public boolean canDropItems() {
		return canDropItems;
	}
}
