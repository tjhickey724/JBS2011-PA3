package jbs2011.pa3;

public class Rectangle {
	/**
	 * the x and y position of the center of the rectangle
	 * and its width w
	 */
	public float x,y,w,h;
	public float speed =100; //+(int)(50*Math.random()); // default speed in pixels/second
	
	
	/**
	 * this is true if the rectangle is a target of the game
	 */
	public boolean isTarget=false;
	
	
	/**
	 * create a rectangle with center at (x,y) and whose sides both have width w
	 * Also specify if the square is a "target" element or not
	 * @param x
	 * @param y
	 * @param w
	 * @param isTarget
	 */
	public Rectangle(float x, float y, float w, boolean isTarget) {
       this(x,y,w,w,isTarget);
	}
	/**
	 * create a rectangle as specified
	 * @param x x-coord of lower left corner
	 * @param y y-coord of lower left corner
	 * @param w width
	 * @param h height
	 * @param isTarget - true if the rectangle is a target element
	 */
	public Rectangle(float x, float y, float w, float h, boolean isTarget) {
		this.x=x; this.y=y; this.w=w; this.h = h;
		this.isTarget = isTarget;
	}
	
	
	/** 
	 * return true if (x,y) is within the rectangle
	 * @param x
	 * @param y
	 * @return true if point within rectangle
	 */
	public boolean inside(float x, float y){
		return (Math.abs(x-this.x)<this.w/2) && (Math.abs(y-this.y)<this.w/2);
	}
	
	/**
	 * move the center of the rectangle to (x,y)
	 * @param x
	 * @param y
	 */
	public void move(float x, float y){
		this.x = x; this.y =y;
	}
	
	/**
	 * update moves the object at a given speed from right to left
	 */
	public void update(long dt){
		//this.x -= (this.speed+this.y/2)*dt/1000;
	}

	/**
	 * return a nice string representation of the rectangle
	 */
	  public String toString(){
		  if (this.isTarget)
			  return "target(x,y,w): ("+this.x +", "+this.y+", "+this.w+")\n";
		  else
			  return "sqaure(x,y,w): ("+this.x +", "+this.y+", "+this.w+")\n";

	  }
	
}
