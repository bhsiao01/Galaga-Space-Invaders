import java.awt.*;
import java.util.List;

public class SpaceInvader extends Invader {
	private List<Bullet> bombs;

	static int BOMB_SPEED = 3;	//the speed of the bombs dropped
	static int MAX_DROPPING = 3;	//the maximum number of space invaders dropping bombs at any
									//given time
	
	public static int numDropping = 0;	//the number of space invaders currently dropping bombs
	
	public SpaceInvader(int x, int y, List<Bullet> b) {
		super(x, y, Color.WHITE);
		this.bombs = b;
	}
	
	/**
	 * Makes the space invaders drop bombs and increments the number of invaders currently dropping
	 * bombs
	 */
	public void attack() { 
		if (!this.getDead()) {
			if ((!this.getAttack()) && (numDropping < MAX_DROPPING)) {
				this.setAttack(true);
				numDropping++;
				Bullet new_b = 
					new Bullet(this.getPx() + this.getWidth() / 2, this.getPy() + this.getHeight(), 
								BOMB_SPEED, true, this, Color.WHITE);
				bombs.add(new_b);
			}
		}
	}
	
	/**
	 * Increments the maximum number of space invaders that can drop bombs every 2 seconds
	 */
	public static void setMaxDropping() {
		int t = (int) (System.currentTimeMillis() % 2000);
		if ((t < 15) && (t % 3 == 0)) {
			MAX_DROPPING++;
		}        	
	}
	
	/**
	 * Sets the field MAX_DROPPING to the given value
	 * Mainly used when resetting the game to reset MAX_DROPPING to 0
	 * @param n: the number that MAX_DROPPING should be set to
	 */
	public static void setMaxDropping(int n) {
		MAX_DROPPING = n;      	
	}
}
