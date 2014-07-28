package com.iteye.liudongtony.tank;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {

	private int x, y;
	private boolean alive = true;
	int[] diameter = {4, 8, 12, 20, 28, 36, 44, 30, 20, 10};
	private TankClient tc;
	
	int step = 0;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(! alive) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == diameter.length) {
			alive = false;
			step = 0;
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		step ++;
		g.setColor(c);
	}
}
