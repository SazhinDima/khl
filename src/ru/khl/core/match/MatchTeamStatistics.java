package ru.khl.core.match;

import java.util.ArrayList;
import java.util.List;

import ru.khl.core.player.Player;
import ru.khl.core.player.Position;
import ru.khl.core.team.Team;

public class MatchTeamStatistics {

	private Team team;

	private Integer count;

	private final List<Goal> goals = new ArrayList<Goal>();

	private final List<Penalty> penalties = new ArrayList<Penalty>();

	private final List<Player> players = new ArrayList<Player>();

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void addGoal(Goal goal) {
		goals.add(goal);
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public Goal createGoal() {
		Goal goal = new Goal();
		addGoal(goal);
		return goal;
	}

	public void addPenalty(Penalty penalty) {
		penalties.add(penalty);
	}

	public List<Penalty> getPenalties() {
		return penalties;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Player> getGoalkeepers() {
		List<Player> goaltenders = new ArrayList<Player>();
		for (Player player : players) {
			if (player.getPosition().equals(Position.GOALTENDER)) {
				goaltenders.add(player);
			}
		}
		return goaltenders;
	}

	public List<Player> getDefenders() {
		List<Player> goaltenders = new ArrayList<Player>();
		for (Player player : players) {
			if (player.getPosition().equals(Position.DEFENSE)) {
				goaltenders.add(player);
			}
		}
		return goaltenders;
	}

	public List<Player> getForwards() {
		List<Player> goaltenders = new ArrayList<Player>();
		for (Player player : players) {
			if (player.getPosition().equals(Position.FORWARD)) {
				goaltenders.add(player);
			}
		}
		return goaltenders;
	}

	public Penalty createPenalty() {
		Penalty penalty = new Penalty();
		addPenalty(penalty);
		return penalty;
	}

	public String getSafetyCount() {
		if (getCount() == null) {
			return "";
		}
		return Integer.toString(getCount());
	}

	public static MatchTeamStatistics obtainMatchTeamStatistics(Match match,
			Team team) {
		MatchTeamStatistics home = match.getHome();
		MatchTeamStatistics away = match.getAway();
		if (home.getTeam().equals(team)) {
			return home;
		}
		return away;
	}
}
