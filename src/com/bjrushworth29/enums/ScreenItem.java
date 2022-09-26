package com.bjrushworth29.enums;

public enum ScreenItem {
	TEST_GAME("screenTestGame"),
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
