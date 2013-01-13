package ru.khl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.khl.adapter.HeaderSectionedAdapter;
import ru.khl.adapter.MatchAdapter;
import ru.khl.core.match.Match;
import ru.khl.core.match.MatchesList;
import ru.khl.core.services.MatchService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.commonsware.android.listview.SectionedAdapter;

public class MatchesFragment extends Fragment implements OnItemClickListener {

	private static final String KEY_POSITION = "position";
	private static final String MONTH = "month";
	private static final String YEAR = "year";
	private static final String MATCHES_MAP = "matchesMap";

	SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

	SectionedAdapter adapter;
	Map<Date, List<Match>> matchesMap = null;

	public static MatchesFragment newInstance(int position, int month, int year) {
		MatchesFragment frag = new MatchesFragment();
		Bundle args = new Bundle();

		args.putInt(KEY_POSITION, position);
		args.putInt(MONTH, month);
		args.putInt(YEAR, year);
		frag.setArguments(args);

		return (frag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.activity_matches, container,
				false);
		return (result);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(MATCHES_MAP, (Serializable) matchesMap);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			Map<Date, List<Match>> savedMatchesMap = (Map<Date, List<Match>>) savedInstanceState
					.getSerializable(MATCHES_MAP);
			matchesMap = savedMatchesMap;
		}

		int month = getArguments().getInt(MONTH);
		int year = getArguments().getInt(YEAR);

		adapter = new HeaderSectionedAdapter(getActivity().getLayoutInflater());
		if (matchesMap == null) {
			matchesMap = new LinkedHashMap<Date, List<Match>>();
			new LoadMatches().execute(Integer.toString(month),
					Integer.toString(year));
		} else {
			fillListView();
		}

	}

	public void fillListView() {
		if (getView() != null) {
			int selection = 0;
			Set<Date> matchDays = matchesMap.keySet();
			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			int currentMonth = calendar.get(Calendar.MONTH);
			int month = getArguments().getInt(MONTH);
			for (Date matchDay : matchDays) {
				if ((currentMonth == month) && (currentDate.after(matchDay))) {
					selection = adapter.getCount();
				}
				adapter.addSection(dt.format(matchDay),
						new MatchAdapter(getActivity(), R.layout.match,
								matchesMap.get(matchDay)));
			}
			ListView listview = (ListView) getView().findViewById(R.id.lvTypes);
			listview.setOnItemClickListener(MatchesFragment.this);
			listview.setAdapter(adapter);
			listview.setSelection(selection);
		}
	}

	private class LoadMatches extends AsyncTask<String, Void, List<String>> {

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			int month = Integer.parseInt(arg[0]);
			int year = Integer.parseInt(arg[1]);
			MatchService.getInstance().loadMatches(year, month);
			adapter.clear();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.YEAR, year);
			int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int i = 1; i < daysInMonth + 1; i++) {
				calendar.set(Calendar.DATE, i);
				List<Match> matches = new ArrayList<Match>();
				Date date = calendar.getTime();
				MatchesList.getInstance().obtainMatchesForDate(date, matches);
				if (!matches.isEmpty()) {
					if (getActivity() != null) {
						matchesMap.put(date, matches);
					}
				}
			}
			return output;
		}

		@Override
		protected void onPostExecute(List<String> output) {
			fillListView();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Match match = (Match) adapter.getItem(position);
		Intent intent = new Intent(getActivity(), MatchActivity.class);
		intent.putExtra(MatchActivity.MATCH_ID, match.getId().toString());
		startActivity(intent);
	}
}
