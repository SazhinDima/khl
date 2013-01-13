package ru.khl;

import java.util.Calendar;
import java.util.List;

import ru.khl.adapter.MatchesAdapter;
import ru.khl.core.tournament.TournamentsList;
import ru.khl.util.CalendarUtil;
import ru.khl.util.CalendarUtil.Month;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MatchesActivity extends FragmentActivity {

	private static final String CURRENT_ITEM = "currentItem";

	ViewPager pager;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager);

		pager = (ViewPager) findViewById(R.id.pager);
		final FragmentPagerAdapter pagerAdapter = buildAdapter();
		new Handler().post(new Runnable() {
			public void run() {
				if (savedInstanceState != null) {
					pager.setCurrentItem(savedInstanceState
							.getInt(CURRENT_ITEM));
				} else {
					Calendar calendar = Calendar.getInstance();
					int m = calendar.get(Calendar.MONTH);
					int y = calendar.get(Calendar.YEAR);
					List<Month> months = CalendarUtil.getInstance().getMonths(
							TournamentsList.getInstance()
									.getCurrentTournament().getStartDate(),
							TournamentsList.getInstance()
									.getCurrentTournament().getEndDate());
					int i = 0;
					for (Month month : months) {
						if ((m == month.getMonth()) && (y == month.getYear())) {
							break;
						}
						i++;
					}
					pager.setCurrentItem(i);
				}
			}
		});
		pager.setAdapter(pagerAdapter);
	}

	private FragmentPagerAdapter buildAdapter() {
		return (new MatchesAdapter(this, getSupportFragmentManager()));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CURRENT_ITEM, pager.getCurrentItem());
	}
}
