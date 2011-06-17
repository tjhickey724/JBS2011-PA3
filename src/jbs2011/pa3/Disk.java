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
	double gravity = -100;
	
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
		if (this.y < r)
			this.y = r;
		this.isStatic = false;
	}
	/**
	 * returns true if the point (x,y) is within the disk
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean inside(float x, float y){
		float dist = (x-this.x)*(x-this.x) + (y-this.y)*(y-this.y);
		boolean z = (dist < this.r*this.r);  
		System.out.println("inside: x="+x+" y="+y+" dist="+dist+" z="+z+" d="+this);
		return z;
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
	 * velocity and assuming gravity is working on the disk and it has mass of 1
	 * kg
	 * 
	 * @param t
	 */
	public void update(long dt) {
		vy += (dt / 1000.0) * gravity;
		x += vx * (dt / 1000.0);
		y += vy * (dt / 1000.0);

		if (y <= r) {
			vx = 0;
			vy = 0;
			y = r;
		}

	}

	/**
	 * returns true if the disk intersects the square
	 * @param s
	 * @return
	 */
	public boolean intersects(Square s) {
		return ((Math.abs(this.x - s.x) < this.r + (s.w) / 2) 
				&& (Math.abs(this.y - s.y) < this.r + (s.w) / 2));
	}

	/**
	 * returns true if the current disk intersect the disk d
	 * @param d
	 * @return
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
