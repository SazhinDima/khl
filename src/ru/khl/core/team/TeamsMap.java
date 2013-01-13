package ru.khl.core.team;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TeamsMap extends AbstractMap<Integer, Team> {

	Map<Integer, Team> teams = new HashMap<Integer, Team>();

	private static TeamsMap instance;

	public static synchronized TeamsMap getInstance() {
		if (instance == null) {
			instance = new TeamsMap();
		}
		return instance;
	}

	public Team createTeam(Integer id, String name) {
		if (containsKey(id)) {
			return get(id);
		}
		Team team = new Team();
		team.setId(id);
		team.setName(name);
		put(id, team);
		return team;
	}

	@Override
	public Team put(Integer key, Team team) {
		return teams.put(key, team);
	}

	@Override
	public Set<java.util.Map.Entry<Integer, Team>> entrySet() {
		return teams.entrySet();
	}

	public Collection<Team> obtainSortedByScoresTeams() {
		ScoreComparator scoreComparator = new ScoreComparator(teams);
		TreeMap<Integer, Team> sortedMap = new TreeMap<Integer, Team>(
				scoreComparator);
		sortedMap.putAll(teams);
		return sortedMap.values();
	}

	class ScoreComparator implements Comparator<Integer> {

		Map<Integer, Team> teams;

		public ScoreComparator(Map<Integer, Team> teams) {
			this.teams = teams;
		}

		public int compare(Integer a, Integer b) {
			Team teamA = teams.get(a);
			Team teamB = teams.get(b);
			if (teamA.getScores() >= teamB.getScores()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
