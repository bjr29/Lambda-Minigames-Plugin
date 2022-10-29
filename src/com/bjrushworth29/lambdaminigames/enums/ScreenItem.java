package com.bjrushworth29.lambdaminigames.enums;

public enum ScreenItem {
	SUMO_GAME("screenSumoGame");

	private final String value;

	ScreenItem(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
