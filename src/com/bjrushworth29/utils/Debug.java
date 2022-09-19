package com.bjrushworth29.utils;

import com.bjrushworth29.LambdaMinigames;

import java.util.logging.Logger;

public class Debug {
	private static final Logger LOGGER;

	static {
		LOGGER = LambdaMinigames.getPlugin().getLogger();
	}

	public static void info(String message, Object... args) {
		LOGGER.info(String.format(message, args));
	}

	public static void warn(String message, Object... args) {
		LOGGER.warning(String.format(message, args));
	}
}
