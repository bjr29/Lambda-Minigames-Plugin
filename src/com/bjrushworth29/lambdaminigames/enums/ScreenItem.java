package com.bjrushworth29.lambdaminigames.enums;

public enum ScreenItem {
	SUMO_GAME("screenSumoGame"),
	DUELS_GAME("screenDuelsGame"),
	DUELS_ROD("screenDuelsRodGame"),
	DUELS_BOW("screenDuelsBowGame");

	private final String value;

	ScreenItem(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
