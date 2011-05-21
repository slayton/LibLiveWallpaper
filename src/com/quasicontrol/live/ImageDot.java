package com.quasicontrol.live;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ImageDot extends UIPoint{
	protected Bitmap image;
	
	ImageDot(int x, int y, Bitmap image){
		super(x,y);
		this.image = image;
	}

	@Override
	public void draw(Canvas c, Paint p) {
		// TODO Auto-generated method stub
		
	}
}
