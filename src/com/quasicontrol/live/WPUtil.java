package com.quasicontrol.live;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.view.Display;

public class WPUtil {

	public static final String MONSTER_FILE_PATH = "pacdroid_monster_image.png";
	public static final String PACDROID_FILE_PATH = "pacdroid_image";
	public static final int IMAGE_SIZE_X = 22;
	public static final int IMAGE_SIZE_Y = 22;
	
	// list of screen offsets for each display type
	protected static final int LDPI_T=28;
	protected static final int LDPI_B=46;
	protected static final int LDPI_L=6;
	protected static final int LDPI_R=6;
	
	protected static final int MDPI_T=36;
	protected static final int MDPI_B=58;
	protected static final int MDPI_L=10;
	protected static final int MDPI_R=10;

	protected static final int HDPI_T=52;
	protected static final int HDPI_B=90;
	protected static final int HDPI_L=16;
	protected static final int HDPI_R=16;
	
	// create rect objects that store the boundary offsets for each display type
	protected static Rect LDPI_BOUNDS = new Rect(LDPI_L,LDPI_T,LDPI_R, LDPI_B);
	protected static Rect MDPI_BOUNDS = new Rect(MDPI_L,MDPI_T,MDPI_R, MDPI_B);
	protected static Rect HDPI_BOUNDS = new Rect(HDPI_L,HDPI_T,HDPI_R, HDPI_B);
		
	
	public static DisplayMetrics getDisplayMetrics(WallpaperService wps){
		
		Display display = ((WindowManager) wps.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    
	    DisplayMetrics metrics = new DisplayMetrics();
	    display.getMetrics(metrics);
	    
	    int width = display.getWidth(); 
	    int height = display.getHeight();
	    WPUtil.logD("Loaded Disply metrics:".concat(Integer.toString(width)).concat("x").concat(Integer.toString(height)));
	  
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


	// convert the offsets rectangles into the rectangles representing the actuall screen bounds
	protected static Rect getBounds(Display d, Rect r) 
	{
		return new Rect(r.left, r.top, d.getWidth()-r.right, d.getHeight()-r.bottom);
	}
	
	public static Rect getDrawingBounds(Display d, DisplayMetrics m){
		Rect r;
		//load the approrpiate offset
		switch(m.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			r = WPUtil.LDPI_BOUNDS;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			r = WPUtil.MDPI_BOUNDS;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			r = WPUtil.HDPI_BOUNDS;
			break;
		default: //DENSITY_DEFAULT
			r = WPUtil.MDPI_BOUNDS;
		}
		
		WPUtil.logD("Rectangle:" + r.toShortString());
		switch (d.getOrientation()){
		case Surface.ROTATION_0:
			break;
		case Surface.ROTATION_90:
			r = rectRotate90(r);
			break;
		case Surface.ROTATION_180:
			//r =  rectRotate180(r);
			break;
		case Surface.ROTATION_270:
			r = rectRotate270(r);
			break;
		}
		r = getBounds(d, r);
		WPUtil.logD("Returning bounds:" + r.toShortString());

		return r;	
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
	public static Rect rectRotate90(Rect in){
		WPUtil.logD("calcuating 90 degree rotation");
		return new Rect(in.left, in.top, in.bottom, in.right);
		//l t r b
	}
	public static Rect rectRotate270(Rect in){
		WPUtil.logD("calcuating 180 degree rotation");
		return new Rect(in.left, in.top, in.bottom, in.right);
	}
	public static ArrayList<String> getInstalledThemes(Context c, ArrayList<String> themes){
		ArrayList<String> list = new ArrayList<String>();
		PackageManager p = c.getPackageManager();
		int nThemes = themes.size();
		for (int i=0; i<nThemes; i++)
		{
			try{
				p.getPackageInfo(themes.get(i), PackageManager.GET_ACTIVITIES);
				list.add(themes.get(i));
				WPUtil.logD("Theme package IS  installed:" + themes.get(i));
			}
			catch (Exception e)
			{
				WPUtil.logD("Theme package NOT installed:" + themes.get(i));
			}
		}
		WPUtil.logD("Detected " + Integer.toString(list.size()) + " installed themes!");
			
		return list;
	}
	public static boolean isThemeInstalled(Context c, String theme){
		try{
			c.getPackageManager().getPackageInfo(theme, PackageManager.GET_ACTIVITIES);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
	
	/*public static Rect rectRotate270(Rect in){
		WPUtil.logD("calcuating 270 degree rotation");
		return new Rect(in.bottom, in.left, in.top, in.right);
	}*/
	
	public static String getThemeDisplayName(String theme){
		if (theme.equalsIgnoreCase("com.quasicontrol.pacdroidlive"))
			return "PacDroid Theme";
		if (theme.equalsIgnoreCase("com.quasicontrol.pacdroidlive.classic"))
			return "Classic Pac Theme";
		if (theme.equalsIgnoreCase("com.quasicontrol.pacdroidlive.elfhunter"))
			return "Legend of Zelda Theme";
		else
			return "Unknown Theme";
	}
}

