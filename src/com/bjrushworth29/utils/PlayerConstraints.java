package com.bjrushworth29.utils;

import org.bukkit.GameMode;

public record PlayerConstraints(
		boolean pvp,
		GameMode gameMode,
		boolean canMove,
		boolean canInteractBlocks,
		boolean canInteractItems,
		boolean takesAnyDamage,
		boolean takesFallDamage,
		boolean hungerEnabled
) {}
