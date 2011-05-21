package com.quasicontrol.live;

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.Display;

public class WPUtil {

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
	//	Log.d("LiveWallpaper", msg);
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
}

