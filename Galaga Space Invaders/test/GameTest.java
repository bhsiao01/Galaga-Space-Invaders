import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import javax.swing.JLabel;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {
	
	final JLabel status = new JLabel("Running...");
	private GameCourt court;
	
	//sets up a court for all tests as the court will be used in the majority of the tests
	@Before
	public void setUp() {
		court = new GameCourt(status);
		court.reset();
	}

    @Test
    public void testInvaderShift() {
    	//sets up a 2D array of invaders that are positioned such that the rightmost ones
    	//are past the right border of the court
    	Invader[][] invaders = new SpaceInvader[2][5];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				invaders[i][j] = new SpaceInvader(j * 30 + 285, i * 30, new LinkedList<Bullet>());
			}
		}
		
    	int[][] y = new int[2][5];
    	int vx = invaders[0][0].getVx();
    	
    	//gets the initial y-positions of the invaders
        for (int i = 0; i < invaders.length; i++) {
        	for (int j = 0; j < invaders[i].length; j++) {
        		y[i][j] = invaders[i][j].getPy();
        	}
        }

		//checks that the invaders shift down (they are at the rightmost border)
		assertTrue(Invader.checkShift(invaders));
		
		//checks that the invaders have correctly shifted down and that their x-velocities
		//are negated
		for (int i = 0; i < invaders.length; i++) {
			for (int j = 0; j < invaders[i].length; j++) {
				assertEquals(-1 * vx, invaders[i][j].getVx());
				assertEquals(y[i][j] + 30, invaders[i][j].getPy());
				assertFalse(Invader.checkShift(invaders));
			}
		}
    }
    
    @Test
    public void testCourtNumGInvadersAlive() {
    	int n = court.gInvadersAlive();
    	assertEquals(court.initialNumGInvaders, n);
    }
    
    @Test
    public void testCourtNumSInvadersShot() {
    	Invader[][] s = court.getSInvaders();
    	s[0][0].setDead();
    	s[1][3].setDead();
    	s[2][7].setDead();
    	assertEquals(3, court.sInvadersShot());
    }
    
    @Test
    public void testCourtNewGInvaders() {
    	court.newGInvaders();
    	List<Invader> g = court.getGInvaders();
    	assertEquals(2 * court.initialNumGInvaders, g.size());
    	
    	for (int i = 0; i < g.size(); i++) {
    		assertFalse(g.get(i).getDead());
    		assertEquals(GameCourt.INVADER_VELOCITY, g.get(i).getVx());
    		assertFalse(g.get(i).getAttack());
    	}
    }
    
    @Test
    public void testCheckDeadHitByBomb() {
    	Invader[][] s = court.getSInvaders();
    	Player p = court.getPlayer();
    	s[0][0].attack();	//makes the first invader drop a bomb
    	List<Bullet> bombs = court.getBombs();
    	assertEquals(1, bombs.size());	//checks that only 1 bomb is being dropped
    	bombs.get(0).setPx(p.getPx());	//sets the bomb's position to the player's position
    	bombs.get(0).setPy(p.getPy());
    	
    	assertTrue(court.checkDead());	//checks that the player is dead since the bomb interesects
    									//the player
    }
    
    @Test
    public void testCheckDeadHitBySInvader() {
    	Player p = court.getPlayer();
    	List<Invader> g = court.getGInvaders();
    	Invader g0 = g.get(0);
    	
    	//sets the first galaga invader's position to the player's
    	g0.setPx(p.getPx());
    	g0.setPy(p.getPy());
    	
    	assertTrue(court.checkDead());
    }
    
    @Test
    public void testCheckDeadHitByGInvader() {
    	Player p = court.getPlayer();
    	Invader[][] s = court.getSInvaders();
    	Invader s0 = s[0][0];
    	
    	//sets the first space invader's position to the player's
    	s0.setPx(p.getPx());
    	s0.setPy(p.getPy());
    	
    	assertTrue(court.checkDead());
    }
    
    @Test
    public void testCheckWin() {
    	Invader[][] s = court.getSInvaders();
    	
    	//sets all of the space invader's dead status to true
    	for (int i = 0; i < s.length; i++) {
    		for (int j = 0; j < s[i].length; j++) {
    			s[i][j].setDead();
    		}
    	}
    	
    	assertTrue(court.checkWin());
    }
    
    @Test
    public void testSInvaderShot() {
    	Invader[][] s = court.getSInvaders();
    	Bullet[] b = court.getBullets();
    	
    	b[0].setFired();	//sets the first bullet's fired status to true
    	b[0].setPx(s[1][3].getPx());	//sets the first bullet's position to an invader's position
    	b[0].setPy(s[1][3].getPy());
    	
    	s[1][3].move(b);	//this function checks whether the invader has been hit by a bullet
    	assertTrue(s[1][3].getDead());	//checks that the invader has been killed
    }
    
    @Test
    public void testGInvaderShot() {
    	List<Invader> g = court.getGInvaders();
    	Bullet[] b = court.getBullets();
    	
    	b[0].setFired();	//sets the first bullet's fired status to true
    	b[0].setPx(g.get(2).getPx());	//sets the first bullet's position to an invader's position
    	b[0].setPy(g.get(2).getPy());
    	
    	g.get(2).move(b);	//this function checks whether the invader has been hit by a bullet
    	assertTrue(g.get(2).getDead());	//checks that the invader has been killed
    }
    
    @Test
    public void testInvaderShotByUnfiredBullet() {
    	List<Invader> g = court.getGInvaders();
    	Bullet[] b = court.getBullets();
    	
    	b[0].setPx(g.get(4).getPx());	//sets the first bullet's position to an invader's position
    	b[0].setPy(g.get(4).getPy());
    	
    	g.get(4).move(b);	//this function checks whether the invader has been hit by a bullet
    	assertFalse(g.get(4).getDead());	//checks that the invader has not been killed
    										//(since the bullet has not been fired)
    }
    
    //test gInvader dropping
    @Test
    public void testGInvaderAttack() {
    	List<Invader> g = court.getGInvaders();
    	//calls attack until the invader actually attacks since attack() depends on the time elapsed
    	while (!g.get(3).getAttack()) {	
    		g.get(3).attack();
    	}
    	assertTrue(g.get(3).getAttack());	//checks that the invader has attacked
    	assertEquals((int) (System.currentTimeMillis()), GalagaInvader.lastDropped);	//checks that the 
    	//lastDropped has been correctly updated
    	assertEquals(GalagaInvader.speed, g.get(3).getVy());	//checks that vy has been updated
    }
    
    @Test
    public void testSInvaderAttack() {
    	Invader[][] s = court.getSInvaders();
    	
    	SpaceInvader.numDropping = 0;	//sets numDropping to 0 because tick is triggered, meaning
    									//another invader may also attack
    	s[2][5].attack();	//makes one invader attack
    	assertTrue(s[2][5].getAttack());	//checks that the invader's attackNow status is updated
    	assertEquals(1, SpaceInvader.numDropping);	//checks that numDropping is incremented by 1
    	
    	List<Bullet> b = court.getBombs();
    	assertEquals(1, b.size());	//checks that the invader creates a bomb
    	
    }
    
    @Test
    public void testPlayerFireBullet() {
    	Player p = court.getPlayer();
    	Bullet[] b = court.getBullets();
    	
    	b[4].setFired(p);
    	assertTrue(b[4].getFired());	//checks that the bullet has been fired
    	for (int i = 0; i < 4; i++) {
    		assertFalse(b[i].getFired());	//checks rest of bullets have not been fired
    	}
    	
    	//checks the bullet is at the correct position
    	assertEquals(p.getPx() + (p.getWidth() / 2), b[4].getPx());
    	assertEquals(p.getPy(), b[4].getPy());
    	
    }
    
    @Test
    public void testSInvaderDropBomb() {
    	Invader[][] s = court.getSInvaders();
    	
    	SpaceInvader.numDropping = 0;	//sets numDropping to 0 because tick is triggered, meaning
    									//another invader may also attack
    	s[0][0].attack();	//makes one invader attack
    	
    	List<Bullet> b = court.getBombs();
    	assertEquals(1, b.size());	//checks that the invader creates a bomb
    	//checks that the bomb is at the right position
    	assertEquals(s[0][0].getPx() + s[0][0].getWidth() / 2, b.get(0).getPx());
    	assertEquals(s[0][0].getPy() + s[0][0].getHeight(), b.get(0).getPy());
    }
    
    @Test
    public void testBulletOutOfBounds() {
    	Player p = court.getPlayer();
    	Bullet[] b = court.getBullets();
    	
    	b[1].setFired(p);
    	assertTrue(b[1].getFired());	//checks that the bullet has been fired 
    	
    	b[1].fire();
    	assertEquals(b[1].getSpeed(), b[1].getVy());	//checks the bullet has the right vy
    	//checks that the bullet is being fired while it hasn't hit a wall yet
    	while (b[1].hitWall() != Direction.UP) {
    		assertTrue(b[1].getFired());
    		b[1].fire();
    	}
    	assertFalse(b[1].getFired());
    }
    
    @Test
    public void testBombOutOfBounds() {
    	Invader[][] s = court.getSInvaders();
    	
    	s[0][0].attack();	//makes one invader attack
    	
    	List<Bullet> b = court.getBombs();
    	assertEquals(1, b.size());	//checks that the invader creates a bomb
    	//checks that the bomb is at the right position
    	assertTrue(b.get(0).getFired());	//checks the bomb will fire
    	b.get(0).fire();
    	assertEquals(b.get(0).getSpeed(), b.get(0).getVy());	//checks that vy is the right value
    	
    	//checks that the bomb is being fired while it hasn't hit a wall yet
    	while (b.get(0).hitWall() != Direction.DOWN) {
    		assertTrue(b.get(0).getFired());
    		b.get(0).fire();
    	}
    	assertFalse(b.get(0).getFired());
    }

}
