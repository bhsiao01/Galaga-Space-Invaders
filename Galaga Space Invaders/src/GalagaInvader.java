import java.awt.*;

public class GalagaInvader extends Invader {
	static int lastDropped = (int) (System.currentTimeMillis());
	static final int speed = 2;
	
	public GalagaInvader(int x, int y) {
		super(x, y, Color.RED);
	}
	
	/**
	 * Makes the Galaga invaders drop down and sets the last time a Galaga invader dropped down
	 * to the current time
	 */
	public void attack() {
		if (!this.getDead()) {
			int time = (int) (Math.abs(System.currentTimeMillis() - lastDropped));
			//checks whether it's been at least 1 second since the last Galaga invader attacked
			if ((!this.getAttack()) && (time > 1000)) {	
				lastDropped = (int) (System.currentTimeMillis());
				this.setAttack(true);
				this.setVy(speed);
				this.setVx(0);
			}
			
			//"kills" the Galaga invader once it hits the bottom border
			if (this.hitWall() == Direction.DOWN) {
				this.setDead();
				this.setAttack(false);
			}
		}
	}
}
