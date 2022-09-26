package com.bjrushworth29.enums;

public enum DefaultWorld {
	HUB("hub"),
	TEST_GAME("test_game");

	private final String value;

	DefaultWorld(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
