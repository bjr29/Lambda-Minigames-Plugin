package com.bjrushworth29.lambdaminigames.enums;

public enum DefaultInventoryLoadout {
	HUB("hub"),
	HUB_QUEUED("hubQueued"),
	DUELS("duels");

	private final String value;

	DefaultInventoryLoadout(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
