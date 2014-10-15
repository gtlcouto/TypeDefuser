
package com.example.managerdata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {
	private int BMP_ROWS = 1;
	private int BMP_COLUMNS = 1;
	private Bitmap bmp;

	private int width;
	private int height;
	private int x = 0;
	private int y = 0;
	public ArrayList<Sprite_Action> actions = new ArrayList<Sprite_Action>();
	public int idAction = 0;

	public Sprite(Bitmap bmp, int BMP_COLUMNS, int BMP_ROWS) {
		this.bmp = bmp;
		this.BMP_COLUMNS = BMP_COLUMNS;
		this.BMP_ROWS = BMP_ROWS;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
	}

	public void addAction(int index, int[] frames) {
		if (actions.size() == index) {
			Sprite_Action action = new Sprite_Action(frames);
			actions.add(index, action);
		} else if (actions.size() > index) {
			Sprite_Action action = new Sprite_Action(frames);
			actions.set(index, action);
		} else
			Log.i("debug", "Error! add new Action");
	}

	private void update() {
		Sprite_Action action = actions.get(idAction);
		action.nextFrame();
	}

	public void setPolision(int x, int y, int anchor) {
		this.x = x;
		this.y = y;
	}

	public int getIndexFrame() {
		return getIndexFrame(idAction);
	}

	public int getIndexFrame(int index) {
		if (actions.size() > index) {
			Sprite_Action action = actions.get(idAction);
			return action.currentFrame;
		} else
			Log.i("debug", "Error! get Frame");
		return 0;
	}

	public int getNumberFrame() {
		return getNumberFrame(idAction);
	}

	public int getNumberFrame(int index) {
		if (actions.size() > index) {
			Sprite_Action action = actions.get(idAction);
			return action.getNumberFrame();
		} else
			Log.i("debug", "Error! get Frame");
		return 0;
	}

	public void Draw(Canvas canvas) {
		update();
		Sprite_Action action = actions.get(idAction);
		paintFrame(canvas, action.getFrame());
	}

	public void paintFrame(Canvas canvas, int frame) {
		int srcX = (frame % BMP_COLUMNS) * width;
		int srcY = (frame / BMP_COLUMNS) * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bmp, src, dst, null);
	}
}

class Sprite_Action {
	public int currentFrame = 0;
	private int[] frame;

	Sprite_Action() {
		// TODO Auto-generated constructor stub
	}

	public int getNumberFrame() {
		return frame.length;
	}

	public Sprite_Action(int[] frame) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
	}

	public void nextFrame() {
		currentFrame = (currentFrame + 1) % frame.length;
	}

	public void resetFrame() {
		currentFrame = 0;
	}

	public int getFrame() {
		return frame[currentFrame];
	}
}