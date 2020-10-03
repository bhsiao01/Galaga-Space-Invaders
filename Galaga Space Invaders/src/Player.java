import java.awt.*;

public class Player extends GameObj{
	
	public Player() {
		super(0, 0, 180, 380, 40, 20, 400, 400);
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}
