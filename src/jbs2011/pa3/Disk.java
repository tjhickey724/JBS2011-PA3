package jbs2011.pa3;

/**
 * This represents a Disk which is controlled by gravity but which can be
 * caught and flicked by a user.
 * @author tim
 *
 */
public class Disk {

	final static String TAG = "pa3-disk";
	
	/**
	 * the location of the disk (x,y) and its radius (r)
	 */
	public float x, y, r;
	
	/**
	 * the velocity of the disk in the x and y directions
	 */
	public float vx, vy;
	
	/**
	 * the gravitational constant for the game
	 */
	float gravity = -100;
	
	public boolean weightless = false;
	
	/**
	 * true if the disk is static, i.e. not subject to gravity
	 */
	public boolean isStatic;

	/**
	 * creates a new disk with center (x,y) and radius r
	 * @param x
	 * @param y
	 * @param r
	 */
	public Disk(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.vx = 0;
		this.vy = 0;
		//if (this.y < r)
		//	this.y = r;
		this.isStatic = false;
	}
	/**
	 * returns true if the point (x,y) is within the disk
	 * @param x
	 * @param y
	 * @return true if point inside disk
	 */
	public boolean inside(float x, float y){
		float dist2 = (x-this.x)*(x-this.x) + (y-this.y)*(y-this.y);
		boolean z = (dist2 < this.r*this.r);  
		return z;
	}
	
	/**
	 * returns the distance between the point x,y and the center of the disk
	 * @param x
	 * @param y
	 * @return dist
	 */
	public float dist(float x, float y) {
		float dist = (float) Math.sqrt((x-this.x)*(x-this.x) + (y-this.y)*(y-this.y));
		return dist;
	}
	/**
	 * returns true if the point (x,y) is within d pixels of the disk
	 * @param x
	 * @param y
	 * @param d
	 * @return true if point is near disk
	 */
	public boolean near(float x, float y,float d){
		return this.dist(x,y)<d;
	}
	
	/**
	 * moves the center of the disk to (x,y)
	 * @param x
	 * @param y
	 */
	public void move(float x, float y){
		this.x = x; this.y =y;
	}

	/**
	 * updates the position of disk d after t milliseconds using its position and
	 * velocity. If the weightless flag is true then gravity is zero for this disk.
	 * 
	 * @param dt
	 */
	public void update(long dt) {
	//	float localGravity = weightless?0:gravity;
	//	vy += (dt / 1000.0) * gravity;  //localGravity;
		float drag =500f;
		float stopSpeed = 150;
		vx += -1*Math.signum(vx)*drag*(dt/1000f);
		vy += -1*Math.signum(vy)*drag*(dt/1000f);
		x += vx * (dt / 1000.0);
		y += vy * (dt / 1000.0);


	}

	/**
	 * returns true if the disk intersects the square
	 * @param s
	 * @return true if the disk intersects the square
	 */
	public boolean intersects(Rectangle s) {
		return ((Math.abs(this.x - s.x) < this.r + (s.w) / 2) 
				&& (Math.abs(this.y - s.y) < this.r + (s.h) / 2));
	}
	private Disk initialHit(Rectangle s) {
		//this is the position of the disk as it just hit the Rectangle!
		// calculate the time we need to go back to when it hit the edges (top and also side)
		float tx1,tx2,ty1,ty2;
		if (this.vx != 0) {
		 tx1 =  (this.x + this.r - (s.x-s.w/2))/this.vx; // hit left
		 tx2 =   (this.x - this.r - (s.x+s.w/2))/this.vx; // hit right
		} else {tx1= tx2 = -1f;}
		if (this.vy!= 0) {
		 ty1 =  (this.y + this.r - (s.y-s.h/2))/this.vy; // hit bottom
		 ty2 =  (this.y - this.r - (s.y+s.h/2))/this.vy; // hit top
		} else {ty1=ty2= -1f;}
		// find the smallest (in absolute value) negative value to find first side hit
		float mint=1e10f;
		if (tx1 >= 0) {mint=tx1;}
		else if (tx2 >= 0) {mint=tx2;}
		else if (ty1 >= 0) {mint = ty1;}
		else if (ty2 >=0) {mint = ty2;}
		int hitType=0;
		
		if ((tx1>=0)&&(tx1<=mint)) {
			hitType=1; mint=tx1;
		}
		if ((tx2>=0)&& (tx2<=mint)) {
			hitType=2; mint = tx2;
		}
		if ((ty1>=0)&&(ty1<=mint)){
			hitType=3; mint=ty1;
		}
		if ((ty2>=0)&&(ty2<=mint)) {
			hitType=4; mint=ty2;
		}

	    float newx = this.x - mint*this.vx;
	    float newy = this.y - mint*this.vy;
        Disk d = new Disk(newx,newy, this.r);
        d.vx=this.vx; d.vy=this.vy;
		return d;
	}
	
	public void moveInside(float w, float h) {
		if (this.x<0) this.x=0.05f*w;
		if (this.x>w) this.x=0.95f*w;
		if (this.y<0) this.y=0.05f*w;
		if (this.y>h) this.y=0.95f*h;
	}
	
	/**
	 * if the disk intersects the square then bounce it off the square
	 * @param s
	 */
	public boolean bounceOff(Rectangle s) {
       if (!this.intersects(s)) return false;
       // the disk has probably interpenetrated the rectangle
       // so we need to back off (go back in time) to where they just touch
       // call that new disk dBack. We can then easily calculate the new
       // disk position
       Disk dBack = initialHit(s);
       if (Math.abs(dBack.y-s.y)< s.h) {
    	   // the disk is hitting from the side
    	   // so adjust its position post bounce and its velocity
    	   float newx = dBack.x - (this.x-dBack.x);
    	   this.x = newx;
    	   this.vx = - this.vx;
       } else if (Math.abs(dBack.x-s.x)< s.w){
    	   // the disk is hitting from the top or bottom
    	   float newy = dBack.y - (this.y-dBack.y);
    	   this.y = newy;
    	   this.vy = -this.vy;
       } else {
    	   // the disk is hitting the corner
    	   // so calculate the "tangent" and use that as the reflecting surface
    	   // for now we cheat and just bounce it straight back!
    	   this.vx = -this.vx;
    	   this.vy= - this.vy;
    	   this.x = 2*this.x-dBack.x;
    	   this.y = 2*this.y-dBack.y;
       }
       return true;
	}

	/**
	 * returns true if the current disk intersect the disk d
	 * @param d
	 * @return true if the current disk intersect the disk d
	 */
	public boolean intersects(Disk d) {
		return ((Math.pow(this.x - d.x, 2.0) + Math.pow(this.y - d.y, 2)) < Math
				.pow(this.r + d.r, 2));
	}

	/** 
	 * return a nice string representation of the disks position and radius
	 */
	public String toString() {
		return "disk(x,y,r): (" + this.x + ", " + this.y + ", " + this.r + ", "
				+ this.isStatic + ")\n";
	}

}
