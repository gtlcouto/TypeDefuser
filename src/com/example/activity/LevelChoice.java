package com.example.activity;

import com.example.gamebomb.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelChoice extends Activity implements OnClickListener {

	private TextView level_choice_tv_easy, level_choice_tv_medium,
			level_choice_tv_hard, tv_unlimited;
	private ImageView level_choice_img_box_easy, level_choice_img_box_medium,
			level_choice_img_box_hard, img_unlimited;

	SharedPreferences sharedPreferences;
	public static String PRE_NAME_LEVEL = "choose_level";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_choice);

		// get the previously values which user was choose 
		sharedPreferences = this.getSharedPreferences(PRE_NAME_LEVEL, 0);
		WelcomeGame.isEasy = sharedPreferences.getBoolean("isEasy",
				WelcomeGame.isEasy);
		WelcomeGame.isMedium = sharedPreferences.getBoolean("isMedium",
				WelcomeGame.isMedium);
		WelcomeGame.isHard = sharedPreferences.getBoolean("isHard",
				WelcomeGame.isHard);

		WelcomeGame.isUnlimited = sharedPreferences.getBoolean("isUnlimited",
				false);

		level_choice_tv_easy = (TextView) findViewById(R.id.level_choice_tv_easy);
		level_choice_tv_medium = (TextView) findViewById(R.id.level_choice_tv_medium);
		level_choice_tv_hard = (TextView) findViewById(R.id.level_choice_tv_hard);
		tv_unlimited = (TextView) findViewById(R.id.tv_unlimited);

		level_choice_tv_easy.setTypeface(WelcomeGame.typeface);
		level_choice_tv_medium.setTypeface(WelcomeGame.typeface);
		level_choice_tv_hard.setTypeface(WelcomeGame.typeface);
		tv_unlimited.setTypeface(WelcomeGame.typeface);

		level_choice_img_box_easy = (ImageView) findViewById(R.id.level_choice_img_box_easy);
		level_choice_img_box_medium = (ImageView) findViewById(R.id.level_choice_img_box_medium);
		level_choice_img_box_hard = (ImageView) findViewById(R.id.level_choice_img_box_hard);
		img_unlimited = (ImageView) findViewById(R.id.img_unlimited);
		// set icon for the level that was choose.
		if (WelcomeGame.isEasy) {
			level_choice_img_box_easy
					.setImageResource(R.drawable.radio_checked);

		} else {
			level_choice_img_box_easy
					.setImageResource(R.drawable.img_check_box);
		}
		if (WelcomeGame.isMedium) {
			level_choice_img_box_medium
					.setImageResource(R.drawable.radio_checked);
		} else {
			level_choice_img_box_medium
					.setImageResource(R.drawable.img_check_box);
		}
		if (WelcomeGame.isHard) {
			level_choice_img_box_hard
					.setImageResource(R.drawable.radio_checked);
		} else {
			level_choice_img_box_hard
					.setImageResource(R.drawable.img_check_box);
		}

		img_unlimited
				.setImageResource(WelcomeGame.isUnlimited ? R.drawable.radio_checked
						: R.drawable.img_check_box);

	}
	//handle the click event choosing dificult
	@Override
	public void onClick(View v) {
		WelcomeGame.isEasy = false;
		WelcomeGame.isMedium = false;
		WelcomeGame.isHard = false;
		WelcomeGame.isUnlimited = false;

		switch (v.getId()) {
		case R.id.level_choice_rl_easy:
			WelcomeGame.isEasy = true;
			WelcomeGame.level = 1;
			break;
		case R.id.level_choice_rl_medium:
			WelcomeGame.isMedium = true;
			WelcomeGame.level = 2;
			break;
		case R.id.level_choice_rl_hard:
			WelcomeGame.isHard = true;
			WelcomeGame.level = 3;
			break;
		case R.id.ll_unlimited:
			WelcomeGame.isUnlimited = true;
			WelcomeGame.level = 1;
			break;
		}
		level_choice_img_box_easy
				.setImageResource(WelcomeGame.isEasy ? R.drawable.radio_checked
						: R.drawable.img_check_box);
		level_choice_img_box_medium
				.setImageResource(WelcomeGame.isMedium ? R.drawable.radio_checked
						: R.drawable.img_check_box);
		level_choice_img_box_hard
				.setImageResource(WelcomeGame.isHard ? R.drawable.radio_checked
						: R.drawable.img_check_box);
		img_unlimited
				.setImageResource(WelcomeGame.isUnlimited ? R.drawable.radio_checked
						: R.drawable.img_check_box);
	}

	//handle the key back. 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	// Called when user create a interupt in game. Using to save rms for game.
	@Override
	protected void onStop() {
		super.onStop();
		sharedPreferences = this.getSharedPreferences(PRE_NAME_LEVEL, 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("isEasy", WelcomeGame.isEasy);
		editor.putBoolean("isMedium", WelcomeGame.isMedium);
		editor.putBoolean("isHard", WelcomeGame.isHard);
		editor.putBoolean("isUnlimited", WelcomeGame.isUnlimited);
		editor.commit();
	}

	//Load data when user come back game.
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		WelcomeGame.isEasy = savedInstanceState.getBoolean("isEasy");
		WelcomeGame.isMedium = savedInstanceState.getBoolean("isMedium");
		WelcomeGame.isHard = savedInstanceState.getBoolean("isHard");
		WelcomeGame.isUnlimited = savedInstanceState.getBoolean("isUnlimited");
	}

	//push data when occure a interut.
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isEasy", WelcomeGame.isEasy);
		outState.putBoolean("isMedium", WelcomeGame.isMedium);
		outState.putBoolean("isHard", WelcomeGame.isHard);
		outState.putBoolean("isUnlimited", WelcomeGame.isUnlimited);

	}

}
