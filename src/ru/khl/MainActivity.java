package ru.khl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button matches = (Button) findViewById(R.id.matches);
		matches.setOnClickListener(this);

		Button table = (Button) findViewById(R.id.table);
		table.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.matches:
			Intent matchesIntent = new Intent(MainActivity.this,
					MatchesActivity.class);
			startActivity(matchesIntent);
			break;
		case R.id.table:
			Intent tableIntent = new Intent(MainActivity.this,
					TableActivity.class);
			startActivity(tableIntent);
			break;
		}

	}
}
