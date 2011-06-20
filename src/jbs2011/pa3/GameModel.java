package jbs2011.pa3;

import java.util.ArrayList;


/**
 * This provides a model for how the game works
 * with hooks for user interaction. 
 * The user flings one (or more) disks trying to hit all targets (which disappear when hit)
 * If she hits all targets without hitting any obstacles she wins. Hitting an obstacle ends
 * the game.
 * 
 * @author tim
 *
 */
public class GameModel  {
	/**
	 * list of all active disks in the game
	 */
	public ArrayList<Disk> disks = new ArrayList<Disk>();
	/**
	 * list of all static squares
	 */
	public ArrayList<Square> squares=new ArrayList<Square>();
	/**
	 * list of all target squares
	 */
	public ArrayList<Square> targets=new ArrayList<Square>();
	
	// last time the doDraw method was called
	private long lastTime;
	// current time that the doDraw method is called
	private long currTime;
	// beginning time for the current game 
	private long startTime=0;
	
	/**
	 * length of a game in seconds
	 */
	public float gameLength=15; // seconds
	/**
	 * time remaining in the current game
	 */
	public float timeRemaining;
	/**
	 * number of games won so far
	 */
	public int wins;
	/**
	 * number of games lost so far
	 */
	public int losses;
	
	// time since the last call to doDraw
	private long dt;
	
	// true if this is the first time doDraw has been called since the application was started
	private boolean firstEval=true;
	
	/**
	 * this is set to true when the level is completed
	 */
	public boolean levelOver=true;
	
	/**
	 * this is set to true when the user wins 
	 */
	public boolean userWon = false;
	
	/**
	 * this is set to true when the user loses
	 */
	public boolean userLost = false;
	
	/**
	 * this is the game gravity for all non-weightless, non-static objects
	 */
	public float gravity = -100; 
	
	/**
	 * create an empty GameModel
	 */
	public GameModel(){

	}

	/**
	 * add a disk to the model
	 * @param d
	 * @return d
	 */
	public Disk addDisk(Disk d){
		disks.add(d);
		return d;
	}
	
	/**
	 * create a new disk with center (x,y) and radius r and add it to the model
	 * @param x
	 * @param y
	 * @param r
	 * @return a disk
	 */
	public Disk addDisk(float x, float y, float r){
		Disk d = new Disk(x,y,r);
		return addDisk(d);
	}
	
	/**
	 * add a square to the model
	 * @param s
	 * @return a square
	 */
	public Square addSquare(Square s){
		squares.add(s);
		return s;
	}
	
	/**
	 * create a square and add it to the model
	 * @param x
	 * @param y
	 * @param w
	 * @return a square
	 */
	public Square addSquare(float x, float y, float w){
		Square s = new Square(x,y,w,false);
		return addSquare(s);
	}
	
	/** 
	 * add a target to the model
	 * @param t
	 * @return t
	 */
	public Square addTarget(Square t){
		targets.add(t);
		return t;
	}
	
	/**
	 * create a target and add it to the model
	 * @param x
	 * @param y
	 * @param w
	 * @return a target
	 */
	public Square addTarget(float x, float y, float w) {
		return addTarget(new Square(x,y,w,true));
	}

	/**
	 * returns the disk whose center is closest to (x,y) and within 100 pixels if one exists and returns null else
	 * @param x
	 * @param y
	 * @return a disk
	 */
	public Disk touchingDisk(float x, float y){
		// find the closest disk to (x,y)
		if (disks.size()==0) return null;
		
		Disk closestDisk = disks.get(0);
		float closestDistance = closestDisk.dist(x, y);
		for (Disk d:disks){
			float d1 = d.dist(x, y);
			if (d1 < closestDistance){
				closestDistance = d1;
				closestDisk = d;
			}
		}
		if (closestDistance < 100)
			return closestDisk;
		else
			return null;
		
	}
	
	/**
	 * return a non-target square touching (x,y) if one exists ar return null
	 * @param x
	 * @param y
	 * @return a square
	 */
	public Square touchingSquare(float x, float y){
		for (Square s:squares)
			if (s.inside(x,y)) return s;
		return null;
	}
	
	/**
	 * return a target square touching (x,y) or null if none exists
	 * @param x
	 * @param y
	 * @return a target
	 */
	public Square touchingTarget(float x, float y){
		for (Square s:targets)
			if (s.inside(x,y)) return s;
		return null;
	}
	
	
	/**
	 * reseting model remove all squares and disks from playing surface, and resets the game status
	 */
	
	public void resetGame(){
		disks.clear();
		squares.clear();
		targets.clear();
		levelOver=false;
		userWon=false;
		userLost=false;
	}
	
