package com.iteye.liudongtony.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;


public class Missile {
	public static final int MISSILE_WIDTH = 4;
	public static final int MISSILE_HEIGHT = 4;
	public static final int X_MISSILE_SPEED = 8;
	public static final int Y_MISSILE_SPEED = 8;
	
	int x,y;
	Tank.Direction dir; 
	private Boolean alive = true;
	private Boolean goodGuy;
	private TankClient tc;
	
	public Boolean isAlive() {
		return alive;
	}

	public Missile(int x, int y, Tank.Direction mDir, Boolean goodGuy, TankClient tc) {
		this.x = x;
		this.y = y;
		this.dir = mDir;		
		this.goodGuy = goodGuy;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if(! alive){
			tc.missiles.remove(this);
			return;
		}
		
		Color c = g.getColor();		
		g.setColor(Color.WHITE);
		g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);
		g.setColor(c);
		
		move();
	}
	
	private void move() {
		switch(dir) {
		case L:
			x -= X_MISSILE_SPEED;
			break;
		case UL:
			x -= X_MISSILE_SPEED;
			y -= Y_MISSILE_SPEED;
			break;
		case U:
			y -= Y_MISSILE_SPEED;
			break;
		case UR:
			x += X_MISSILE_SPEED;
			y -= Y_MISSILE_SPEED;
			break;
		case R:
			x += X_MISSILE_SPEED;
			break;
		case DR:
			x += X_MISSILE_SPEED;
			y += Y_MISSILE_SPEED;
			break;
		case D:
			y += Y_MISSILE_SPEED;
			break;
		case DL:
			x -= X_MISSILE_SPEED;
			y += Y_MISSILE_SPEED;
			break;
		}
		if(x < 0 || x > TankClient.FRAME_WIDTH || y < 0 || y > TankClient.FRAME_HEIGHT) {
			alive = false;
		}
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);
	}
	
	public boolean hitTank(Tank t) {
		if(goodGuy != t.isGoodGuy() && t.isAlive() && alive && this.getRec().intersects(t.getRec()) && t.isAlive()) {
			t.setAlive(false);
			alive = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> foeTanks) {
		for(int i=0; i<foeTanks.size(); i++) {
			if(hitTank(foeTanks.get(i))) {
				foeTanks.remove(i);
				return true;				
			}
		}
		return false;
	}
	
}
