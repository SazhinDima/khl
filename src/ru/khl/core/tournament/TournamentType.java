package ru.khl.core.tournament;


public enum TournamentType {

	REGULAR("regular"), PLAYOFF("playoff");

	private String type;

	private TournamentType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	public static TournamentType obtainTournamentType(String type) {
		TournamentType[] values = TournamentType.values();
		for (TournamentType eachValue : values) {
			if (eachValue.toString().equals(type)) {
				return eachValue;
			}
		}
		return null;
	}

}
