package ru.khl.core.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.htmlcleaner.TagNode;

import ru.khl.core.match.Goal;
import ru.khl.core.match.Match;
import ru.khl.core.match.MatchTeamStatistics;
import ru.khl.core.match.MatchesList;
import ru.khl.core.match.Penalty;
import ru.khl.core.player.Player;
import ru.khl.core.player.PlayersMap;
import ru.khl.core.team.Team;
import ru.khl.core.team.TeamsMap;
import ru.khl.core.tournament.Tournament;
import ru.khl.core.tournament.TournamentType;
import ru.khl.core.tournament.TournamentsList;

public class MatchService {

	private static MatchService instance;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat bornFormatter = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat tournamentFormatter = new SimpleDateFormat("yyyy-MM-dd");

	public static final String MATCHES_URL_PATTERN = "http://khlapp.khl.ru/games.php?tournament=current&month=%d-%02d";
	public static final String GOALS_URL_PATTERN = "http://khlapp.khl.ru/protocolGoals.php?game=%s";
	public static final String PENALTIES_URL_PATTERN = "http://khlapp.khl.ru/protocolPenalties.php?game=%s";
	public static final String TABLE_URL_PATTERN = "http://khlapp.khl.ru/tables.php?tournamentId=%s";
	public static final String PLAYER_URL_PATTERN = "http://en.khl.ru/players/%s/";
	public static final String MATCH_TEAM_URL_PATTERN = "http://khlapp.khl.ru/players.php?team=%d&game=%d";
	public static final String TOURNAMENTS_PATTERN = "http://khlapp.khl.ru/tournaments.php";

	public static synchronized MatchService getInstance() {
		if (instance == null) {
			instance = new MatchService();
		}
		return instance;
	}

