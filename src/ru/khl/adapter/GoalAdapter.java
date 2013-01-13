package ru.khl.adapter;

import java.util.List;

import ru.khl.R;
import ru.khl.core.match.Goal;
import ru.khl.core.player.Player;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GoalAdapter extends ArrayAdapter<Goal> {

	private final Context context;
	private final List<Goal> allGoals;
	private final int resourceId;

	public GoalAdapter(Context context, int resourceId, List<Goal> allGoals,
			List<Goal> homeGoals, List<Goal> awayGoals) {
		super(context, resourceId, allGoals);
		this.context = context;
		this.allGoals = allGoals;
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
			holder.time = (TextView) v.findViewById(R.id.goalTime);
			holder.number = (TextView) v.findViewById(R.id.goalNumber);
			holder.name = (TextView) v.findViewById(R.id.goalName);
			holder.assistant1 = (TextView) v.findViewById(R.id.assistant1Name);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Goal goal = allGoals.get(position);
		if (goal != null) {
			Player player = goal.getPlayer();
			Player assistant1 = goal.getAssistant1();
			Player assistant2 = goal.getAssistant2();
			TextView time = holder.time;
			TextView number = holder.number;
			TextView name = holder.name;
			TextView assistant1Name = holder.assistant1;
			if (time != null) {
				time.setText(String.format("%d:%02d", goal.getMinute(),
						goal.getSecond()));
			}
			if (number != null) {
				number.setText(player.getNumber().toString());
			}
			if (name != null) {
				name.setText(player.getName());
			}
			if (assistant1Name != null) {
				if (assistant1 == null) {
					assistant1Name.setText("");
				} else {
					assistant1Name.setText(assistant1.getName());
				}
			}
		}
		return v;
	}

	static class ViewHolder {
		TextView time;
		TextView number;
		TextView name;
		TextView assistant1;
	}
}