	/**
	 * this iterates through all disks and changes their position
	 * based on their current velocity (updated via gravity). If
	 * a disk intersects a static object (disk or square), it becomes
	 * static itself. If a disk hits the target the game is over (user won)
	 * and if the disks are all static the game is over (user lost).
	 * Also if the timeRemaining drops below the zero the game is over (user lost).
	 * The number of wins and losses is also calculated here
	 */
	public void updateGame(long now) {
		if (levelOver) return; // don't do anything when the game is not being played ...
		
		currTime = now;
		if (firstEval){
			dt=0;
			firstEval=false;
		} else {
			dt = currTime - lastTime;
		}
		
		// see if the user has run out of time
		timeRemaining = gameLength - (now-startTime)/1000f;
		if (timeRemaining<0){
			levelOver=true; userLost=true;
			losses += 1;
			return;
		}
			
		
		// the non-static disks will have their positions updated
		// so we first find the arraylist of non-static disks
		ArrayList<Disk> activeDisks = new ArrayList<Disk>();
		for (Disk d:disks){
			if (!d.isStatic)
				activeDisks.add(d);
		}
		
		// if all of the disks are static (i.e. frozen), the level is over and the user lost.
		if (activeDisks.size()==0){
			levelOver=true; userLost=true;
			losses += 1;
			return;
		}
		
		// if there are non-static disks, then we update their positions
		lastTime = currTime;
		ArrayList<Square> hitTarget= new ArrayList<Square>();
		ArrayList<Disk> hitDisk = new ArrayList<Disk>();
		
		for (Disk d:activeDisks){
				// let gravity move the object over the time interval dt
				d.update(dt);
				
				// check to see if the disk has hit any squares, if so it become static
				for (Square s:squares) 
					if (d.intersects(s)) 
						d.isStatic=true;
				
				// check to see if the disk has hit any static disks, and make it static if it has
				for (Disk d1:disks) 
					if (d.intersects(d1) && d1.isStatic)
						d.isStatic = true;	
				
				// check to see if the disk has hit any targets, and if so the level is over..

				for (Square s:targets)
					if (d.intersects(s)){
						hitTarget.add(s);
						hitDisk.add(d); 
					}
					
		}
		for (Disk d:hitDisk){
			//disks.remove(d);
		}
		for (Square s:hitTarget)
			targets.remove(s);
		
		if (targets.size()==0) {
			levelOver=true;
			userWon=true;
			wins += 1;
		}

	}
	
	
	/**
	 * return a nice string representation of the game model
	 */
	public String toString(){
		java.lang.StringBuffer s= new StringBuffer();
		s.append("currenttime = "+lastTime + "\n");
		for (Disk d:disks){
			s.append("disk "+d+"\n");
		}
		for (Square sq:squares){
			s.append("square "+sq+"\n");
		}
		for (Square t:targets){
			s.append("target "+t+"\n");
		}
		return s.toString();
	}
	

	public void createLevel(int level){
		createLevel(level,500,500);
	}
	
	public void createLevel(int level, int width, int height){
		startTime = System.currentTimeMillis();
		timeRemaining = gameLength;
		switch (level){
		case 1: // this is a simple level with 3 disks a square and a target, useful for debugging...
			addSquare(150f, 50f, 50f);
			addTarget(180f, 150f, 50f);
			Disk d = addDisk(150f, 500f, 50f);
			addDisk(300f, 500f, 30f);
			addDisk(350f, 500f, 20f);
			addDisk(400f, 500f, 10f);
			d.vx = 10;
			d.vy = 102;
			break;
			
		
		case 2: // this is a fun level with up to 30 visible blocks, 10 targets and 1 disks..
			for (int i=0;i<10; i++){
				this.addSquare((float)Math.random()*width,(float)Math.random()*(height-100)+100,(float)Math.random()*30+40);
			}
			
			for (int i=0; i<10; i++)
				this.addTarget((float)Math.random()*(width-200)+100,(float)Math.random()*(height-100)+100,50);

			for (int i=0;i<2;i++)
				this.addDisk(50f*i,50f,25f);

			break;
	  }
	}
	/**
	 * do a little testing of the model...
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("staring the test method\n");
		GameModel g = new GameModel();
		g.addSquare(0f,1f,2f);
		g.addTarget(0f,3f,2f);
		Disk d = g.addDisk(0f,10f,1f);
		d.vx=1; d.vy=20;
		g.toString();
		for(int i=0; i<200; i++) {
			System.out.println("Level "+i);
			g.updateGame(100L*i);
			System.out.println(g.toString());
		}
		try {
			java.lang.Thread.sleep(100L);
		}
		catch (Exception e){
			System.out.println("Exception: "+e);
		}
		
		
		System.out.println("completing the method");
	}
	
	

}
