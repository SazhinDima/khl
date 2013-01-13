package ru.khl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.khl.core.services.MatchService;
import ru.khl.core.team.Team;
import ru.khl.core.team.TeamsMap;
import ru.khl.core.tournament.TournamentsList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		if (savedInstanceState == null) {
			new LoadTable().execute();
		} else {
			fillTableView();
		}

	}

	public void fillTableView() {
		int bgColor = Color.rgb(227, 227, 227);
		TableLayout table = (TableLayout) findViewById(R.id.table);
		table.setVisibility(View.VISIBLE);
		TableLayout tableTeamName = (TableLayout) findViewById(R.id.table_team_name);
		tableTeamName.setVisibility(View.VISIBLE);
		Collection<Team> teams = TeamsMap.getInstance()
				.obtainSortedByScoresTeams();
		boolean f = true;
		for (Team team : teams) {
			TableRow rowTeamName = (TableRow) getLayoutInflater().inflate(
					R.layout.table_row_team_name, null);
			if (f) {
				rowTeamName.setBackgroundColor(bgColor);
			}
			((TextView) rowTeamName.findViewById(R.id.team_name)).setText(team
					.getName());
			tableTeamName.addView(rowTeamName);
			TableRow row = (TableRow) getLayoutInflater().inflate(
					R.layout.table_row, null);
			if (f) {
				row.setBackgroundColor(bgColor);
			}
			((TextView) row.findViewById(R.id.game_count)).setText(team
					.getGameCount().toString());
			((TextView) row.findViewById(R.id.wins)).setText(team.getWins()
					.toString());
			((TextView) row.findViewById(R.id.wins_ot)).setText(team
					.getWinsOT().toString());
			((TextView) row.findViewById(R.id.wins_so)).setText(team
					.getWinsSO().toString());
			((TextView) row.findViewById(R.id.loses_so)).setText(team
					.getLosesSO().toString());
			((TextView) row.findViewById(R.id.loses_ot)).setText(team
					.getLosesOT().toString());
			((TextView) row.findViewById(R.id.loses)).setText(team.getLoses()
					.toString());
			((TextView) row.findViewById(R.id.scoring_goals)).setText(team
					.getScoringGoals().toString());
			((TextView) row.findViewById(R.id.missed_goals)).setText(team
					.getMissedGoal().toString());
			((TextView) row.findViewById(R.id.scores)).setText(team.getScores()
					.toString());
			table.addView(row);
			f = !f;
		}

	}

	private class LoadTable extends AsyncTask<String, Void, List<String>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(TableActivity.this, "Working...",
					"request to server", true, false);
		}

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			MatchService.getInstance().loadTable(
					TournamentsList.getInstance().getCurrentTournament()
							.getId());
			return output;
		}

		@Override
		protected void onPostExecute(List<String> output) {
			pd.dismiss();
			fillTableView();
		}
	}

}
