package ru.khl.adapter;

import java.util.List;

import ru.khl.R;
import ru.khl.core.match.Match;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MatchAdapter extends ArrayAdapter<Match> {

	private final Context context;
	private final List<Match> items;
	private final int resourceId;

	public MatchAdapter(Context context, int resourceId, List<Match> items) {
		super(context, resourceId, items);
		this.context = context;
		this.items = items;
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
			holder.homeName = (TextView) v.findViewById(R.id.homeName);
			holder.homeCount = (TextView) v.findViewById(R.id.homeCount);
			holder.awayName = (TextView) v.findViewById(R.id.awayName);
			holder.awayCount = (TextView) v.findViewById(R.id.awayCount);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Match match = items.get(position);
		if (match != null) {
			TextView homeName = holder.homeName;
			TextView homeCount = holder.homeCount;
			TextView awayName = holder.awayName;
			TextView awayCount = holder.awayCount;
			if (homeName != null) {
				homeName.setText(match.getHome().getTeam().getName());
			}
			if (homeCount != null) {
				homeCount.setText(match.getHome().getSafetyCount());
			}
			if (awayName != null) {
				awayName.setText(match.getAway().getTeam().getName());
			}
			if (awayCount != null) {
				awayCount.setText(match.getAway().getSafetyCount());
			}
		}
		return v;
	}

	static class ViewHolder {
		TextView homeName;
		TextView homeCount;
		TextView awayName;
		TextView awayCount;
	}
}