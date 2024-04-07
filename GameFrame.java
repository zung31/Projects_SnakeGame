package com.projects.java;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	// constructor
	public GameFrame() {
		GamePanel panel = new GamePanel();
		this.add(panel); // or this.add(new GamePanel()); is a shortcut
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
