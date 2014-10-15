package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.gamebomb.R;

public class SplashCount extends Activity {

	private String LOG = "dLog";
	private Boolean isCount = false;
	private static final long TIME = 1000;
	private TextView splash_tv_count_number, splash_tv_line1, splash_tv_line2;
	long timeLimit = 3 * 1000;
	long time_begin = -1;
	long time_count = 0;

	public void onCreate(Bundle savedInstanceState) {

		if (getIntent().getBooleanExtra("EXIT", false)) {
			System.exit(0);
		} else {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.splash);
			splash_tv_count_number = (TextView) findViewById(R.id.splash_tv_count_number);
			splash_tv_line1 = (TextView) findViewById(R.id.splash_tv_line1);
			splash_tv_line2 = (TextView) findViewById(R.id.splash_tv_line2);

			splash_tv_count_number.setTypeface(WelcomeGame.typeface);
			splash_tv_line1.setTypeface(WelcomeGame.typeface);
			splash_tv_line2.setTypeface(WelcomeGame.typeface);
			if(WelcomeGame.isUnlimited)
			{
				splash_tv_line1.setText("You have unlimited time to defuse bombs!");
			}
			// create a thread to start counting
			Thread welcomeThread = new Thread() {

				@Override
				public void run() {
					try {
						// if(is)
						while (true) {
							if (!isCount) {
								time_begin = System.currentTimeMillis();
							} else {
								time_count += (System.currentTimeMillis() - time_begin);
								time_begin = System.currentTimeMillis();

								final int count = (int) (timeLimit - time_count) / 1000 + 1;
								if (time_count < timeLimit) {
									// post to UI the number
									splash_tv_count_number.post(new Runnable() {

										@Override
										public void run() {
											splash_tv_count_number
													.setText(String
															.valueOf(count));
										}
									});
								} else {
									break;
								}
								
							}
							Thread.sleep(100);
						}
					} catch (Exception e) {
						Log.e(getClass().getName(), e.toString());
					}
					startActivity(new Intent(SplashCount.this,
							PlayBombActivity.class));
					finish();
				}
			};
			welcomeThread.start();
		}
	}

	//prevent the event back key in this form
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		isCount = false;
		super.onPause();
	}

	// resume the counter when game was interrupted
	@Override
	protected void onResume() {
		isCount = true;
		time_begin = System.currentTimeMillis();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.onResume();
	}

}
