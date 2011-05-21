package com.quasicontrol.live;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public abstract class UIPoint extends Point{
	
	public UIPoint(int x, int y)
	{
		super(x,y);		
	}
	public abstract void draw(Canvas c, Paint p);
	
	public static double distance(UIPoint p1, UIPoint p2){
		return Math.sqrt(Math.pow(p1.x - p2.x,2) + Math.pow(p1.y - p2.y,2));
	}
}

