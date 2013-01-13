package ru.khl.adapter;

import java.util.List;

import ru.khl.R;
import ru.khl.core.match.Penalty;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PenaltyAdapter extends ArrayAdapter<Penalty> {

	private final Context context;
	private final List<Penalty> allPenalties;
	private final int resourceId;

	public PenaltyAdapter(Context context, int resourceId,
			List<Penalty> allPenalties, List<Penalty> homePenalties,
			List<Penalty> awayPenalties) {
		super(context, resourceId, allPenalties);
		this.context = context;
		this.allPenalties = allPenalties;
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
			holder.time = (TextView) v.findViewById(R.id.penaltyTime);
			holder.number = (TextView) v.findViewById(R.id.penaltyNumber);
			holder.name = (TextView) v.findViewById(R.id.penaltyName);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Penalty penalty = allPenalties.get(position);
		if (penalty != null) {
			TextView time = holder.time;
			TextView number = holder.number;
			TextView name = holder.name;
			if (time != null) {
				time.setText(String.format("%d:%02d", penalty.getMinute(),
						penalty.getSecond()));
			}
			if (number != null) {
				number.setText(penalty.getSafetyPlayerNumber());
			}
			if (name != null) {
				name.setText(penalty.getSafetyPlayerName());
			}
		}
		return v;
	}

	static class ViewHolder {
		TextView time;
		TextView number;
		TextView name;
	}
}