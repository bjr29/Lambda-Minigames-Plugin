package com.bjrushworth29.enums;

public enum EWorld {
	HUB("hub"),
	TEST_GAME("test_game");

	private final String value;

	EWorld(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
