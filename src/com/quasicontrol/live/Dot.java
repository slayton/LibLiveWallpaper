package com.quasicontrol.live;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Dot extends UIPoint {

	public int r=3;
	public boolean enabled = true;
	
	public Dot(int x, int y, int r)
	{
		super(x,y);
		this.r = r;
	}
	public Dot(int x, int y)
	{
		super(x,y);
	}
	
	public void draw(final Canvas c, final Paint mPaint)
	{
		if (this.enabled)
			c.drawCircle(x, y, r, mPaint);
	}
	public void disableDot(){
		this.enabled = false;
	}
	public void enableDot(){
		this.enabled = true;
	}
}
