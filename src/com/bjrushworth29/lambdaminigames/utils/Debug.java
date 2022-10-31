package com.bjrushworth29.lambdaminigames.utils;

import com.bjrushworth29.lambdaminigames.LambdaMinigames;
import com.bjrushworth29.lambdaminigames.managers.GameManager;
import com.bjrushworth29.lambdaminigames.enums.DebugLevel;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class Debug {
	private static final Logger LOGGER;
	private static DebugLevel debugLevel = DebugLevel.REGULAR;
	private static boolean useDebugCommands = true;

	static {
		LOGGER = LambdaMinigames.getPlugin().getLogger();

		if (Bukkit.getIp().equals("")) {
			Debug.info("Server is running on localhost, using extra debug messages and commands");

			debugLevel = DebugLevel.FULL;
			useDebugCommands = true;

			GameManager.applyDebugSettings();
		}
	}

	public static void info(String message, Object... args) {
		info(DebugLevel.REGULAR, message, args);
	}

	public static void info(DebugLevel debugLevel, String message, Object... args) {
		switch (Debug.debugLevel) {
			case REGULAR:
			case MIN:
				if (debugLevel == DebugLevel.MIN || debugLevel == DebugLevel.REGULAR) {
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
		if (debugLevel != DebugLevel.NONE) {
			LOGGER.warning(String.format(message, args));
		}
	}

	public static boolean isUsingDebugCommands() {
		return useDebugCommands;
	}
}
