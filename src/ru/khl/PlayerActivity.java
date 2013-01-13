package ru.khl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.khl.core.player.Player;
import ru.khl.core.player.PlayersMap;
import ru.khl.core.services.MatchService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PlayerActivity extends Activity {

	public static final String PLAYER_ID = "player_id";
	SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
	Player player;
	ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		player = PlayersMap.getInstance().obtainPlayerById(
				Integer.valueOf(getIntent().getStringExtra(PLAYER_ID)));

		TextView playerName = (TextView) findViewById(R.id.playerName);
		playerName.setText(player.getName());

		TextView playerNumber = (TextView) findViewById(R.id.playerNumber);
		playerNumber.setText(player.getNumber().toString());

		if (savedInstanceState == null) {
			new LoadPlayer().execute();
		} else {
			fillFields();
		}

	}

	public void fillFields() {
		TextView playerHeight = (TextView) findViewById(R.id.playerHeight);
		playerHeight.setText(player.getHeight().toString());
		TextView playerWeight = (TextView) findViewById(R.id.playerWeight);
		playerWeight.setText(player.getWeight().toString());
		TextView playerBorn = (TextView) findViewById(R.id.playerBorn);
		playerBorn.setText(dt.format(player.getBorn()));
		TextView playerPosition = (TextView) findViewById(R.id.playerPosition);
		int strId = getResources().getIdentifier(
				player.getPosition().toString(), "string", getPackageName());
		playerPosition.setText(getString(strId));
		ImageView playerImageView = (ImageView) findViewById(R.id.playerImageView);
		imageLoader.displayImage(player.getImageURL(), playerImageView);
	}

	private class LoadPlayer extends AsyncTask<String, Void, List<String>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(PlayerActivity.this, "Working...",
					"request to server", true, false);
		}

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			MatchService.getInstance().loadPlayer(player.getId());
			return output;
		}

		@Override
		protected void onPostExecute(List<String> output) {
			pd.dismiss();
			fillFields();
		}
	}

}
