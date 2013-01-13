package ru.khl.core.match;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MatchesList extends AbstractList<Match> {

	List<Match> matches = new CopyOnWriteArrayList<Match>();

	SimpleDateFormat dateWithoutTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static MatchesList instance;

	public static synchronized MatchesList getInstance() {
		if (instance == null) {
			instance = new MatchesList();
		}
		return instance;
	}

	public Match createMatch() {
		Match match = new Match();
		add(match);
		return match;
	}

	@Override
	public Match get(int i) {
		return matches.get(i);
	}

	@Override
	public int size() {
		return matches.size();
	}

	@Override
	public boolean add(Match object) {
		return matches.add(object);
	}

	public void obtainMatchesForDate(Date date, List<Match> matches) {
		try {
			Date dateWithoutTime = dateWithoutTimeFormat
					.parse(dateWithoutTimeFormat.format(date));
			for (Iterator<Match> it = this.matches.iterator(); it.hasNext();) {
				Match match = it.next();
				Date matchDateWithoutTime = dateWithoutTimeFormat
						.parse(dateWithoutTimeFormat.format(match.getDate()));
				if (matchDateWithoutTime.equals(dateWithoutTime)) {
					matches.add(match);
				}
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public Match obtainMatchById(Integer id) {
		for (Match match : this.matches) {
			if (id.equals(match.getId())) {
				return match;
			}
		}
		throw new RuntimeException("Cannot find match");
	}

}
