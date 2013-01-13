package ru.khl;

import java.util.ArrayList;
import java.util.List;

import ru.khl.adapter.PlayerAdapter;
import ru.khl.core.match.Match;
import ru.khl.core.match.MatchTeamStatistics;
import ru.khl.core.match.MatchesList;
import ru.khl.core.player.Player;
import ru.khl.core.services.MatchService;
import ru.khl.core.team.Team;
import ru.khl.core.team.TeamsMap;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MatchTeamActivity extends FragmentActivity implements
		OnItemClickListener {

	public static final String MATCH_ID = "match_id";
	public static final String TEAM_ID = "team_id";
	Match match;
	Team team;
	MatchTeamStatistics matchTeamStatistics;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_team);
		match = MatchesList.getInstance().obtainMatchById(
				Integer.valueOf(getIntent().getStringExtra(MATCH_ID)));
		team = TeamsMap.getInstance().get(
				Integer.valueOf(getIntent().getStringExtra(TEAM_ID)));
		matchTeamStatistics = MatchTeamStatistics.obtainMatchTeamStatistics(
				match, team);
		TextView teamName = (TextView) findViewById(R.id.teamName);
		teamName.setText(team.getName());
		if (savedInstanceState == null) {
			new LoadMatchTeam().execute();
		} else {
			fillListView();
		}

	}

	public void fillListView() {
		GridView gvGoalkeepers = (GridView) findViewById(R.id.gvGoalkeepers);
		gvGoalkeepers.setOnItemClickListener(this);
		List<Player> goalkeepers = matchTeamStatistics.getGoalkeepers();
		ArrayAdapter<Player> goalkeeperAdapter = new PlayerAdapter(
				MatchTeamActivity.this, R.layout.player, goalkeepers);
		gvGoalkeepers.setAdapter(goalkeeperAdapter);

		GridView gvDefenders = (GridView) findViewById(R.id.gvDefenders);
		gvDefenders.setOnItemClickListener(this);
		List<Player> defenders = matchTeamStatistics.getDefenders();
		ArrayAdapter<Player> defenderAdapter = new PlayerAdapter(
				MatchTeamActivity.this, R.layout.player, defenders);
		gvDefenders.setAdapter(defenderAdapter);

		GridView gvForwards = (GridView) findViewById(R.id.gvForwards);
		gvForwards.setOnItemClickListener(this);
		List<Player> forwards = matchTeamStatistics.getForwards();
		ArrayAdapter<Player> forwardAdapter = new PlayerAdapter(
				MatchTeamActivity.this, R.layout.player, forwards);
		gvForwards.setAdapter(forwardAdapter);
	}

	private class LoadMatchTeam extends AsyncTask<String, Void, List<String>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(MatchTeamActivity.this, "Working...",
					"request to server", true, false);
		}

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			MatchService.getInstance().loadMatchTeamPlayers(team, match);
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
		Player player = ((Player) object);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		PlayerFragment newFragment = PlayerFragment.newInstance(player.getId());
		newFragment.show(ft, "dialog");
	}
}
