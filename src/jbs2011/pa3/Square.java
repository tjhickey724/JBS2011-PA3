package jbs2011.pa3;

public class Square {
	/**
	 * the x and y position of the center of the square
	 * and its width w
	 */
	public float x,y,w;
	
	
	/**
	 * this is true if the Square is a target of the game
	 */
	public boolean isTarget;
	
	
	/**
	 * create a square with center at (x,y) and whose sides have width 2
	 * Also specify if the square is a "target" element or not
	 * @param x
	 * @param y
	 * @param w
	 * @param isTarget
	 */
	public Square(float x, float y, float w, boolean isTarget) {
		this.x=x; this.y=y; this.w=w;
		this.isTarget = isTarget;
	}
	
	/** 
	 * return true if (x,y) is within the square
	 * @param x
	 * @param y
	 * @return true if point within square
	 */
	public boolean inside(float x, float y){
		return (Math.abs(x-this.x)<this.w/2) && (Math.abs(y-this.y)<this.w/2);
	}
	
	/**
	 * move the center of the square to (x,y)
	 * @param x
	 * @param y
	 */
	public void move(float x, float y){
		this.x = x; this.y =y;
	}

	/**
	 * return a nice string representation of the square
	 */
	  public String toString(){
		  if (this.isTarget)
			  return "target(x,y,w): ("+this.x +", "+this.y+", "+this.w+")\n";
		  else
			  return "sqaure(x,y,w): ("+this.x +", "+this.y+", "+this.w+")\n";

	  }
	
}
