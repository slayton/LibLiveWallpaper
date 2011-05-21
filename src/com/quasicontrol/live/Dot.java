package com.quasicontrol.live;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Dot extends UIPoint {

	public int r=3;
	boolean enabled = true;
	
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
		mPaint.setStyle(Style.FILL);
		Paint p = new Paint(mPaint);
		p.setStyle(Style.FILL);
		if (this.enabled)
			c.drawCircle(x, y, r, p);
		
	}
	public void disableDot(){
		this.enabled = false;
	}
	public void enableDot(){
		this.enabled = true;
	}
}
