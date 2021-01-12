package com.ssssnake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

	
	private final int SCREEN_WIDTH = 800;
	private final int SCREEN_HEIGHT = 800;
	private final int PIXEL_SIZE = 10;
	private final int ALL_PIXELS = 900;
	private final int RAND_POS = 29;
	private final int DELAY = 140;
	
	private final int x[] = new int[ALL_PIXELS];
	private final int y[] = new int[ALL_PIXELS];
	
	private int dots;
	private int apple_x;
	private int apple_y;
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	
	private Timer timer;
	private Image apple;
	private Image diamond;
	private Image tiger;
	
	public Board() {
		addKeyListener(new TAdapter());
		setBackground(Color.pink);
		setFocusable(true);
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		loadImages();
		initGame();
	}
	
	
	private void loadImages() {
		ImageIcon iid = new ImageIcon(this.getClass().getResource("/res/diamond.png"));
		diamond = iid.getImage();
		
		ImageIcon iia = new ImageIcon(this.getClass().getResource("/res/apple.png"));
		apple = iia.getImage();
		
		ImageIcon iih = new ImageIcon(this.getClass().getResource("/res/tiger.png"));
		tiger = iih.getImage();
	}
	
	private void initGame() {
		dots = 3;
		
		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}
		
		locateApple();
		
		//use a timer on a timer to call action performed method at a fixed delay
		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	protected void doDrawing(Graphics g) {
		Scanner input = new Scanner(System.in);
		if (inGame) {
			g.drawImage(apple, apple_x, apple_y, this);
			
			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(tiger, x[z], y[z], this);
				}
				else {
					g.drawImage(diamond, x[z], y[z], this);
				}
			}
			
			Toolkit.getDefaultToolkit().sync();
		}
		else {
			gameOver(g);
		}
	}
	

	private void gameOver(Graphics g) {
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(small);
		
		g.setColor(Color.black);
		g.setFont(small);
		g.drawString(msg, (SCREEN_WIDTH - metr.stringWidth(msg)) / 2, SCREEN_HEIGHT / 2);
		
	}
	
	private void checkApple() {
		if ((x[0] == apple_x) && (y[0] == apple_y)) {
			dots++;
			locateApple();
		}
	}
	
	private void move() {
		for (int z = dots; z > 0; z--) {
			x[z] = x[z-1];
			y[z] = y[z-1];
		}
		if (leftDirection) {
			x[0] -= PIXEL_SIZE;
		}
		if (rightDirection) {
			x[0] += PIXEL_SIZE;
		}
		if (upDirection) {
			y[0] -= PIXEL_SIZE;
		}
		if (downDirection) {
			y[0] += PIXEL_SIZE;
		}
	}
	
	private void checkCollision() {
		for (int z = dots; z > 0; z--) {
			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				inGame = false;
			}
		}
		if (y[0] >= SCREEN_HEIGHT) {
			inGame = false;
		}
		if (y[0] < 0) {
			inGame = false;
		}
		if (x[0] >= SCREEN_WIDTH) {
			inGame = false;
		}
		if (x[0] < 0) {
			inGame = false;
		}
		if (!inGame) {
			timer.stop();
		}
	}
	
	private void locateApple() {
		int r = (int) (Math.random() * RAND_POS);
		apple_x = r * PIXEL_SIZE;
		
		r = (int) (Math.random() * RAND_POS);
		apple_y = r * PIXEL_SIZE;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			checkApple();
			checkCollision();
			move();
		}
		repaint();
	}
	
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			 int key = e.getKeyCode();
			 
			 if (key == KeyEvent.VK_LEFT && !rightDirection) {
				 leftDirection = true;
				 upDirection = false;
				 downDirection = false;
			 }
			 if (key == KeyEvent.VK_RIGHT && !leftDirection) {
				 rightDirection = true;
				 upDirection = false;
				 downDirection = false;
			 }
			 if (key == KeyEvent.VK_UP && !downDirection) {
				 upDirection = true;
				 rightDirection = false;
				 leftDirection = false;
			 }
			 if (key == KeyEvent.VK_DOWN && !upDirection) {
				 downDirection = true;
				 rightDirection = false;
				 leftDirection = false;
			 }
		}
	}

}