	public void loadMatches(int year, int month) {
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(MATCHES_URL_PATTERN, year, month + 1));
		List<TagNode> gamesNodes = rootNode.getElementListByName("Game", true);
		for (TagNode gameNode : gamesNodes) {
			Match match = MatchesList.getInstance().createMatch();
			loadMatch(match, gameNode);
		}
	}

	public void loadMatch(Match match, TagNode gameNode) {
		TagNode numberNode = gameNode.findElementByName("Number", false);
		String number = numberNode.getText().toString();
		match.setNumber(Integer.parseInt(number));

		TagNode idNode = gameNode.findElementByName("Id", false);
		String id = idNode.getText().toString();
		match.setId(Integer.parseInt(id));

		TagNode dateNode = gameNode.findElementByName("Date", false);
		String date = dateNode.getText().toString();
		try {
			match.setDate(formatter.parse(date));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		loadMatchTeam(match.getHome(),
				gameNode.findElementByName("FirstTeam", false),
				gameNode.findElementByName("FirstTeamId", true));
		loadMatchTeam(match.getAway(),
				gameNode.findElementByName("SecondTeam", false),
				gameNode.findElementByName("SecondTeamId", true));
	}

	public void loadMatchTeam(MatchTeamStatistics matchTeamStatistics,
			TagNode teamNode, TagNode teamIdNode) {
		String teamName = teamNode.findElementByName("Name", false).getText()
				.toString();
		String teamCount = teamNode.findElementByName("Scores", false)
				.getText().toString();
		String teamId = teamIdNode.getText().toString();
		Team teamHome = TeamsMap.getInstance().createTeam(
				Integer.valueOf(teamId), teamName);
		matchTeamStatistics.setTeam(teamHome);
		if (!"".equals(teamCount)) {
			matchTeamStatistics.setCount(Integer.valueOf(teamCount));
		}
	}

	public void loadPenalties(Match match) {
		MatchTeamStatistics home = match.getHome();
		MatchTeamStatistics away = match.getAway();
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(PENALTIES_URL_PATTERN, match.getId()));
		List<TagNode> penaltiesNodes = rootNode.getElementListByName("Penalty",
				true);
		for (TagNode penaltyNode : penaltiesNodes) {
			String teamIdStr = penaltyNode.findElementByName("TeamId", false)
					.getText().toString();
			Integer teamId = Integer.valueOf(teamIdStr);
			final Penalty penalty;
			if (teamId == home.getTeam().getId()) {
				penalty = home.createPenalty();
			} else {
				penalty = away.createPenalty();
			}

			parseMatchTime(penaltyNode.findElementByName("BeginTime", false),
					new ITimeExecutor() {
						public void execute(Integer minute, Integer second) {
							penalty.setMinute(minute);
							penalty.setSecond(second);
						}
					});

			Player player = createPlayerFromNodes(
					penaltyNode.findElementByName("PlayerId", false),
					penaltyNode.findElementByName("PlayerName", false));
			penalty.setPlayer(player);
		}
	}

	public void loadGoals(Match match) {
		MatchTeamStatistics home = match.getHome();
		MatchTeamStatistics away = match.getAway();
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(GOALS_URL_PATTERN, match.getId()));
		List<TagNode> goalsNodes = rootNode.getElementListByName("Goal", true);
		int homeCount = 0;
		int awayCount = 0;
		for (TagNode goalNode : goalsNodes) {
			String cont = goalNode.findElementByName("Score", false).getText()
					.toString();
			int delimiterIndex = cont.indexOf(":");
			int curHomeCount = Integer.parseInt(cont.substring(0,
					delimiterIndex));
			int curAwayCount = Integer.parseInt(cont.substring(
					delimiterIndex + 1, cont.length()));
			Player player = createPlayerFromNodes(
					goalNode.findElementByName("ScorerId", false),
					goalNode.findElementByName("ScorerName", false));
			Player assistant1 = createPlayerFromNodes(
					goalNode.findElementByName("Assist1Id", false),
					goalNode.findElementByName("Assist1Name", false));
			Player assistant2 = createPlayerFromNodes(
					goalNode.findElementByName("Assist2Id", false),
					goalNode.findElementByName("Assist2Name", false));
			final Goal goal;
			if (curHomeCount == homeCount) {
				awayCount = curAwayCount;
				goal = away.createGoal();
			} else {
				homeCount = curHomeCount;
				goal = home.createGoal();
			}
			goal.setPlayer(player);
			if (assistant1 != null) {
				goal.setAssistant1(assistant1);
			}
			if (assistant2 != null) {
				goal.setAssistant2(assistant2);
			}

			parseMatchTime(goalNode.findElementByName("Time", false),
					new ITimeExecutor() {
						public void execute(Integer minute, Integer second) {
							goal.setMinute(minute);
							goal.setSecond(second);
						}
					});
		}
	}

	protected void parseMatchTime(TagNode timeNode, ITimeExecutor timeExecutor) {
		String time = timeNode.getText().toString();
		int timeDelimiterIndex = time.indexOf(":");
		timeExecutor.execute(
				Integer.valueOf(time.substring(0, timeDelimiterIndex)),
				Integer.valueOf(time.substring(timeDelimiterIndex + 1,
						time.length())));
	}

	protected Player createPlayerFromNodes(TagNode playerIdNode,
			TagNode playerNode) {
		String playerId = playerIdNode.getText().toString();
		String playerStr = playerNode.getText().toString();
		if ("".equals(playerId)) {
			return null;
		}
		return createPlayer(Integer.valueOf(playerId), playerStr);
	}

	protected Player createPlayer(Integer id, String playerStr) {
		int index1 = playerStr.indexOf(".");
		int index2 = playerStr.indexOf("(");
		if (index2 == -1) {
			index2 = playerStr.length();
		}
		Integer number = Integer.valueOf(playerStr.substring(0, index1));
		String name = playerStr.substring(index1 + 1, index2);
		return PlayersMap.getInstance().createPlayer(id, name, number);
	}

	protected interface ITimeExecutor {
		void execute(Integer minute, Integer second);
	}

	public void loadTable(int tournamentId) {
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(TABLE_URL_PATTERN, tournamentId));
		List<TagNode> teamsNodes = rootNode.getElementListByName("Team", true);
		for (TagNode teamNode : teamsNodes) {
			String teamId = teamNode.findElementByName("Id", false).getText()
					.toString();
			String teamName = teamNode.findElementByName("Name", false)
					.getText().toString();
			Team team = TeamsMap.getInstance().createTeam(
					Integer.valueOf(teamId), teamName);
			loadTeamStatistics(team, teamNode);
		}
	}

	protected void loadTeamStatistics(Team team, TagNode teamNode) {
		String gameCountStr = teamNode.findElementByName("GameCount", false)
				.getText().toString();
		team.setGameCount(Integer.valueOf(gameCountStr));

		String winsStr = teamNode.findElementByName("Wins", false).getText()
				.toString();
		team.setWins(Integer.valueOf(winsStr));

		String winsOTStr = teamNode.findElementByName("WinsOT", false)
				.getText().toString();
		team.setWinsOT(Integer.valueOf(winsOTStr));

		String winsSOStr = teamNode.findElementByName("WinsSO", false)
				.getText().toString();
		team.setWinsSO(Integer.valueOf(winsSOStr));

		String losesSOStr = teamNode.findElementByName("LosesSO", false)
				.getText().toString();
		team.setLosesSO(Integer.valueOf(losesSOStr));

		String losesOTStr = teamNode.findElementByName("LosesOT", false)
				.getText().toString();
		team.setLosesOT(Integer.valueOf(losesOTStr));

		String losesStr = teamNode.findElementByName("Loses", false).getText()
				.toString();
		team.setLoses(Integer.valueOf(losesStr));

		String scoresStr = teamNode.findElementByName("Scores", false)
				.getText().toString();
		team.setScores(Integer.valueOf(scoresStr));

		String goalsStr = teamNode.findElementByName("Goals", false).getText()
				.toString();
		int delimiterIndex = goalsStr.indexOf("-");
		team.setScoringGoals(Integer.valueOf(goalsStr.substring(0,
				delimiterIndex)));
		team.setMissedGoal(Integer.valueOf(goalsStr.substring(
				delimiterIndex + 1, goalsStr.length())));
	}

	public void loadPlayer(int playerId) {
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(PLAYER_URL_PATTERN, playerId));
		Player player = PlayersMap.getInstance().obtainPlayerById(playerId);
		TagNode playerNode = rootNode.findElementByAttValue("class",
				"borderdiv", true, false);

		TagNode imageNode = playerNode.findElementByName("div", true);
		String imageStyle = imageNode.getAttributeByName("style");
		int firstDelimiter = imageStyle.indexOf("(");
		int secondDelimiter = imageStyle.indexOf(")");
		player.setImageURL(imageStyle.substring(firstDelimiter + 1,
				secondDelimiter));

		List<TagNode> playerStats = playerNode.getElementListByName("li", true);
		for (TagNode playerStat : playerStats) {
			StringBuffer text = playerStat.getText();
			TagNode bold = playerStat.findElementByName("b", false);
			if (text.indexOf("#:") == 0) {
				player.setNumber(Integer.valueOf(bold.getText().toString()));
			} else if (text.indexOf("Position:") == 0) {
				player.setPosition(bold.getText().toString());
			} else if (text.indexOf("Height:") == 0) {
				player.setHeight(Integer.valueOf(bold.getText().toString()));
			} else if (text.indexOf("Weight:") == 0) {
				player.setWeight(Integer.valueOf(bold.getText().toString()));
			} else if (text.indexOf("Born:") == 0) {
				try {
					player.setBorn(bornFormatter.parse(bold.getText()
							.toString()));
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public void loadMatchTeamPlayers(Team team, Match match) {
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(MATCH_TEAM_URL_PATTERN, team.getId(),
						match.getId()));
		MatchTeamStatistics matchTeamStatistics = MatchTeamStatistics
				.obtainMatchTeamStatistics(match, team);
		List<TagNode> playerNodes = rootNode.getElementListByName("Player",
				true);
		for (TagNode playerNode : playerNodes) {
			TagNode idNode = playerNode.findElementByName("Id", false);
			TagNode nameNode = playerNode.findElementByName("Name", false);
			TagNode numberNode = playerNode.findElementByName("Number", false);
			TagNode positionNode = playerNode.findElementByName("Type", false);
			Player player = PlayersMap.getInstance().createPlayer(
					Integer.valueOf(idNode.getText().toString()),
					nameNode.getText().toString(),
					Integer.valueOf(numberNode.getText().toString()));
			player.setPosition(positionNode.getText().toString());
			matchTeamStatistics.addPlayer(player);
		}

	}

	public void loadTournaments() {
		TagNode rootNode = LoadPageService.getInstance().loadPage(
				String.format(TOURNAMENTS_PATTERN));

		List<TagNode> tournamentNodes = rootNode.getElementListByName(
				"Tournament", true);
		for (TagNode tournamentNode : tournamentNodes) {
			TagNode idNode = tournamentNode.findElementByName("Id", false);
			TagNode nameNode = tournamentNode.findElementByName("Name", false);
			TagNode startDateNode = tournamentNode.findElementByName(
					"startDate", false);
			TagNode endDateNode = tournamentNode.findElementByName("endDate",
					false);
			TagNode typeNode = tournamentNode.findElementByName("Type", false);
			TagNode currentNode = tournamentNode.findElementByName("Current",
					false);
			try {
				Tournament tournament = TournamentsList.getInstance()
						.createTournament(
								Integer.valueOf(idNode.getText().toString()),
								nameNode.getText().toString(),
								tournamentFormatter.parse(startDateNode
										.getText().toString()),
								tournamentFormatter.parse(endDateNode.getText()
										.toString()),
								TournamentType.obtainTournamentType(typeNode
										.getText().toString()));
				if (currentNode.getText().toString().equals("1")) {
					TournamentsList.getInstance().setCurrentTournament(
							tournament);
				}
			} catch (NumberFormatException e) {
				throw new RuntimeException(e);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
