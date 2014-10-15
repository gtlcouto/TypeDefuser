package com.example.activity;

import com.example.gamebomb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class About extends Activity {

	private TextView about_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		about_text = (TextView) findViewById(R.id.about_text);
		about_text.setTypeface(WelcomeGame.typeface);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//finish this activity by stopping it
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
