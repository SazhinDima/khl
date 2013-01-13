package ru.khl.core.player;

import java.util.Arrays;

public enum Position {

	GOALTENDER("goaltender", "Goalkeeper"), DEFENSE("defense", "Defender"), FORWARD(
			"forward", "Forward");

	private String[] positions;

	private Position(String... positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return positions[0];
	}

	public String[] getPositions() {
		return positions;
	}

	public static Position obtainPosition(String position) {
		Position[] values = Position.values();
		for (Position eachValue : values) {
			if (Arrays.asList(eachValue.getPositions()).contains(position)) {
				return eachValue;
			}
		}
		return null;
	}
}
