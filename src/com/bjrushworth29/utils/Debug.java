package com.bjrushworth29.utils;

import com.bjrushworth29.LambdaMinigames;
import com.bjrushworth29.enums.DebugLevel;

import java.util.logging.Logger;

public class Debug {
	public static final boolean USE_DEBUG_COMMANDS = true;

	private static final Logger LOGGER;
	private static final DebugLevel LEVEL = DebugLevel.FULL;

	static {
		LOGGER = LambdaMinigames.getPlugin().getLogger();
	}

	public static void info(String message, Object... args) {
		info(DebugLevel.REGULAR, message, args);
	}

	public static void info(DebugLevel debugLevel, String message, Object... args) {
		switch (LEVEL) {
			case REGULAR:
				if (debugLevel == DebugLevel.REGULAR) {
					break;
				}

			case MIN:
				if (debugLevel == DebugLevel.MIN) {
					break;
				}
				return;

			case WARNING_ONLY:
			case NONE:
				return;
		}

		LOGGER.info(String.format(message, args));
	}

	public static void warn(String message, Object... args) {
		if (LEVEL != DebugLevel.NONE) {
			LOGGER.warning(String.format(message, args));
		}
	}
}
