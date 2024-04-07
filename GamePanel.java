package com.projects.java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	//define constants
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; //size of objects
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // max number of objects can fill
	static final int DELAY = 50; //speed of game
	final int x[] = new int[GAME_UNITS]; // x coordinate of snake
	final int y[] = new int[GAME_UNITS]; // y coordinate of snake
	int bodyParts = 6; // initial size of snake
	int applesEaten; // score
	int appleX; // x coordinate of apple
	int appleY; // y coordinate of apple
	char direction = 'R'; // initial direction of snake
	boolean running = false; // game is running or not
	Timer timer; // timer for game
	Random random; // random number generator
	
	//constructor
	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // set size of panel
		this.setBackground(Color.black); // set background color
		this.setFocusable(true); // allow focus on panel
		this.addKeyListener(new MyKeyAdapter()); // add key listener
		startGame();
	}
	
	public void startGame() {
		newApple(); // call newApple method to create apple
		running = true;
		timer = new Timer(DELAY, this); // create new timer and set delay to this
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if(running) { // check if game is running
			// draw grid to see the movement of snake	
			// i will remove the grid in the final version
			/*
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				// g.drawLine(x1, y1, x2, y2) where x1 y1 is initial coordinate and x2 y2 is destination
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // draw vertical lines
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // draw horizontal lines
			}
			*/
		
			// draw apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // fill oval with red color
		
			// draw snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green); // head of snake
				} else {
					g.setColor(new Color(45, 180, 0)); // body of snake
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); // random color for snake for fun
				}
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // fill rectangle with color
			}
			
			// draw score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 30)); // set font
			FontMetrics metrics = getFontMetrics(g.getFont()); // line text in the center
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		//random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) gives the random number between 0 and number of columns
		// after * UNIT_SIZE gives the random x coordinate of apple cause apple size is 25 (unit size) so it should be multiple of 25
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // set random x coordinate of apple
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // set random y coordinate of apple
	}
	
	public void move() {
		// move body parts of snake
		for (int i = bodyParts; i > 0; i--) {
			// shift the coordinates of snake body parts to the next body part
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		// move head of snake
		// y[0] is y coordinate of head of snake
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) { // if snake ate apple
			bodyParts++;
			applesEaten++;
			newApple(); // generate new apple
		}
	}
	
	public void checkCollisions() {
		for (int i = bodyParts; i > 0; i--) {
			// if head of snake collides with any body part of snake then game is over (loop through all body parts of snake)
			if ((x[0] == x[i]) && (y[0] == y[i])) { // check if snake collided with itself at the current position
				running = false;
			}
		}
		
		//check if head of snake collides with left border
		if (x[0] < 0) {
			running = false;
		}
		//check if head of snake collides with right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head of snake collides with top border
		if (y[0] < 0) {
			running = false;
		}
		//check if head of snake collides with bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) { // cũng draw nhưng là khi game over
		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75)); // Font
		FontMetrics metrics = getFontMetrics(g.getFont()); // line text in the center
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		// Score text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());
		// g.getFont().getSize() is the height of the font
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) { // check if game is running
			move();
			checkApple(); // check if snake ate apple
			checkCollisions(); // check if snake collided with itself or wall
		}
		repaint(); // repaint the panel
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT: // if left arrow key is pressed
			// limitation: snake can't move in opposite direction
			// kiểu đang right thì không thể left
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			
			case KeyEvent.VK_RIGHT: // if right arrow key is pressed
				// limitation: snake can't move in opposite direction
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
                
			case KeyEvent.VK_UP: // if up arrow key is pressed
				// limitation: snake can't move in opposite direction
				if (direction != 'D') {
					direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN: // if down arrow key is pressed
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}
	}

}
