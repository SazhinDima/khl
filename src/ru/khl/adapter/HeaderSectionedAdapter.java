package ru.khl.adapter;

import ru.khl.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonsware.android.listview.SectionedAdapter;

public class HeaderSectionedAdapter extends SectionedAdapter {

	LayoutInflater inflater;

	public HeaderSectionedAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	protected View getHeaderView(String caption, int index, View convertView,
			ViewGroup parent) {
		TextView result = (TextView) convertView;

		if (convertView == null) {
			result = (TextView) inflater.inflate(R.layout.header, null);
		}
		result.setText(caption);
		return (result);
	}

}
