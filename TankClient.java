package com.iteye.liudongtony.tank;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class TankClient extends Frame{	

	public static final int FRAME_WIDTH =800;
	public static final int FRAME_HEIGHT = 600;
	
	public static final int refreshRatio = 30;
	
	Image offScreenImage = null;
	
	int x = 50, y = 50;	
	
	Tank myTank = new Tank(60, 60, this, Tank.Direction.STOP, true);
	List<Missile> missiles = new ArrayList<>();
	List<Explode> explodes = new ArrayList<>();
	List<Tank> foeTanks = new ArrayList<>();
	
	private void launchFrame() {
		
		for(int i=0; i<10; i++) {
			foeTanks.add(new Tank(100 + 30 * i, 100, this, Tank.Direction.D, false));
		}
		
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocation(300,  200);
		this.setTitle("Tank War");
		this.setBackground(new Color(20, 190, 20));
		this.setResizable(false);
		this.setVisible(true);

		new Thread(new PaintThread()).start();	// a dedicated Thread to maintains frame display
		 
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		this.addKeyListener(new KeyMonitor());
	}
	
	/**
	 * Override java.awt.window paint() method to get a painter. Each object to be displayed in Frame need a painter.
	 * @param Graphics this object to invoke this painter.
	 */
	@Override
	public void paint(Graphics g) {
		myTank.draw(g);

		for(int i=0; i<foeTanks.size(); i++) {
			Tank t = foeTanks.get(i);
			t.draw(g);
		}
		
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(foeTanks);
			m.hitTank(myTank);
			m.draw(g);
		}
		
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		g.setColor(Color.WHITE);
		g.drawString("Missile  count: " + missiles.size(), 10, 40);
		g.drawString("Explode  count: " + explodes.size(), 10, 55);
		g.drawString("FoeTanks count: " + foeTanks.size(), 10, 70);
		
	}	
	
	/**
	 * dual-buffer to solve screen flash issue. use java.awt.image, the buffered image class.
	 */
	@Override	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(new Color(0, 180, 0));
		gOffScreen.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	
	/**
	 * PaintThread to refresh the whole frame, and update elements status periodically.
	 * Looks like not so necessary in modern computers.
	 */
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(refreshRatio);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);		 
		 }
		
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
	}
	

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	
}
