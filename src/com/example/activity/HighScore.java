package com.example.activity;

import com.example.gamebomb.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class HighScore extends Activity {
	private TextView high_score_tv_title, high_score_tv_difficult,
			high_score_tv_words, high_score_tv_easy, high_score_tv_easy_number,
			high_score_tv_medium, high_score_tv_easy_medium,
			high_score_tv_hard, high_score_tv_easy_hard;
	private TextView tv_unlimited;
	private TextView high_score_tv_unlimited;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.high_score);

		// retrieve all higher score from SharedPreferences.
		WelcomeGame.preferences = this.getSharedPreferences(
				WelcomeGame.PRE_NAME_SCORE, 0);
		int easy_score = WelcomeGame.preferences.getInt("easyNum",
				WelcomeGame.easyNum);
		int medium_score = WelcomeGame.preferences.getInt("mediumNum",
				WelcomeGame.mediumNum);
		int hard_score = WelcomeGame.preferences.getInt("hardNum",
				WelcomeGame.hardNum);
		int Unlimit_score = WelcomeGame.preferences.getInt("UnlimitNum",
				WelcomeGame.UnlimitNum);

		high_score_tv_title = (TextView) findViewById(R.id.high_score_tv_title);
		high_score_tv_difficult = (TextView) findViewById(R.id.high_score_tv_difficult);
		high_score_tv_words = (TextView) findViewById(R.id.high_score_tv_words);
		high_score_tv_easy = (TextView) findViewById(R.id.high_score_tv_easy);
		high_score_tv_easy_number = (TextView) findViewById(R.id.high_score_tv_easy_number);
		high_score_tv_medium = (TextView) findViewById(R.id.high_score_tv_medium);
		high_score_tv_easy_medium = (TextView) findViewById(R.id.high_score_tv_easy_medium);
		high_score_tv_hard = (TextView) findViewById(R.id.high_score_tv_hard);
		high_score_tv_easy_hard = (TextView) findViewById(R.id.high_score_tv_easy_hard);
		
		// set font for the text.
		tv_unlimited = (TextView) findViewById(R.id.tv_unlimited);
		high_score_tv_unlimited = (TextView) findViewById(R.id.high_score_tv_unlimited);

		high_score_tv_title.setTypeface(WelcomeGame.typeface);
		high_score_tv_difficult.setTypeface(WelcomeGame.typeface);
		high_score_tv_words.setTypeface(WelcomeGame.typeface);
		high_score_tv_easy.setTypeface(WelcomeGame.typeface);
		high_score_tv_easy_number.setTypeface(WelcomeGame.typeface);
		high_score_tv_medium.setTypeface(WelcomeGame.typeface);
		high_score_tv_easy_medium.setTypeface(WelcomeGame.typeface);
		high_score_tv_hard.setTypeface(WelcomeGame.typeface);
		high_score_tv_easy_hard.setTypeface(WelcomeGame.typeface);
		
		tv_unlimited.setTypeface(WelcomeGame.typeface);
		high_score_tv_unlimited.setTypeface(WelcomeGame.typeface);

		high_score_tv_easy_number.setText(String.valueOf(easy_score));
		high_score_tv_easy_medium.setText(String.valueOf(medium_score));
		high_score_tv_easy_hard.setText(String.valueOf(hard_score));
		high_score_tv_unlimited.setText(String.valueOf(Unlimit_score));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, WelcomeGame.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
