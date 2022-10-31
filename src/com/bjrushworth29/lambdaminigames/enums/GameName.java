package com.bjrushworth29.lambdaminigames.enums;

public enum GameName {
	SUMO("sumo"),
	DUELS("duels"),
	DUELS_ROD("duelsRod"),
	DUELS_BOW("duelsBow");

	private final String value;

	GameName(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
