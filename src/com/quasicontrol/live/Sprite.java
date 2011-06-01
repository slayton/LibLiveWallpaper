package com.quasicontrol.live;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;

public class Sprite extends UIPoint {
	protected Rect arenaBounds;
	public int state;
	//Resources r;
	
	public static final int MOVING_UP = 0;
	public static final int MOVING_DOWN = 1;
	public static final int MOVING_LEFT = 2;
	public static final int MOVING_RIGHT = 3;
	public static final int IS_SUPER = 4;
	public static final int IS_DEAD = 5;

	public static final long MIN_ELAPSED_TIME = 30;
	
	public static final int AI_RANDOM = 0;
	public static final int AI_HUNT = 1;
	public static final int AI_AVOID = 2;
	
	
	protected int dir=-1;
	protected int vel = 3;
	protected int dx = 0;
	protected int dy = 0;
	
	protected Bitmap image;
	protected Matrix m;
	protected long tLastMove =0 ;

	protected ArrayList<TurningPoint> tp;
	protected int tpSize;
	protected TurningPoint tpMove = null;

	protected ArrayList<Sprite> targets;
	protected int nTargets;
	protected Sprite tempTarget;
	
	protected ArrayList<Bitmap> imageArray;
	protected int nImageArray;
	protected Rect imageBounds;
	
	protected Bitmap drawMe;
	protected int imgIdx;
	
	protected int aiType=0;
	protected Random rand;
	
	public boolean enabled=true;
	public int tag;
	
	protected int turnTimeOut = 5;
	protected int TURN_TIMEOUT = 5;
	
	public Sprite(int x, int y, Rect arenaBounds, ArrayList<Bitmap> imageList)
	{
		super(x,y);

		//WPUtil.logD("imageList is null:" + Boolean.toString(imageList==null));
		this.state = Sprite.MOVING_UP;
		this.arenaBounds = arenaBounds;
		this.imageArray = imageList;

		//WPUtil.logD("Actor Created at:".concat(Integer.toString(x)).concat("x").concat(Integer.toString(y)));
		dy = 0;
		dx = 0;
		m = new Matrix();
        tLastMove = SystemClock.elapsedRealtime();
        aiType = 0;
        rand = new Random();
        imgIdx = 0;
		this.image = imageArray.get(imgIdx);
		this.nImageArray = imageArray.size();

	}
	public Sprite(int x, int y, Rect arenaBounds, ArrayList<Bitmap> imageList, ArrayList<Sprite> targets)
	{
		this(x, y, arenaBounds, imageList);
		this.targets = targets;
		this.nTargets = targets.size();
	}
	public void setAiType(int i)
	{
		switch (i){
		case Sprite.AI_RANDOM:
			this.aiType = i;
			//WPUtil.logD("setting ai to RANDOM");
			break;
		case Sprite.AI_HUNT:
			this.aiType = i;
			WPUtil.logD("setting ai to HUNT");
			break;
		case Sprite.AI_AVOID:
			this.aiType = i;
			//WPUtil.logD("setting ai to AVOID");
			break;
		default:
			this.aiType =i;
		}
	}
	public void setTargetActors(ArrayList<Sprite> targets)
	{
		this.targets = targets;
	}
	public void setTurningPoints(ArrayList<TurningPoint> tp)
	{
		this.tp = tp;
		this.tpSize = tp.size();
	}
	
