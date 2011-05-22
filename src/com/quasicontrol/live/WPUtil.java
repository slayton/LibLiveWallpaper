package com.quasicontrol.live;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.Display;

public class WPUtil {

	public static final String MONSTER_FILE_PATH = "pacdroid_monster_image.png";
	public static final String PACDROID_FILE_PATH = "pacdroid_image";
	public static final int IMAGE_SIZE_X = 22;
	public static final int IMAGE_SIZE_Y =22;
	public static DisplayMetrics getDisplayMetrics(WallpaperService wps){
		
		Display display = ((WindowManager) wps.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    int width = display.getWidth(); 
	    int height = display.getHeight();
	    WPUtil.logD("Loaded device Width and Height ".concat(Integer.toString(width)).concat("x").concat(Integer.toString(height)));
	    
	    DisplayMetrics metrics = new DisplayMetrics();
	    display.getMetrics(metrics);
	    
	    switch(metrics.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			WPUtil.logD("Display is --> DENSITY_LOW");
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			WPUtil.logD("Display is --> DENSITY_MEDIUM");
			break;
		case DisplayMetrics.DENSITY_HIGH:
			WPUtil.logD("Display is --> DENSITY_HIGH");
			break;
		default: //DENSITY_DEFAULT
			WPUtil.logD("Display is --> DENSITY_DEFAULT");
	    }	
	    return metrics;
	     
	}
	public static void logW(String msg)
	{
		//Log.w("LiveWallpaper", msg);
	}
	public static void logi(String msg)
	{
		//Log.i("LiveWallpaper", msg);
	}
	public static void logD(String msg)
	{
		Log.d("LiveWallpaper", msg);
	}
	public static int bottomOffset(DisplayMetrics m){
		switch(m.densityDpi){
			case DisplayMetrics.DENSITY_LOW:
				return 46;
			case DisplayMetrics.DENSITY_MEDIUM:
				return 58;
			case DisplayMetrics.DENSITY_HIGH:
				return 90;
			default: //DENSITY_DEFAULT
				return 46;
		}	
	}
	public static int topOffset(DisplayMetrics m){
		switch(m.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			return 28;
		case DisplayMetrics.DENSITY_MEDIUM:
			return 36;
		case DisplayMetrics.DENSITY_HIGH:
			return 52;
		default: // DENSITY_DEFAULT
			return 36;
		}	
	}
	public static int leftOffset(DisplayMetrics m){
		switch(m.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			return 6;
		case DisplayMetrics.DENSITY_MEDIUM:
			return 10;
		case DisplayMetrics.DENSITY_HIGH:
			return 16;
		default: // DENSITY_DEFAULT
			return 10;
		}	
	}
	public static int rightOffset(DisplayMetrics m){
		switch(m.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			return 6;
		case DisplayMetrics.DENSITY_MEDIUM:
			return 10;
		case DisplayMetrics.DENSITY_HIGH:
			return 16;
		default: // DENSITY_DEFAULT
			return 10;
		}	
	}
	public static Bitmap loadPublicImage(String path)
	{
		return BitmapFactory.decodeFile(path);
	}
	public static Bitmap loadPrivateImage(Context c, String path)
	{
		Bitmap b = null;
		try{
			FileInputStream fi = c.openFileInput(path);
			b = BitmapFactory.decodeStream(fi);
			WPUtil.logD("Read bitmap:" + path);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return b;
		
	}
	public static Bitmap resizeBitmap(Bitmap b, int w, int h)
	{
			WPUtil.logD("Resized Bitmap");
			int width = b.getWidth();
	        int height = b.getHeight();

	        float scaleWidth = ((float) w) / width;
	        float scaleHeight = ((float) h) / height;

	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);

	        Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0, width, height,
	                matrix, true);

	        return resizedBitmap;
	}
	public static void savePrivateBitmap(Context c, String path, Bitmap b){
		try {
				FileOutputStream out = c.openFileOutput(path, Context.MODE_PRIVATE);
				b.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.close();
				WPUtil.logD("Saving bitmap:"+path);
		} catch (Exception e) {
		       e.printStackTrace();
		}
	}
}

