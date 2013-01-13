package ru.khl.adapter;

import java.util.List;

import ru.khl.R;
import ru.khl.core.player.Player;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayerAdapter extends ArrayAdapter<Player> {

	private final Context context;
	private final List<Player> players;
	private final int resourceId;

	public PlayerAdapter(Context context, int resourceId, List<Player> players) {
		super(context, resourceId, players);
		this.context = context;
		this.players = players;
		this.resourceId = resourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(resourceId, null);
			holder = new ViewHolder();
			holder.number = (TextView) v.findViewById(R.id.playerNumber);
			holder.name = (TextView) v.findViewById(R.id.playerName);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Player player = players.get(position);
		if (player != null) {
			TextView number = holder.number;
			TextView name = holder.name;

			if (number != null) {
				number.setText(player.getNumber().toString());
			}
			if (name != null) {
				name.setText(player.getName());
			}
		}
		return v;
	}

	static class ViewHolder {
		TextView number;
		TextView name;
	}
}