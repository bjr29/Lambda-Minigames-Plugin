package com.bjrushworth29.enums;

public enum Constraints {
	HUB("hub"),
	WAITING("waiting"),
	SPECTATOR("spectator"),
	PVP_DEFAULT("pvpDefault"),
	PVP_SUMO("pvpSumo");

	private final String value;

	Constraints(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
