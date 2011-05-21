package com.quasicontrol.live;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class TurningPoint extends Point {

	public boolean up = true;
	public boolean down = true;
	public boolean left = true;
	public boolean right = true;
	
	public TurningPoint(int x, int y)
	{
		super(x,y);
	}
}
