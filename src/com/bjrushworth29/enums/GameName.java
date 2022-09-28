package com.bjrushworth29.enums;

public enum GameName {
	SUMO("sumo");

	private final String value;

	GameName(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
