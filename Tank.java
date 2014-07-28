package com.iteye.liudongtony.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;

public class Tank {
	public static final int TANK_WIDTH = 30;
	public static final int TANK_HEIGHT = 30;
	public static final int X_SPEED = 4;
	public static final int Y_SPEED = 4;
	
	TankClient tc;
	
	private static Random r = new Random();
	
	private boolean goodGuy;
	public boolean isGoodGuy() {
		return goodGuy;
	}

	private boolean alive = true;
	
	private int step = r.nextInt(12) + 3;
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	int x, y;
	
	private boolean bL = false, bU = false, bR = false, bD = false;
	enum Direction {L, UL, U, UR, R, DR, D, DL, STOP};
	
	private Direction dir = Direction.STOP;
	private Direction mDir = Direction.D;
	
	public Tank(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public Tank(int x, int y, TankClient tc, boolean goodGuy) {
		this(x, y, tc);
		this.goodGuy = goodGuy;
	}
	
	public Tank(int x, int y, TankClient tc, Direction d, boolean goodGuy) {
		this(x, y, tc);
		this.dir = d;
		this.goodGuy = goodGuy;
	}
	
	public void draw(Graphics g) {
		if(!alive) return;
		
		Color c = g.getColor();	
		if(goodGuy) {
			g.setColor(Color.BLUE);
		} else g.setColor(new Color(126, 135, 5));
		g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
		
		g.setColor(Color.WHITE);		
		switch(mDir) {
		case L:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x - 4, y + Tank.TANK_HEIGHT/2);
			break;
		case UL:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.TANK_WIDTH/2, y - 4, x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2);
			break;
		case UR:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x + Tank.TANK_WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x + Tank.TANK_WIDTH + 4, y + Tank.TANK_HEIGHT/2);
			break;
		case DR:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x + Tank.TANK_WIDTH, y + Tank.TANK_HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT + 4);
			break;
		case DL:
			g.drawLine(x + Tank.TANK_WIDTH/2, y + Tank.TANK_HEIGHT/2, x, y + Tank.TANK_HEIGHT);
			break;
		}		
		
		g.setColor(c);
		move();
	}
	
	void move() {
		switch(dir) {
		case L:
			x -= X_SPEED;
			break;
		case UL:
			x -= X_SPEED;
			y -= Y_SPEED;
			break;
		case U:
			y -= Y_SPEED;
			break;
		case UR:
			x += X_SPEED;
			y -= Y_SPEED;
			break;
		case R:
			x += X_SPEED;
			break;
		case DR:
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case D:
			y += Y_SPEED;
			break;
		case DL:
			x -= X_SPEED;
			y += Y_SPEED;
			break;
		case STOP:
			break;
		}
		
		if(! goodGuy) {
			if(step == 0) {
				step = r.nextInt(12) + 3;
				Direction[] dirs = Direction.values();
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			
			if(r.nextInt(40) > 38) this.shoot();
		}
		
		if(this.dir != Direction.STOP) {
			mDir = dir;
		}
		
		if(x < 2) x = 2;
		if(y < 25) y = 25;
		if(x > TankClient.FRAME_WIDTH - Tank.TANK_WIDTH - 2) x = TankClient.FRAME_WIDTH - Tank.TANK_WIDTH - 2;
		if(y > TankClient.FRAME_HEIGHT - Tank.TANK_HEIGHT - 2) y = TankClient.FRAME_HEIGHT - Tank.TANK_HEIGHT - 2;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;			
		}
		locationDirection();
	}
	
	//ÖØÖÃ·½Ïò
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_Z:
			tc.myTank.shoot();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;			
		}
		locationDirection();
	}
	
	void locationDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.UL;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.UR;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.DR;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.DL;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	public void shoot() {
		if(!alive) return;
		int x = this.x + Tank.TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2;
		int y = this.y + Tank.TANK_HEIGHT / 2 - Missile.MISSILE_HEIGHT / 2;
		Missile m = new Missile(x, y, mDir, goodGuy, this.tc);
		tc.missiles.add(m);
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, TANK_WIDTH, TANK_HEIGHT);
	}

	
}
