package com.bjrushworth29.enums;

public enum DefaultWorld {
	HUB("world_hub"),
	SUMO("world_sumo");

	private final String value;

	DefaultWorld(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
