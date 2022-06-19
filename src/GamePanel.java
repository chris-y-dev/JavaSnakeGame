import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.*;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	//fields
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	//unit (size of each thing)
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; //how many objs can we find in screen?
	
	static int delay = 80; //speed of game.
	
	//////Arrays for coordinates of snake
	final int x[] = new int[GAME_UNITS]; //snake cannot be longer than game_units.
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 6;
	int applesEaten = 0;
	
	//coorindates for apple
	int appleX;
	int appleY;
	
	//direction of snake (start by going right)
	char direction = 'R'; 
	
	boolean running = false;
	Timer timer;
	Random random;
	
	
	///Constructor
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter()); //adds key listener to this component
		startGame();
	}
	
	//methods 
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay, this); //pass in delay + listener (this = ActionListener interface)
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {	
		/////////Two screens -> using If condition
		
		if(running) {	
			//drawing a grid to help us visualise unit/frame size
			for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) { ///for each 'unit'
				
				//X axis
				g.drawLine(i* UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); //x1 y1 x2 y2 (first coordinate + end coordinate)
				
				//Y axis
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); 
			}
			
			
			////draw apple
			g.setColor(Color.red);
			
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //draw a circle - X, Y, width, height
			
			
			////draw body of snake using loop + coordinate Array
			for(int i = 0; i<bodyParts; i++) {
				if (i==0) {
					//0 = head of snake
					g.setColor(Color.green);
	
					g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
				} else {
					//body of snake
					g.setColor(new Color(45,180,0));
					
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}	
			}
			
			////While game is running, display score
			displayScore(g);
			
		} else {
			displayScore(g);
			gameOver(g);  //call game over method, pass in Graphics
		}
		
	}
	
	public void move() {
		///iterate through ALL BODY PARTS of snake
		
		for (int i = bodyParts; i>0; i--){
			//take each coordinate + shift it by 1 spot
			
			//eg index 6 = index 5......index 1 = index 0
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		//switch to determine direction
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE; ///Y Axis starts on top at 0 -> Minus to go up
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE; ///Add to go down 25 + 25 -> 50
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE; // coording x: 50 - 25 = 25. = move to left
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE; /// + 25 to move to right
			break;
		}
		
	}
	
	public void newApple() {
		
		//generate new apple coordinates
		//nextInt(Upperbound) -> 600/25 = 24units wide. * 24 to get accurate pixel coordinate
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; 
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; 
		
	}
	
	public void checkApple() {
		
		//Compare head coordinates VS apple coordinates
		if ((x[0]==appleX)&& (y[0]==appleY)) {
			bodyParts+=1;
			applesEaten+=1;
			newApple();
		}
		
	}
	
	public void checkCollisions() {
		
		///check if head collides with body
		for(int i = bodyParts; i>0; i--) {
			
			if((x[0] == x[i]) && (y[0] == y[i])){	//if the coordinates match coordinates of body
				
				//stop the game
				running = false;
			}
		}
		
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
			System.out.println("Left border");
		}
		
		//if head touches Right border (screen width)
		if(x[0] > SCREEN_WIDTH) {
			running = false;
			System.out.println("Right border");
		}
		
		//check if head touches Bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
			System.out.println("Bottom border");
		}
		
		//if head touches Top border
		if (y[0] < 0) {
			running = false;
			System.out.println("Top border");
		}	
	}
	
	public void displayScore(Graphics g) {
		//Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Helvetica", Font.BOLD, 25));
		
		//Font Metrics help align font to Centre of Screen
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		//Draw the string
		///String, Coordinate X, Coordinate Y (if y=size of font, y = top)
		g.drawString(("Score: " + applesEaten), (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/SCREEN_WIDTH + 25, g.getFont().getSize()); 
		
		//above helps calculate size of string, size of screen + position it in middle.
		
	}
	
	//increase speed
	public void setSpeed() {
		
		if (applesEaten == 5) {
			timer.setDelay(75);
		}
		
		if (applesEaten == 10) {
			
			timer.setDelay(70);
		} 

		if (applesEaten == 15) {
			timer.setDelay(65);
		} 

		if (applesEaten == 20) {

			timer.setDelay(60);
		}
	}
	
	public void gameOver(Graphics g) {
		//Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Helvetica", Font.BOLD, 70));
		
		//Font Metrics help align font to Centre of Screen
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		//Draw the string
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); ///String, Coordinate X, Coordinate Y
		
		//above helps calculate size of string, size of screen + position it in middle.
	}
	
	
	/////this is where game events occur
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			//move the snake
			move();
			
			//track if ran into apple / collision
			checkApple();
			checkCollisions();
			setSpeed();
		}
		//if no longer running - repaint
		repaint();
		
	}
	
	
	//inner class
	public class MyKeyAdapter extends KeyAdapter{
		/////////////////This is the event listener - needed to determine keyboard input
		@Override
		public void keyPressed(KeyEvent e) {
			
			//get the key input
			//***********LIMIT DIRECTIONS TO 90Degrees ONLY (180 = self crash)****************
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				//////Only turn left IF current direction is NOT R (no 180!)
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
