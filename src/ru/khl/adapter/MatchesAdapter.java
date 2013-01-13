package ru.khl.adapter;

import ru.khl.MatchesFragment;
import ru.khl.core.tournament.TournamentsList;
import ru.khl.util.CalendarUtil;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MatchesAdapter extends FragmentPagerAdapter {

	Context ctxt = null;

	public MatchesAdapter(Context ctxt, FragmentManager mgr) {
		super(mgr);
		this.ctxt = ctxt;
	}

	@Override
	public int getCount() {
		return CalendarUtil
				.getInstance()
				.getMonths(
						TournamentsList.getInstance().getCurrentTournament()
								.getStartDate(),
						TournamentsList.getInstance().getCurrentTournament()
								.getEndDate()).size();
	}

	@Override
	public Fragment getItem(int position) {
		return MatchesFragment.newInstance(
				position,
				CalendarUtil
						.getInstance()
						.getMonths(
								TournamentsList.getInstance()
										.getCurrentTournament().getStartDate(),
								TournamentsList.getInstance()
										.getCurrentTournament().getEndDate())
						.get(position).getMonth(),
				CalendarUtil
						.getInstance()
						.getMonths(
								TournamentsList.getInstance()
										.getCurrentTournament().getStartDate(),
								TournamentsList.getInstance()
										.getCurrentTournament().getEndDate())
						.get(position).getYear());
	}

	@Override
	public String getPageTitle(int position) {
		return CalendarUtil
				.getInstance()
				.getMonths(
						TournamentsList.getInstance().getCurrentTournament()
								.getStartDate(),
						TournamentsList.getInstance().getCurrentTournament()
								.getEndDate()).get(position).getMonthName();
	}
}