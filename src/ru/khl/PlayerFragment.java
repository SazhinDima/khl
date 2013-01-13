package ru.khl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.khl.core.player.Player;
import ru.khl.core.player.PlayersMap;
import ru.khl.core.services.MatchService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PlayerFragment extends DialogFragment {

	public static final String PLAYER_ID = "player_id";

	Player player;
	ImageLoader imageLoader;
	SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

	public static PlayerFragment newInstance(Integer playerId) {
		PlayerFragment f = new PlayerFragment();

		Bundle args = new Bundle();
		args.putInt(PLAYER_ID, playerId);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_player, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		player = PlayersMap.getInstance().obtainPlayerById(
				getArguments().getInt(PLAYER_ID));
		getDialog().setTitle(player.getName());
		new LoadPlayer().execute();

	}

	public void fillFields() {
		TextView playerHeight = (TextView) getView().findViewById(
				R.id.playerHeight);
		playerHeight.setText(player.getHeight().toString());
		TextView playerWeight = (TextView) getView().findViewById(
				R.id.playerWeight);
		playerWeight.setText(player.getWeight().toString());
		TextView playerBorn = (TextView) getView()
				.findViewById(R.id.playerBorn);
		playerBorn.setText(dt.format(player.getBorn()));
		TextView playerNumber = (TextView) getView().findViewById(
				R.id.playerNumber);
		playerNumber.setText(player.getNumber().toString());
		TextView playerPosition = (TextView) getView().findViewById(
				R.id.playerPosition);
		playerPosition.setText(getResources().getIdentifier(
				player.getPosition().toString(), "string",
				getActivity().getPackageName()));
		ImageView playerImageView = (ImageView) getView().findViewById(
				R.id.playerImageView);
		imageLoader.displayImage(player.getImageURL(), playerImageView);
	}

	private class LoadPlayer extends AsyncTask<String, Void, List<String>> {

		@Override
		protected List<String> doInBackground(String... arg) {
			List<String> output = new ArrayList<String>();
			MatchService.getInstance().loadPlayer(player.getId());
			return output;
		}

		@Override
		protected void onPostExecute(List<String> output) {
			fillFields();
		}
	}

}
