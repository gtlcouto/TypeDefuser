/*
 * Project: WelcomeGame
 * Author: Gustavo Couto
 * Description: Final Project CPA Program. Game where the user has to defuse a bomb in 10 seconds 
 * based on key word given. 
 */

package com.example.activity;

import com.example.gamebomb.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class WelcomeGame extends Activity implements OnClickListener {
	private String LOG = "dLog";
	public static int level = 0;
	private TextView mainmenu_tv_difficult, mainmenu_tv_start,
			mainmenu_tv_high_score, mainmenu_tv_about;

	public static Typeface typeface;
	public static int easyNum = 0;
	public static int mediumNum = 0;
	public static int hardNum = 0;
	public static int UnlimitNum = 0;

	public static Boolean isEasy = true;
	public static Boolean isMedium = false;
	public static Boolean isHard = false;
	public static Boolean isUnlimited = false;

	public static SharedPreferences preferences;
	public static String PRE_NAME_SCORE = "score";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isTaskRoot()) {
			finish();
			return;
		}
		// get the level was saved before

		WelcomeGame.preferences = this.getSharedPreferences(
				LevelChoice.PRE_NAME_LEVEL, 0);
		WelcomeGame.isEasy = WelcomeGame.preferences.getBoolean("isEasy",
				WelcomeGame.isEasy);
		WelcomeGame.isMedium = WelcomeGame.preferences.getBoolean("isMedium",
				WelcomeGame.isMedium);
		WelcomeGame.isHard = WelcomeGame.preferences.getBoolean("isHard",
				WelcomeGame.isHard);

		WelcomeGame.isUnlimited = WelcomeGame.preferences.getBoolean(
				"isUnlimited", false);

		setContentView(R.layout.mainmenu);
		// set font type for text in game
		typeface = Typeface.createFromAsset(getAssets(),
				"fonts/KiloGram_KG.otf");
		mainmenu_tv_difficult = (TextView) findViewById(R.id.mainmenu_tv_difficult);
		mainmenu_tv_start = (TextView) findViewById(R.id.mainmenu_tv_start);
		mainmenu_tv_high_score = (TextView) findViewById(R.id.mainmenu_tv_high_score);
		mainmenu_tv_about = (TextView) findViewById(R.id.mainmenu_tv_about);

		mainmenu_tv_difficult.setTypeface(typeface);
		mainmenu_tv_start.setTypeface(typeface);
		mainmenu_tv_high_score.setTypeface(typeface);
		mainmenu_tv_about.setTypeface(typeface);
	}

	// handle the onclick event.
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainmenu_rl_level:
			Intent inte = new Intent(this, LevelChoice.class);
			startActivity(inte);
			break;
		case R.id.mainmenu_rl_start:
			Intent i = new Intent(WelcomeGame.this, SplashCount.class);
			startActivity(i);
			break;
		case R.id.mainmenu_rl_high_about:
			Intent intent = new Intent(WelcomeGame.this, About.class);
			startActivity(intent);
			break;
		case R.id.mainmenu_rl_high_score:
			Intent in = new Intent(WelcomeGame.this, HighScore.class);
			startActivity(in);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
