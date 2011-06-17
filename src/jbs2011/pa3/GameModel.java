package jbs2011.pa3;

import java.util.ArrayList;

/**
 * This provides a model for how the game works
 * with hooks for user interaction.
 * @author tim
 *
 */
public class GameModel  {
	
	public ArrayList<Disk> disks = new ArrayList<Disk>();
	public ArrayList<Square> squares=new ArrayList<Square>();
	public ArrayList<Square> targets=new ArrayList<Square>();
	private long lastTime;
	private long currTime;
	private long dt;
	private boolean firstEval=true;
	
	/**
	 * this is set to true when the level is completed
	 */
	public boolean levelOver=false;
	
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
	 * @return
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
	 * @return
	 */
	public Disk addDisk(float x, float y, float r){
		Disk d = new Disk(x,y,r);
		return addDisk(d);
	}
	
	/**
	 * add a square to the model
	 * @param s
	 * @return
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
	 * @return
	 */
	public Square addSquare(float x, float y, float w){
		Square s = new Square(x,y,w,false);
		return addSquare(s);
	}
	
	/** 
	 * add a target to the model
	 * @param t
	 * @return
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
	 * @return
	 */
	public Square addTarget(float x, float y, float w) {
		return addTarget(new Square(x,y,w,true));
	}

	/**
	 * returns a disk touching (x,y) if one exists and return null else
	 * @param x
	 * @param y
	 * @return
	 */
	public Disk touchingDisk(float x, float y){
		for (Disk d:disks)
			if (d.inside(x,y)) return d;
		return null;
	}
	
	/**
	 * return a non-target square touching (x,y) if one exists ar return null
	 * @param x
	 * @param y
	 * @return
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
	 * @return
	 */
	public Square touchingTarget(float x, float y){
		for (Square s:targets)
			if (s.inside(x,y)) return s;
		return null;
	}
	
	
	/**
	 * reseting model remove all squares and disks from playing surface, and reset the game status
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
	 *
	 */
	public void updateGame(long now) {
		currTime = now;
		if (firstEval){
			dt=0;
			firstEval=false;
		} else {
			dt = currTime - lastTime;
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
			return;
		}
		
		// if there are non-static disks, then we update their positions
		lastTime = currTime;
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
						levelOver=true;
						userWon=true;
					}

					
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
