package ru.khl;

import java.util.ArrayList;
import java.util.List;

import ru.khl.core.services.MatchService;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class LoadingActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		new Loading().execute();
	}

	private class Loading extends AsyncTask<String, Void, List<String>> {

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			MatchService.getInstance().loadTournaments();
			return output;
		}

		@Override
		protected void onPostExecute(List<String> output) {
			Intent mainActivity = new Intent(LoadingActivity.this,
					MainActivity.class);
			startActivity(mainActivity);
		}
	}
}
