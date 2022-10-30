package com.bjrushworth29.lambdaminigames.enums;

public enum GameName {
	SUMO("sumo"),
	DUELS("duels");

	private final String value;

	GameName(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
