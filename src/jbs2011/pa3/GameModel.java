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
	
	public boolean levelOver=false;
	
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
	 * reseting model remove all squares and disks from playing surface
	 */
	
	public void resetGame(){
		disks.clear();
		squares.clear();
		targets.clear();
	}
	
	/**
	 * this iterates through all disks and changes their position
	 * based on their current velocity (updated via gravity). If
	 * a disk intersects a static object (disk or square), it becomes
	 * static itself.
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
	//	System.out.println("now="+now+" and dt = "+dt);
		lastTime = currTime;
		for (Disk d:disks){
			if (! d.isStatic){
				d.update(dt);
				for (Square s:squares) 
					if (d.intersects(s)) 
						d.isStatic=true;
					
				for (Disk d1:disks) 
					if (d.intersects(d1) && d1.isStatic)
						d.isStatic = true;	
				
				for (Square s:targets)
					if (d.intersects(s))
						levelOver=true;
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
