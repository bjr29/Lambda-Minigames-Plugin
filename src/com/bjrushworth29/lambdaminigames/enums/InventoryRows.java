package com.bjrushworth29.lambdaminigames.enums;

public enum InventoryRows {
	ONE(9),
	TWO(18),
	THREE(27),
	FOUR(36),
	FIVE(45),
	SIX(54);

	private final int value;

	InventoryRows(int i) {
		value = i;
	}

	public int getValue() {
		return value;
	}
}
