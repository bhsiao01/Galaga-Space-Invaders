import java.awt.*;

public class Bullet extends GameObj {
	private boolean fired;
	private int speed;
	private Color color;
	private Invader invader;
	
	public Bullet(int s, Color c) {
		super(0, 0, 0, 0, 3, 5, 400, 400);
		this.fired = false;
		this.speed = s;
		this.color = c;
		this.invader = null;
	}
	
	/**
	 * Creates a bomb to be dropped by an invader
	 * @param x: the x-coordinate of the invader
	 * @param y: the y-coordinate of the invader
	 * @param s: the speed for the bullet
	 */
	public Bullet(int x, int y, int s, boolean f, Invader inv, Color c) {
		super(0, 0, x, y, 3, 5, 400, 400);
		this.fired = f;
		this.speed = s;
		this.color = c;
		this.invader = inv;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
	
	/**
	 * Fires the bullet and checks whether it has hit a border or an invader
	 */
	public void fire() {
		super.move();
		this.setVy(this.speed);
		
		if (((this.speed < 0) && (this.hitWall() == Direction.UP)) || 
			((this.speed > 0) && (this.hitWall() == Direction.DOWN))) {
			this.fired = false;
			
			//is called when the invader is dropping a bomb
			if (this.invader != null) {
				this.invader.setAttack(false);
				SpaceInvader.numDropping--;
			}
		}
	}
	
	/* GETTERS */
	public boolean getFired() {
		return fired;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	/* SETTERS */
	/**
	 * This is called when the bullet hits something and should no longer be fired.
	 */
	public void setFired() {
		this.fired = !this.fired;
	}
	
	/**
	 * This is called when the user presses the space bar to fire another bullet
	 * @param p the player
	 */
	public void setFired(Player p) {
		this.fired = !this.fired;
		this.setPx(p.getPx() + p.getWidth() / 2);
		this.setPy(p.getPy());
	}
}
