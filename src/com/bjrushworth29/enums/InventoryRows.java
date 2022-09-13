package com.bjrushworth29.enums;

public enum InventoryRows {
	One(9),
	Two(18),
	Three(27),
	Four(36),
	Five(45),
	Six(54);

	private final int value;

	InventoryRows(int i) {
		value = i;
	}

	public int getValue() {
		return value;
	}
}
