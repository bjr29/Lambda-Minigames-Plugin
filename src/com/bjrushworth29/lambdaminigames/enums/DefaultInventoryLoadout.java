package com.bjrushworth29.lambdaminigames.enums;

public enum DefaultInventoryLoadout {
	HUB("hub"),
	HUB_QUEUED("hubQueued"),
	DUELS("duels"),
	DUELS_ROD("duelsRod"),
	DUELS_BOW("duelsBow"),
	DUELS_SOUP("duelsSoup");

	private final String value;

	DefaultInventoryLoadout(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
