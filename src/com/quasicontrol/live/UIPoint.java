package com.quasicontrol.live;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public abstract class UIPoint extends Point{
	
	public UIPoint(int x, int y)
	{
		super(x,y);		
	}
	public abstract void draw(Canvas c, Paint p);
	
	public static double distance(UIPoint p1, UIPoint p2){
		return Math.sqrt(Math.pow(p1.x - p2.x,2) + Math.pow(p1.y - p2.y,2));
	}
	public void rotated(Rect rOld, Rect rNew){
		int dx = rOld.right-rOld.left;
		float xPer = (float)(x - rOld.left)/(float)dx; // compute the percentage distance
		
		int dy = rOld.bottom-rOld.top;
		float yPer = (float)(y-rOld.top)/(float)dy;
		
		dx = rNew.right-rNew.left;
		this.x = (int)(xPer * dx) + rNew.left;
		
		dy = rNew.bottom-rNew.top;
		this.y = (int)(yPer * dy) + rNew.top;
	}
}

