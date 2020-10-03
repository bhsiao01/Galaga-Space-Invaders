import java.awt.*;
import java.util.List;

/**
 * The abstract class for the 2 different types of invaders (SpaceInvaders and GalagaInvaders).
 * The SpaceInvaders and GalagaInvaders will share move(), shiftAll(), and bomb() methods.
 */
public abstract class Invader extends GameObj {
	private boolean dead;	//whether the invader is dead
	private boolean attackNow;	//whether the invader should attack now
	private Color color;	//the color of the invader
	
	public Invader (int x, int y, Color c) {
		super(1, 0, x, y, 20, 20, 400, 400);
		this.dead = false;
		this.attackNow = false;
		this.color = c;
	}
	
	/**
	 * Draws the invader
	 */
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
	
	/**
	 * Moves the invaders
	 * @param b the bullets; this allows move() to check whether this invader has been hit and
	 * killed by the player
	 */
	public void move(Bullet[] b) {
		if (!this.dead) {
			super.move();
			for (int i = 0; i < b.length; i++) {
				if ((b[i].getFired()) && (this.hitObj(b[i]) != null)) {
					this.dead = true;
					b[i].setFired();
				}
			}
		}
	}
	
	/**
	 * the method of how the invaders will attack
	 * To be implemented by subtypes
	 */
	public abstract void attack();
	
	/**
	 * Static method that shifts the invaders down by 30 pixels each time the rightmost or leftmost
	 * invader hits a border
	 * @param invaders the space invaders in the court
	 */
	private static void shift(Invader[][] invaders) {
		for (int i = 0; i < invaders.length; i++) {
			for (int j = 0; j < invaders[i].length; j++) {
				int y = invaders[i][j].getPy();
				int vx = invaders[i][j].getVx();
				invaders[i][j].setPy(y + 30);
				invaders[i][j].setVx(vx * -1);
			}
		}
	}
	
	/**
	 * Static method that shifts the invaders down by 30 pixels each time the rightmost or leftmost
	 * invader hits a border
	 * @param invaders the Galaga invaders in the court
	 */
	private static void shift(List<Invader> invaders) {
		for (int i = 0; i < invaders.size(); i++) {
			int y = invaders.get(i).getPy();
			int vx = invaders.get(i).getVx();
			invaders.get(i).setPy(y + 30);
			invaders.get(i).setVx(-1 * vx);
		}
	}
	
	/**
	 * Static method that checks whether the rightmost or leftmost invader has hit a border
	 * @param invaders the space invaders in the court
	 * @return whether the invaders need to shift down
	 */
	public static boolean checkShift(Invader[][] invaders) {
		for (int i = 0; i < invaders.length; i++) {
			for (int j = 0; j < invaders[i].length; j++) {
				if (!invaders[i][j].getDead()) {
					if ((invaders[i][j].hitWall() == Direction.LEFT) || 
							(invaders[i][j].hitWall() == Direction.RIGHT)) {
						shift(invaders);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Static method that checks whether the rightmost or leftmost invader has hit a border
	 * @param invaders the Galaga invaders in the court
	 * @return whether the invaders need to shift down
	 */
	public static boolean checkShift(List<Invader> invaders) {
		for (int i = 0; i < invaders.size(); i++) {
			if (!invaders.get(i).getDead()) {
				if ((invaders.get(i).hitWall() == Direction.LEFT) || 
						(invaders.get(i).hitWall() == Direction.RIGHT)) {
					shift(invaders);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Generates a random number to determine whether the invaders should attack
	 * @return whether a random integer between 1 and 100 is <= 5
	 */
	public boolean randomNum() {
		int r = (int) (Math.random() * 100 + 1);
		return r <= 5;
	}
	
	/* GETTERS */
	public boolean getDead() {
		return this.dead;
	}
	
	public boolean getAttack() {
		return this.attackNow;
	}
	
	/* SETTERS */
	public void setDead() {
		this.dead = !this.dead;
	}
	
	public void setAttack(boolean b) {
		this.attackNow = b;
	}
}
