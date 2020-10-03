/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import java.util.*;
import java.util.List;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	private Player player;
	private Invader[][] sInvaders;
	private List<Invader> gInvaders;
	private Bullet[] bullets;
	private List<Bullet> bombs;

    // the state of the game logic

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private int bulletFrame = 0; //the last millisecond that a bullet was fired
    private int bulletIndex = 0;
    private int numGInvaders = 10;
    
    
    int initialNumGInvaders = 10;

    // Game constants
    static final int COURT_WIDTH = 400;
    static final int COURT_HEIGHT = 400;
    static final int PLAYER_VELOCITY = 3;
    static final int INVADER_VELOCITY = 1;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 15;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.BLACK);

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setVx(-PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setVx(PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                	if ((!bullets[bulletIndex].getFired()) && (System.currentTimeMillis() - bulletFrame > 100)) {
                		bullets[bulletIndex].setFired(player);
                		bulletFrame = (int) (System.currentTimeMillis());
                		bulletIndex++;
                		if (bulletIndex > 4) {
                			bulletIndex = 0;
                		}
                	}
                }
            }

            public void keyReleased(KeyEvent e) {
                player.setVx(0);
            }
        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	player = new Player();
    	bullets = new Bullet[5];
    	for (int i = 0; i < 5; i++) {
    		bullets[i] = new Bullet(-3, Color.GREEN);
    	}
    	
    	bombs = new LinkedList<Bullet>();
    	
    	sInvaders = new SpaceInvader[3][10];
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 10; j++) {
    			sInvaders[i][j] = new SpaceInvader(j * 30, i * 30, bombs);
    		}
    	}
    	SpaceInvader.numDropping = 0;
    	SpaceInvader.setMaxDropping(3);
    	
    	gInvaders = new LinkedList<Invader>();
    	for (int i = 0; i < initialNumGInvaders; i++) {
    		gInvaders.add(new GalagaInvader(i*30, 95));
    	}
    	numGInvaders = 10;

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * Creates the instructions window when the Instructions button is pressed
     */
    public void displayInstructions() {
    	JFrame instructions = new JFrame();
    	instructions.setLocation(400, 400);
    	JPanel panel = new JPanel();
    	instructions.add(panel, BorderLayout.CENTER);
    	String text = "<html> Instructions <br/> "
    			+ "In Galaga Space Invaders, you are the gun (the green rectangle).<br/>"
    			+ "The red invaders are the \"Galaga\" invaders, and the white invaders are the "
    			+ "\"space\" invaders. <br/> The space invaders drop bombs, while the Galaga "
    			+ "invaders dive down to hit you. The Galaga invaders regenerate "
    			+ "each time you kill all of them. <br/> If the space" 
    			+ " invaders or their bombs or the Galaga invaders touch you, you die. <br/>"
    			+ "Your goal is to shoot down all of the space invaders. <br/>"
    			+ "Your score only increments when you shoot the space invaders. <br/>"
    			+ "Use the arrow keys to move and the space bar to shoot. You can only shoot "
    			+ "5 bullets at once. <br/>"
    			+ "You win when you kill all of the space invaders. </html>";
    	JLabel textLabel = new JLabel(text);
    	panel.add(textLabel);
    	
    	instructions.pack();
        instructions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructions.setVisible(true);
        
    	requestFocusInWindow();
    }
    
    /**
     * Helper method that counts how many space invaders have been shot
     * @return the number of space invaders that have been killed
     */
    int sInvadersShot() {
    	int num = 0;
    	for (int i = 0; i < sInvaders.length; i++) {
    		for (int j = 0; j < sInvaders[i].length; j++) {
	    		if (sInvaders[i][j].getDead()) {
	    			num++;
	    		}
    		}
    	}
    	return num;
    }
    
    /**
     * Helper method that counts the number of Galaga invaders that are still alive
     * @return the number of Galaga invaders that are still alive
     */
    int gInvadersAlive() {
    	int numAlive = 0;
    	for (int i = 0; i < gInvaders.size(); i++) {
    		if (!gInvaders.get(i).getDead()) {
    			numAlive++;
    		}
    	}
    	return numAlive;
    }
    
    /**
     * Helper method that creates a new row of Galaga invaders each time all of the previous ones
     * are all killed
     */
    void newGInvaders() {
    	//resets the positions and states of being dead for the pre-existing galaga invaders
    	for (int i = 0; i < numGInvaders; i++) {
    		gInvaders.get(i).setPx((i % 10) * 30);
    		gInvaders.get(i).setPy(40 + (i / 10) * 30);
    		gInvaders.get(i).setVx(INVADER_VELOCITY);
    		gInvaders.get(i).setVy(0);
    		if (gInvaders.get(i).getDead()) {
    			gInvaders.get(i).setDead();
    		}
    		gInvaders.get(i).setAttack(false);
    	}
    	
    	//adds another row galaga invaders
    	for (int i = 0; i < 10; i++) {
    		gInvaders.add(new GalagaInvader(i * 30, 10));
    	}
    	//add double the original amount when all are dead. Solves shifting problem
    	numGInvaders = gInvaders.size();
    }
    
    /**
     * checks whether the player is hit by a bomb, space invader, or galaga invader
     * @return the boolean value of whether the player is dead
     */
    boolean checkDead() {
    	boolean dead = false;
    	for (int i = 0; i < bombs.size(); i++) {
    		if (bombs.get(i).getFired()) {
    			if (bombs.get(i).intersects(player)) {
    				dead = true;
    			}
    		}
    	}
    	
    	for (int i = 0; i < sInvaders.length; i++) {
    		for (int j = 0; j < sInvaders[i].length; j++) {
    			if (!sInvaders[i][j].getDead()) {
	    			if (sInvaders[i][j].intersects(player)) {
	    				dead = true;
	    			}
    			}
    		}
    	}
    	
    	for (int i = 0; i < gInvaders.size(); i++) {
    		if (!gInvaders.get(i).getDead()) {
    			if (gInvaders.get(i).intersects(player)) {
    				dead = true;
    			}
    		}
    	}
    	
    	return dead;
    }
    
    /**
     * Checks if the player has won
     * @return true if all of the space invaders are dead, false otherwise
     */
    boolean checkWin() {
    	int numAlive = 0;
    	for (int i = 0; i < sInvaders.length; i++) {
    		for (int j = 0; j < sInvaders[i].length; j++) {
    			if (!sInvaders[i][j].getDead()) {
    				numAlive++;
    			}
    		}
    	}
    	
    	if (numAlive == 0) {
    		return true;
    	}
    	return false;
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
        	player.move();
        	for (int i = 0; i < sInvaders.length; i++) {
        		for (int j = 0; j < sInvaders[i].length; j++) {
        			sInvaders[i][j].move(bullets);
        			if (sInvaders[i][j].randomNum()) {
            			sInvaders[i][j].attack();
        			}
        		}
        	}
        	
        	for (int i = 0; i < gInvaders.size(); i++) {
        		gInvaders.get(i).move(bullets);
        		if (gInvaders.get(i).randomNum()) {
        			gInvaders.get(i).attack();
        		}
        	}
        	
        	if (gInvadersAlive() <= 0) {
        		newGInvaders();
        	}
        	
        	Invader.checkShift(sInvaders);
        	Invader.checkShift(gInvaders);
        	
        	SpaceInvader.setMaxDropping();
        	
        	for (int i = 0; i < bullets.length; i++) {
        		if (bullets[i].getFired()) {
        			bullets[i].fire();
        		}
        	}
        	
        	for (int i = 0; i < bombs.size(); i++) {
        		if (bombs.get(i).getFired()) {
        			bombs.get(i).fire();
        		}
        	}
        	
        	status.setText("<html> Score: " + sInvadersShot() + "<br/> Running... <html/>");
        	
        	if (checkDead()) {
        		playing = false;
        		status.setText("<html> Score: " + sInvadersShot() + "<br/> Game over! <html/>");
        	}
        	
        	if (checkWin()) {
        		playing = false;
        		status.setText("<html> Score: " + sInvadersShot() + "<br/> You won! <html/>");
        	}

            // update the display
            repaint();
        }
    }
    
    /* GETTERS */
    public Player getPlayer() {
    	return player;
    }
    
    public Invader[][] getSInvaders() {
    	int l = sInvaders.length;
    	int h = sInvaders[0].length;
    	Invader[][] inv = new Invader[l][h];
    	
    	for (int i = 0; i < sInvaders.length; i++) {
    		for (int j = 0; j < sInvaders[i].length; j++) {
    			inv[i][j] = sInvaders[i][j];
    		}
    	}
    	
    	return sInvaders;
    }
    
    public List<Invader> getGInvaders() {
    	List<Invader> gInv = new LinkedList<Invader>();
    	for (int i = 0; i < gInvaders.size(); i++) {
    		gInv.add(gInvaders.get(i));
    	}
    	
    	return gInv;
    }
    
    public Bullet[] getBullets() {
    	int l = bullets.length;
    	Bullet[] b = new Bullet[l];
    	for (int i = 0; i < l; i++) {
    		b[i] = bullets[i];
    	}
    	
    	return b;
    }
    
    public List<Bullet> getBombs() {
    	int l = bombs.size();
    	List<Bullet> b = new LinkedList<Bullet>();
    	for (int i = 0; i < l; i++) {
    		b.add(bombs.get(i));
    	}
    	
    	return b;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (int i = 0; i < sInvaders.length; i++) {
        	for (int j = 0; j < sInvaders[i].length; j++) {
        		if (!sInvaders[i][j].getDead()) {
        			sInvaders[i][j].draw(g);
        		}
        	}
        }
        
        for (int i = 0; i < gInvaders.size(); i++) {
        	if (!gInvaders.get(i).getDead()) {
        		gInvaders.get(i).draw(g);
        	}
        }
        
        for (int i = 0; i < bullets.length; i++) {
        	if (bullets[i].getFired()) {
        		bullets[i].draw(g);
        	}
        }
        
        for (int i = 0; i < bombs.size(); i++) {
        	if (bombs.get(i).getFired()) {
        		bombs.get(i).draw(g);
        	}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}