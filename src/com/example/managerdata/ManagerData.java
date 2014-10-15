
package com.example.managerdata;

import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ManagerData {
	static AssetManager assetManager = null;
	
	public static void init(AssetManager assetManager) {
		ManagerData.assetManager = assetManager;
	}

	//load image form folder Assets
	public static Bitmap LoadImgFromAssets(String fileName) {
		Bitmap bmp_re = null;

		try {
			InputStream is = null;
			is = assetManager.open(fileName);
			bmp_re = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return bmp_re;
	}
}
