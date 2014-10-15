
package com.example.activity;

import com.example.gamebomb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameResult extends Activity {

	private String LOG = "dLog";

	// private ImageView btn_play_again;
	private RelativeLayout game_result_rl_play_again;
	private TextView tv_text_result, game_result_tv_play_again,
			game_result_tv_user_score_title, game_result_tv_user_score,
			game_result_tv_highest_title, game_result_tv_highest;
	private String title = "win_lose";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_result);

		
		game_result_rl_play_again = (RelativeLayout) findViewById(R.id.game_result_rl_play_again);
		tv_text_result = (TextView) findViewById(R.id.tv_text_result);
		game_result_tv_play_again = (TextView) findViewById(R.id.game_result_tv_play_again);
		game_result_tv_user_score_title = (TextView) findViewById(R.id.game_result_tv_user_score_title);
		game_result_tv_user_score = (TextView) findViewById(R.id.game_result_tv_user_score);
		game_result_tv_highest_title = (TextView) findViewById(R.id.game_result_tv_highest_title);
		game_result_tv_highest = (TextView) findViewById(R.id.game_result_tv_highest);

		// set font type for texts in all lebel following 
		tv_text_result.setTypeface(WelcomeGame.typeface);
		game_result_tv_play_again.setTypeface(WelcomeGame.typeface);
		game_result_tv_user_score_title.setTypeface(WelcomeGame.typeface);
		game_result_tv_user_score.setTypeface(WelcomeGame.typeface);
		game_result_tv_highest_title.setTypeface(WelcomeGame.typeface);
		game_result_tv_highest.setTypeface(WelcomeGame.typeface);

		// catch the bundle which was put from previous form
		Bundle extras = this.getIntent().getExtras();
		title = extras.getString(PlayBombActivity.WIN_LOSE);

		// display the score of user
		game_result_tv_user_score.setText(String
				.valueOf(PlayBombActivity.count));
		PlayBombActivity.count = 0;
		// set the higher score of each dificult
		if(WelcomeGame.isUnlimited)
		{
			game_result_tv_highest.setText(String.valueOf(WelcomeGame.UnlimitNum));
		}else if ((WelcomeGame.level == 0) || (WelcomeGame.level == 1)) {
			game_result_tv_highest.setText(String.valueOf(WelcomeGame.easyNum));
		} else if (WelcomeGame.level == 2) {
			game_result_tv_highest.setText(String
					.valueOf(WelcomeGame.mediumNum));
		} else if (WelcomeGame.level == 3) {
			game_result_tv_highest.setText(String.valueOf(WelcomeGame.hardNum));
		}
		tv_text_result.setText(title);
		game_result_rl_play_again.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GameResult.this, PlayBombActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	// handle the event for back key on keybord
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(LOG, "keycode gameresult: " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
