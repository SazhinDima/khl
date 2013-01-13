package ru.khl.core.team;

public class Team {

	private Integer id;

	private String name;

	private Integer gameCount;

	private Integer wins;

	private Integer winsOT;

	private Integer winsSO;

	private Integer losesSO;

	private Integer losesOT;

	private Integer loses;

	private Integer scoringGoals;

	private Integer missedGoal;

	private Integer scores;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGameCount() {
		return gameCount;
	}

	public void setGameCount(Integer gameCount) {
		this.gameCount = gameCount;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getWinsOT() {
		return winsOT;
	}

	public void setWinsOT(Integer winsOT) {
		this.winsOT = winsOT;
	}

	public Integer getWinsSO() {
		return winsSO;
	}

	public void setWinsSO(Integer winsSO) {
		this.winsSO = winsSO;
	}

	public Integer getLosesSO() {
		return losesSO;
	}

	public void setLosesSO(Integer losesSO) {
		this.losesSO = losesSO;
	}

	public Integer getLosesOT() {
		return losesOT;
	}

	public void setLosesOT(Integer losesOT) {
		this.losesOT = losesOT;
	}

	public Integer getLoses() {
		return loses;
	}

	public void setLoses(Integer loses) {
		this.loses = loses;
	}

	public Integer getScoringGoals() {
		return scoringGoals;
	}

	public void setScoringGoals(Integer scoringGoals) {
		this.scoringGoals = scoringGoals;
	}

	public Integer getMissedGoal() {
		return missedGoal;
	}

	public void setMissedGoal(Integer missedGoal) {
		this.missedGoal = missedGoal;
	}

	public Integer getScores() {
		return scores;
	}

	public void setScores(Integer scores) {
		this.scores = scores;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Team) {
			return (getId() == ((Team) o).getId());
		}
		return false;
	}

}