	public void draw(final Canvas c, final Paint mPaint)
	{	
		//this.move();
		drawMe = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);
		c.drawBitmap(drawMe, this.x-image.getWidth()/2, this.y-image.getHeight()/2, mPaint);
	}
	
	// This is the core of the movement, replace this with the AI as needed
	public void move()
	{
		int tpSize = tp.size();
		for (int i=0; i<tpSize; i++)
		{
			tpMove = tp.get(i);
         	if (this.detectCollision(tpMove))
         		this.turnAtPoint(tpMove);
		}
		this.x += dx;
		this.y += dy;
		turnTimeOut -=1;
	}
	
	public void setEnabled(boolean newVal){
		this.enabled = newVal;
	}
	
	public boolean detectCollision(Point target)
	{
		return (Math.abs(this.x - target.x)<=Math.abs(this.dx) && Math.abs(this.y - target.y)<=Math.abs(this.dy));
	}
	public boolean detectCollision(Point target, int dist)
	{
		return (Math.abs(this.x - target.x)<=dist && Math.abs(this.y - target.y)<=dist);
	}
	public boolean detectLinearCollision(Point target, int dist)
	{
		//return (this.detectCollision(target, dist) && (Math.abs(this.x-target.x)<dist || Math.abs(this.y-target.y)<dist ));
		return ((Math.abs(this.x-target.x)<dist && Math.abs(this.y-target.y)<dist ));
	}
	public void turnAtPoint(TurningPoint tp)
	{	
		if (this.targets==null)
			randomTurn(tp);
		switch (this.aiType){
		case Sprite.AI_RANDOM:
			randomTurn(tp);
			break;
		case Sprite.AI_HUNT:
			huntTurn(tp);
			break;
		case Sprite.AI_AVOID:
			avoidTurn(tp);
			break;
		default:
			randomTurn(tp);
		}
		this.x = tp.x + (int) Math.signum(dx);
		this.y = tp.y + (int) Math.signum(dy);
		
	}
	protected void randomTurn(TurningPoint tp){
	//	WPUtil.logD("init random turn");
		if (turnTimeOut>=1)
			return;
			
		boolean validTurn = false;
		do{
			int val =rand.nextInt(100);
						
			if (val<=25 && tp.up && this.dir!=Sprite.MOVING_DOWN){
				//WPUtil.logD("TAG:" + Integer.toString(tag) + " val:" + Integer.toString(val) + " tp.up:" + Boolean.toString(tp.up) + " moving down:" + Boolean.toString(this.dir==Sprite.MOVING_DOWN)+ " curDir:" + Integer.toString(this.dir));
				validTurn = true;
				turnUp();
			}
			else if (val>25 && val<=50 && tp.down && dir!=Sprite.MOVING_UP){
				//WPUtil.logD("TAG:" + Integer.toString(tag) + " val:" + Integer.toString(val) + " tp.down:" + Boolean.toString(tp.down) + " moving up:" + Boolean.toString(this.dir==Sprite.MOVING_UP)+ " curDir:" + Integer.toString(this.dir));
				validTurn = true;
				turnDown();
			}
			else if (val>50 && val<=75 && tp.left && dir!=Sprite.MOVING_RIGHT){
				//WPUtil.logD("TAG:" + Integer.toString(tag) + " val:" + Integer.toString(val) + " tp.left:" + Boolean.toString(tp.left) + " moving right:" + Boolean.toString(this.dir==Sprite.MOVING_RIGHT)+ " curDir:" + Integer.toString(this.dir));
				validTurn = true;
				turnLeft();
			}
			else if (val>75 && tp.right && dir!=Sprite.MOVING_LEFT){
				//WPUtil.logD("TAG:" + Integer.toString(tag) + " val:" + Integer.toString(val) + " tp.right:" + Boolean.toString(tp.right) + " moving left:" + Boolean.toString(this.dir==Sprite.MOVING_LEFT)+ " curDir:" + Integer.toString(this.dir));
				validTurn = true;
				turnRight();
			}
			//WPUtil.logD("Rand:".concat(Double.toString(val)).concat(" Dir").concat(Integer.toString(dir)) + " valid" + Boolean.toString(validTurn));
		}while(!validTurn);		
	}
	protected void huntTurn(TurningPoint tp){

		//WPUtil.logD("hunting");
		if (rand.nextInt(100)<10)
		{
			randomTurn(tp);
			return;
		}
		
		int tIdx = 0;
		int minDist = Integer.MAX_VALUE;
		int dist;
		for (int i=0; i<targets.size(); i++)
		{
			if (!targets.get(i).enabled)
				continue;
			dist = (int)UIPoint.distance(this, targets.get(i));
			if (dist<minDist){
				minDist = dist;
				tIdx = i;
			}
			
		}
		//boolean validTurn = false;
		int xDist = this.x - targets.get(tIdx).x;
		int yDist = this.y - targets.get(tIdx).y;
		
		if (Math.abs(xDist)>=Math.abs(yDist)) // minimize the larger distance
			if (xDist>0)
				if (tp.left)
					turnLeft();
				else
					randomTurn(tp);
			else
				if(tp.right)
					turnRight();
				else
					randomTurn(tp);
		else
			if (yDist>0)
				if (tp.up)
					turnUp();
				else
					randomTurn(tp);
			else
				if (tp.down)
					turnDown();
				else
					randomTurn(tp);
	}
	protected void avoidTurn(TurningPoint tp){
		
		if (rand.nextInt(100)<25){
			randomTurn(tp);
			return;
		}
		
		int tIdx = 0;
		int minDist = Integer.MAX_VALUE;
		int dist;
		for (int i=0; i<nTargets; i++)
		{
			tempTarget = targets.get(i);
			dist = (int)UIPoint.distance(this, tempTarget);
			if (dist<minDist){
				minDist = dist;
				tIdx = i;
			}
		}
		//boolean validTurn = false;
		int xDist = this.x - targets.get(tIdx).x;
		int yDist = this.y - targets.get(tIdx).y;
		
		if (Math.abs(xDist)>Math.abs(yDist)) //maxmize the smaller distance
			if (xDist<0)
				if (tp.left)
					turnLeft();
				else
					randomTurn(tp);
			else
				if(tp.right)
					turnRight();
				else
					randomTurn(tp);
		else
			if (yDist<0)
				if (tp.up)
					turnUp();
				else
					randomTurn(tp);
			else
				if (tp.down)
					turnDown();
				else
					randomTurn(tp);
		
	}
	public void turnUp(){
	//	if (this.dir==Sprite.MOVING_DOWN)
	//		WPUtil.logD("TAG:" + Integer.toString(tag) + " Turning around: UP");
		dx = 0;
		dy = - vel;
		m.reset();
		m.postRotate(-90);
		dir = Sprite.MOVING_UP;
		turnTimeOut = TURN_TIMEOUT;
	}
	public void turnDown(){
	//	if (this.dir==Sprite.MOVING_UP)
	//		WPUtil.logD("TAG:" + Integer.toString(tag) + " Turning around: DOWN");
		dx = 0;
		dy = vel;
		m.reset();
		m.postRotate(90);
		dir = Sprite.MOVING_DOWN;
		turnTimeOut = TURN_TIMEOUT;

	}
	public void turnLeft(){
	//	if (this.dir==Sprite.MOVING_RIGHT)
	//		WPUtil.logD("TAG:" + Integer.toString(tag) + " Turning around: LEFT");
		dx = -vel;
		dy = 0;
		m.reset();
		m.postRotate(180);
		dir = Sprite.MOVING_LEFT;
		turnTimeOut = TURN_TIMEOUT;

	}
	public void turnRight(){
		//if (this.dir==Sprite.MOVING_LEFT)
			//WPUtil.logD("TAG:" + Integer.toString(tag) + " Turning around: RIGHT");
		dx = vel;
		dy = 0;
		m.reset();
		dir = Sprite.MOVING_RIGHT;
		turnTimeOut = TURN_TIMEOUT;
	}
	public int getDir(){
		return dir;
	}
	public void setDir(int dir){
		//WPUtil.logD("sprit.setDir() called");
		switch (dir){
		case(Sprite.MOVING_DOWN):
			turnDown();break;
		case(Sprite.MOVING_UP):
			turnUp();break;
		case(Sprite.MOVING_LEFT):
			turnLeft();break;
		case(Sprite.MOVING_RIGHT):
			turnRight();break;
		}
	}
	
}
