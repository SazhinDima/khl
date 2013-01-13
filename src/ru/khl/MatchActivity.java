package ru.khl;

import java.util.ArrayList;
import java.util.List;

import ru.khl.adapter.GoalAdapter;
import ru.khl.adapter.HeaderSectionedAdapter;
import ru.khl.adapter.PenaltyAdapter;
import ru.khl.core.match.Goal;
import ru.khl.core.match.Match;
import ru.khl.core.match.MatchesList;
import ru.khl.core.match.Penalty;
import ru.khl.core.player.Player;
import ru.khl.core.services.MatchService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MatchActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener {

	public static final String MATCH_ID = "match_id";
	Match match;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		match = MatchesList.getInstance().obtainMatchById(
				Integer.valueOf(getIntent().getStringExtra(MATCH_ID)));
		TextView homeTeamName = (TextView) findViewById(R.id.homeTeamName);
		homeTeamName.setText(match.getHome().getTeam().getName());
		homeTeamName.setOnClickListener(this);
		TextView awayTeamName = (TextView) findViewById(R.id.awayTeamName);
		awayTeamName.setText(match.getAway().getTeam().getName());
		awayTeamName.setOnClickListener(this);
		if (savedInstanceState == null) {
			new LoadMatch().execute();
		} else {
			fillListView();
		}

	}

	public void fillListView() {
		ListView lvGoals = (ListView) findViewById(R.id.lvGoals);
		lvGoals.setOnItemClickListener(this);
		ListView lvPenalties = (ListView) findViewById(R.id.lvPenalties);
		lvPenalties.setOnItemClickListener(this);
		List<Goal> goals = new ArrayList<Goal>() {
			{
				addAll(match.getHome().getGoals());
				addAll(match.getAway().getGoals());

			}
		};
		Goal.sortByTime(goals);
		ArrayAdapter<Goal> adapterGoal = new GoalAdapter(MatchActivity.this,
				R.layout.goal, goals, match.getHome().getGoals(), match
						.getAway().getGoals());
		HeaderSectionedAdapter seactionedAdapterGoal = new HeaderSectionedAdapter(
				getLayoutInflater());
		seactionedAdapterGoal.addSection("голы", adapterGoal);
		lvGoals.setAdapter(seactionedAdapterGoal);

		List<Penalty> penalties = new ArrayList<Penalty>() {
			{
				addAll(match.getHome().getPenalties());
				addAll(match.getAway().getPenalties());

			}
		};
		Penalty.sortByTime(penalties);
		ArrayAdapter<Penalty> adapterPenalty = new PenaltyAdapter(
				MatchActivity.this, R.layout.penalty, penalties, match
						.getHome().getPenalties(), match.getAway()
						.getPenalties());
		HeaderSectionedAdapter seactionedAdapterPenalty = new HeaderSectionedAdapter(
				getLayoutInflater());
		seactionedAdapterPenalty.addSection("штрафы", adapterPenalty);
		lvPenalties.setAdapter(seactionedAdapterPenalty);
	}

	private class LoadMatch extends AsyncTask<String, Void, List<String>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(MatchActivity.this, "Working...",
					"request to server", true, false);
		}

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			MatchService.getInstance().loadGoals(match);
			MatchService.getInstance().loadPenalties(match);
			return output;
		}

		@Override
		protected void onPostExecute(List<String> output) {
			pd.dismiss();
			fillListView();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Object object = parent.getAdapter().getItem(position);
		Player player = null;
		if (object instanceof Goal) {
			player = ((Goal) object).getPlayer();
		} else if (object instanceof Penalty) {
			player = ((Penalty) object).getPlayer();
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		PlayerFragment newFragment = PlayerFragment.newInstance(player.getId());
		newFragment.show(ft, "dialog");
	}

	public void onClick(View v) {
		Intent intent = new Intent(this, MatchTeamActivity.class);
		switch (v.getId()) {
		case R.id.homeTeamName:
			intent.putExtra(MatchTeamActivity.MATCH_ID, match.getId()
					.toString());
			intent.putExtra(MatchTeamActivity.TEAM_ID, match.getHome()
					.getTeam().getId().toString());
			break;
		case R.id.awayTeamName:
			intent.putExtra(MatchTeamActivity.MATCH_ID, match.getId()
					.toString());
			intent.putExtra(MatchTeamActivity.TEAM_ID, match.getAway()
					.getTeam().getId().toString());
			break;
		}
		startActivity(intent);

	}
}
