
package com.example.activity;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.example.gamebomb.R;
import com.example.managerdata.ManagerData;
import com.example.managerdata.SitesList;
import com.example.managerdata.Sprite;
import com.example.managerdata.XMLHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Region;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayBombActivity extends Activity {

	private String LOG = "dLog";

	private TextView txt_timer, txt_random, tv_display_text;
	private EditText edt_user_text;
	private StartTimer startTimer = null;
	private LinearLayout ln_text_enter;
	private ImageView img_bomb, img_next, imageView1;
	private String[] listText;

	private Boolean isGameRun = true;
	private Boolean isPlaying = false;
	private Boolean isBreak = false;
	private String textOfUser, textRandom;

	int count_text = 0;
	SitesList sitesList;
	Random random = new Random();
	ArrayList<String> str = (ArrayList<String>) new ArrayList<String>();

	Sprite sp_fire;
	Point fire_start_point = new Point(24, 7);
	public ArrayList<Point> fire_point = new ArrayList<Point>();

	int index = 0;
	static Bitmap[] explosion;
	public static String WIN_LOSE = "win_lose";

	long timeLimit = 10 * 1000;
	long time_begin = -1;
	long time_play = 0;
	long time_total = 0;
	public static int count = 0;
	MediaPlayer mp = null;
	Boolean isRightText = false;
	Boolean isWrongText = false;
	Boolean isWin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_game);

		// get the difficult which user choose before.
		WelcomeGame.preferences = this.getSharedPreferences(
				WelcomeGame.PRE_NAME_SCORE, 0);
		WelcomeGame.easyNum = WelcomeGame.preferences.getInt("easyNum",
				WelcomeGame.easyNum);
		WelcomeGame.mediumNum = WelcomeGame.preferences.getInt("mediumNum",
				WelcomeGame.mediumNum);
		WelcomeGame.hardNum = WelcomeGame.preferences.getInt("hardNum",
				WelcomeGame.hardNum);
		WelcomeGame.UnlimitNum = WelcomeGame.preferences.getInt("UnlimitNum",
				WelcomeGame.UnlimitNum);
		if (WelcomeGame.isUnlimited) {
			WelcomeGame.level = WelcomeGame.preferences.getInt("levelPass", 1);
		}


		// get the level which user choose before.
		WelcomeGame.preferences = this.getSharedPreferences(
				LevelChoice.PRE_NAME_LEVEL, 0);
		if (WelcomeGame.preferences.getBoolean("isEasy", WelcomeGame.isEasy)) {
			WelcomeGame.level = 1;
			Log.d(LOG, "level = " + WelcomeGame.level);
		}
		if (WelcomeGame.preferences
				.getBoolean("isMedium", WelcomeGame.isMedium)) {
			WelcomeGame.level = 2;
			Log.d(LOG, "level = " + WelcomeGame.level);
		}
		if (WelcomeGame.preferences.getBoolean("isHard", WelcomeGame.isHard)) {
			WelcomeGame.level = 3;
			Log.d(LOG, "level = " + WelcomeGame.level);
		}
		
		txt_timer = (TextView) findViewById(R.id.txt_timer);
		img_bomb = (ImageView) findViewById(R.id.img_bomb);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		txt_random = (TextView) findViewById(R.id.txt_random);
		edt_user_text = (EditText) findViewById(R.id.edt_user_text);

		// call this function to get the data from xml file.
		getTextfromXML();
		listText = new String[str.size()];
		for (int i = 0; i < str.size(); i++) {
			listText[i] = str.get(i);
		}
		init();
		playGame();
	}

	private final int ID_ACTION_FIRE = 0;
	Bitmap bmp_make_wires = null;
	Bitmap bmp_wires = null;
	Bitmap bmp_bom_b = null;

	//initialized in the game: Load images, frames, load path pixel
	private void init() {

		// TODO Auto-generated method stub
		Resources res = getResources();
		AssetManager mngr = this.getAssets();
		ManagerData.init(mngr);
		String[] str_explosions = res.getStringArray(R.array.explosions);
		explosion = new Bitmap[str_explosions.length];
		for (int i = 0; i < str_explosions.length; i++) {
			explosion[i] = ManagerData.LoadImgFromAssets(str_explosions[i]
					+ ".png");
		}

		bmp_wires = ManagerData.LoadImgFromAssets("wires.png");
		bmp_bom_b = ManagerData.LoadImgFromAssets("bom_b.png");

		// Load line image maker
		if (fire_point.size() == 0) {

			bmp_make_wires = ManagerData.LoadImgFromAssets("make_wires.png");

			fire_start_point = null;
			for (int i = 0; i < bmp_make_wires.getWidth(); i++)
				if (fire_start_point == null) {
					for (int j = 0; j < bmp_make_wires.getHeight(); j++) {
						int c = bmp_make_wires.getPixel(i, j);
						if (c == 0xFFFF00FF) {
							fire_start_point = new Point(i, j);
							// bmp_make_wires.setPixel(i, j, 0xff00ff);
							break;
						}
					}
				} else
					break;

			Point point = fire_start_point;
			int c = bmp_make_wires.getPixel(point.x, point.y);
			while (point != null) {
				fire_point.add(fire_point.size(), point);

				if (point.x < bmp_make_wires.getWidth()) {
					if ((point.y > 0)
							&& (bmp_make_wires.getPixel(point.x + 1,
									point.y - 1) == c))
						point = new Point(point.x + 1, point.y - 1);
					else if (bmp_make_wires.getPixel(point.x + 1, point.y) == c)
						point = new Point(point.x + 1, point.y);
					else if ((point.y < bmp_make_wires.getHeight() - 1)
							&& (bmp_make_wires.getPixel(point.x + 1,
									point.y + 1) == c))
						point = new Point(point.x + 1, point.y + 1);
					else
						point = null;
				} else
					point = null;
			}
		}

		Bitmap bmp_fire = ManagerData.LoadImgFromAssets("fire.png");
		sp_fire = new Sprite(bmp_fire, 4, 1);
		int[] frame = new int[] { 0, 1, 2, 3 };
		sp_fire.addAction(ID_ACTION_FIRE, frame);
	}
	
	//stop main thread
	public void stopCount(View v) {
		startTimer.cancel(true);
	}
	//called when user pressed back key on phone.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mp.stop();
			if ((startTimer != null) && (!startTimer.isCancelled()))
				startTimer.cancel(true);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	//sub class extends AsyncTask: run background
	private class StartTimer extends AsyncTask<Void, Integer, Void> {
		
		//Override this method to perform a computation on a background thread. 
		//The specified parameters are the parameters passed to execute(Params...) by the caller of this task.
		@Override
		protected Void doInBackground(Void... params) {
			isBreak = false;
			while (true) {
				if (isGameRun) {
					while (time_play < timeLimit) {
						if (!isPlaying) {
							time_begin = System.currentTimeMillis();
						} else {
							time_play += (System.currentTimeMillis() - time_begin);
							time_total += (System.currentTimeMillis() - time_begin);
							time_begin = System.currentTimeMillis();
							if (isRightText) {
								time_play = 1;
								count++;
								if(WelcomeGame.isUnlimited)
								{
									WelcomeGame.UnlimitNum = Math.max(count, WelcomeGame.UnlimitNum);									
								}else if (WelcomeGame.level == 0
										|| WelcomeGame.level == 1) {
									if (count >= WelcomeGame.easyNum) {
										WelcomeGame.easyNum = count;

									}
								} else if (WelcomeGame.level == 2) {
									if (count >= WelcomeGame.mediumNum) {
										WelcomeGame.mediumNum = count;

									}
								} else if (WelcomeGame.level == 3) {
									if (count >= WelcomeGame.hardNum) {
										WelcomeGame.hardNum = count;
									}
								}
							}
							int tb_time = (int) (timeLimit - time_play) / 1000;
							Log.d(LOG, "time_total = " + time_total / 1000);
							if ((!WelcomeGame.isUnlimited && (((time_total / 1000) >= 60) && (tb_time == 0)))
									|| isWrongText) {
								isGameRun = false;
								tb_time = -1;
								isWrongText = false;
								time_play = 11 * 1000;
							}
							if (!WelcomeGame.isUnlimited && ((time_total / 1000) >= 60 && isRightText)) {
								isWin = true;
							}
							isRightText = false;
							publishProgress(tb_time + 1);
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						if (isCancelled()) {
							return null;
						}
					}
					isGameRun = false;

				} else {
					index = (index + 1) % explosion.length;
					if (index == 1) {
						playMedia(R.drawable.bum, false);
					}
					publishProgress(0);
					try {
						Thread.sleep(100);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
				if (isCancelled()) {
					return null;
				}
			}
			// return null;
		}
		
		//Runs on the UI thread after publishProgress(Progress...) is invoked. 
		//The specified values are the values passed to publishProgress(Progress...).
		@Override
		protected void onProgressUpdate(Integer... values) {
			img_bomb.setImageBitmap(getPageBitmap());
			txt_timer.setText(values[0].toString());

			if (!isGameRun) {
				txt_timer.setVisibility(View.INVISIBLE);
				imageView1.setVisibility(View.INVISIBLE);
			}
			if (isWin) {
				isPlaying = false;
				mp.stop();
				if (!startTimer.isCancelled())
					startTimer.cancel(true);
				Intent i = new Intent(PlayBombActivity.this, GameResult.class);
				Bundle bundle = new Bundle();
				bundle.putString(WIN_LOSE,
						"YOU DEFUSED ALL THE BOMBS! YOU WIN!");
				i.putExtras(bundle);
				startActivity(i);
				finish();
				isWin = false;
			}

			if ((!isGameRun) && (index >= explosion.length - 1)) {
				isBreak = true;
				isPlaying = false;
				txt_timer.setText("");
				result_game();
				isGameRun = true;
			}
		}
	}
	//Perform any final cleanup before an activity is destroyed.
	@Override
	protected void onDestroy() {
		if (startTimer != null) {
			startTimer.cancel(true);
		}
		super.onDestroy();
	}
	
	//when use pause game, this method to pause game.
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG, "onPause of PlayBombActivity is triggered");
		isPlaying = false;
		mp.pause();
		time_play += (System.currentTimeMillis() - time_begin);
		time_begin = System.currentTimeMillis();
	}

	//Resume the game after game paused
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG, "onResume of PlayBombActivity is triggered");
		playMedia(R.drawable.tictac, true);
		isPlaying = true;
		time_begin = System.currentTimeMillis();
	}

	//Load data text from .xml file
	private void getTextfromXML() {
		try {

			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			InputSource is = null;

			// get data commensurate with level 
			AssetManager assetManager = this.getAssets();
			if ((WelcomeGame.level == 0) || (WelcomeGame.level == 1)) {
				is = new InputSource(assetManager.open("easy_data.xml"));
			} else if (WelcomeGame.level == 2) {
				is = new InputSource(assetManager.open("medium_data.xml"));
			} else if (WelcomeGame.level == 3) {
				is = new InputSource(assetManager.open("hard_data.xml"));
			}

			XMLHandler myXMLHandler = new XMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(is.getByteStream()));

		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		sitesList = XMLHandler.sitesList;
		for (int i = 0; i < sitesList.getText().size(); i++) {
			str.add(sitesList.getText().get(i));
		}
	}

	//Next form result game
	private void result_game() {
		stopCount(txt_timer);
		mp.stop();
		Intent i = new Intent(this, GameResult.class);
		Bundle bundle = new Bundle();
		// if (!isWin) {
		bundle.putString(WIN_LOSE, "THE BOMB EXPLODED! YOU LOST!");
		i.putExtras(bundle);
		startActivity(i);
		finish();
	}

	//start game 
	private void playGame() {

		txt_timer.setVisibility(View.VISIBLE);
		imageView1.setVisibility(View.VISIBLE);
		isPlaying = true;
		time_play = 0;
		time_begin = System.currentTimeMillis();
		int index = random.nextInt(listText.length);
		txt_random.setText(listText[index]);
		edt_user_text.addTextChangedListener(new TextWatcher() {

			// to check the text that user type with the test random
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textRandom = txt_random.getText().toString().trim();
				if (s.length() < textRandom.length()) {

					for (int i = 0; i < s.length(); i++) {
						if (!String
								.valueOf(s.charAt(i))
								.trim()
								.equalsIgnoreCase(
										String.valueOf(textRandom.charAt(i))
												.trim())) {
							isWrongText = true;
						}
					}
				} else if (s.length() == textRandom.length()) {
					if ((s.toString().trim()).equalsIgnoreCase((textRandom
							.trim()))) {
						if((WelcomeGame.level<3)&&WelcomeGame.isUnlimited)
						{
							if(PlayBombActivity.count%10==9)
							{							
								WelcomeGame.level++;								
								reloadText();
							}
						}
						int index = random.nextInt(listText.length);
						txt_random.setText(listText[index]);
						edt_user_text.setText("");
						isRightText = true;						
					} else {
						isWrongText = true;
					}
				} else {
					isWrongText = true;
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		startTimer = new StartTimer();
		startTimer.execute();
	}
	
	//reload data text in module "Unlimited"
	void reloadText()
	{
		str.clear();
		getTextfromXML();
		listText = new String[str.size()];
		for (int i = 0; i < str.size(); i++) {
			listText[i] = str.get(i);
		}
	}
	
	//play file media."Bum.."
	private void playMedia(int idAudio, boolean isLoop) {
		// TODO Auto-generated method stub
		mp = MediaPlayer.create(this, idAudio);
		mp.setLooping(isLoop);
		mp.start();
	}

	//Draw bom in game
	private Bitmap getPageBitmap() { // ham ve trong game
		int width = 512;
		int height = 512;

		int bom_b_mk_x = -78;
		int bom_b_mk_y = -5;
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		if (!isGameRun) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = true;			
			Bitmap bmp_explosion = explosion[index];			
			if (index < 8)
				canvas.drawBitmap(bmp_bom_b, (width - 176) / 2,
						(height - 195) / 2, null);
			// draw the effect 
			canvas.drawBitmap(bmp_explosion, (width - 512) / 2,
					(height - 512) / 2, null);
		} else {
			int bmp_bom_b_x = (width - bmp_bom_b.getWidth()) / 2;
			int bmp_bom_b_y = (height - bmp_bom_b.getHeight()) / 2;
			// draw bomb fist
			canvas.drawBitmap(bmp_bom_b, bmp_bom_b_x, bmp_bom_b_y, null);

			int pos = (int) Math.min(time_play * fire_point.size() / timeLimit,
					fire_point.size() - 1);
			Point point = fire_point.get(pos);
			// draw the remain wire.
			canvas.clipRect(bmp_bom_b_x + bom_b_mk_x + point.x, 0, width,
					height);
			canvas.drawBitmap(bmp_wires, bmp_bom_b_x + bom_b_mk_x, bmp_bom_b_y
					+ bom_b_mk_y, null);
			// draw fire for the wire 
			canvas.clipRect(0, 0, width, height, Region.Op.REPLACE);
			sp_fire.setPolision(bmp_bom_b_x + bom_b_mk_x + point.x - 32,
					bmp_bom_b_y + bom_b_mk_y + point.y - 32, 1);
			sp_fire.Draw(canvas);
		}
		return bitmap;
	}
	
	//save higher score when user finish
	@Override
	protected void onStop() {
		super.onStop();		
		WelcomeGame.preferences = this.getSharedPreferences(
				WelcomeGame.PRE_NAME_SCORE, 0);
		SharedPreferences.Editor editor = WelcomeGame.preferences.edit();
		Log.d(LOG, "WelcomeGame.easyNum = " + WelcomeGame.easyNum
				+ "\nWelcomeGame.mediumNum = " + WelcomeGame.mediumNum
				+ "\nWelcomeGame.hardNum = " + WelcomeGame.hardNum);
		editor.putInt("easyNum", WelcomeGame.easyNum);
		editor.putInt("mediumNum", WelcomeGame.mediumNum);
		editor.putInt("hardNum", WelcomeGame.hardNum);
		editor.putInt("UnlimitNum", WelcomeGame.UnlimitNum);
		if(WelcomeGame.isUnlimited)
		{
			editor.putInt("levelPass", WelcomeGame.level);
		}
		editor.commit();
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		WelcomeGame.easyNum = savedInstanceState.getInt("easyNum");
		WelcomeGame.mediumNum = savedInstanceState.getInt("mediumNum");
		WelcomeGame.hardNum = savedInstanceState.getInt("hardNum");
		WelcomeGame.UnlimitNum = savedInstanceState.getInt("UnlimitNum");
		if (WelcomeGame.isUnlimited) {
			WelcomeGame.level = savedInstanceState.getInt("levelPass", 1);		
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(LOG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		outState.putInt("easyNum", WelcomeGame.easyNum);
		outState.putInt("mediumNum", WelcomeGame.mediumNum);
		outState.putInt("hardNum", WelcomeGame.hardNum);
		outState.putInt("UnlimitNum", WelcomeGame.UnlimitNum);
		if(WelcomeGame.isUnlimited)
		{
			outState.putInt("levelPass", WelcomeGame.level);
		}

	}
}
