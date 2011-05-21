package com.quasicontrol.live;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class WallBlock extends UIPoint {

	protected int w;
	protected int h;
	protected int r;
	
	public WallBlock(int x, int y, int w, int h,int r)
	{
		super(x,y);
		this.w = w; 
		this.h = h;
		this.r = r;
	}
	public void draw(Canvas c, Paint p) {
		c.drawRoundRect(new RectF(x-w/2, y-h/2, x+w/2, y+h/2), (float)r, (float)r, p);
	}

}
