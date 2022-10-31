package com.bjrushworth29.lambdaminigames.enums;

public enum Item {
	SELECT_GAME("selectGame"),
	LEAVE_QUEUE("leaveQueue"),
	DIAMOND_SWORD("diamondSword"),
	INFINITY_BOW("infinityBow"),
	INSTANT_SOUP("instantSoup");

	private final String value;

	Item(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
