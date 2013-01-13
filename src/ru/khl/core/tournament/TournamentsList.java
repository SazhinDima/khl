package ru.khl.core.tournament;

import java.util.AbstractList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TournamentsList extends AbstractList<Tournament> {

	List<Tournament> tournaments = new CopyOnWriteArrayList<Tournament>();

	private Tournament currentTournament;

	private static TournamentsList instance;

	public static synchronized TournamentsList getInstance() {
		if (instance == null) {
			instance = new TournamentsList();
		}
		return instance;
	}

	public Tournament createTournament(Integer id, String name, Date startDate,
			Date endDate, TournamentType type) {
		Tournament tournament = new Tournament();
		tournament.setId(id);
		tournament.setName(name);
		tournament.setStartDate(startDate);
		tournament.setEndDate(endDate);
		tournament.setType(type);
		add(tournament);
		return tournament;
	}

	@Override
	public Tournament get(int i) {
		return tournaments.get(i);
	}

	@Override
	public int size() {
		return tournaments.size();
	}

	@Override
	public boolean add(Tournament object) {
		return tournaments.add(object);
	}

	public Tournament getCurrentTournament() {
		return currentTournament;
	}

	public void setCurrentTournament(Tournament currentTournament) {
		this.currentTournament = currentTournament;
	}

}
