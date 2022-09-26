package com.bjrushworth29.enums;

public enum EInventoryLoadout {
	HUB("hub"),
	HUB_QUEUED("hubQueued");

	private final String value;

	EInventoryLoadout(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
