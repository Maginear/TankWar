import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;


public class Tank {
	
    public int tank_x = 50;
    public int tank_y = 50;
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private static final int SPEED = 5;
    private int BulletNum;
    private boolean bePlayerTank; 
    private boolean live = true; 
    
    private boolean bL = false, bU = false, bR = false, bD = false;
    enum Direction { L, LU, U, RU, R, RD, D, LD, STOP};
    Direction dir = Direction.STOP;
    Direction ptDir = Direction.R;
    
    ArrayList<Integer> oldKeyss = new ArrayList<Integer>();
  
  
    private static Random r = new Random(); 
    public TankClient tc;
	
	public Tank(int tank_x, int tank_y, boolean bePlayerTank, TankClient tc) {
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.bePlayerTank = bePlayerTank;
		this.tc = tc;
		new Thread(new MakeBullet()).start();
	
	}
	
	public void drawMe(Graphics g){
		if(live) {
		Color c = g.getColor();
		if(bePlayerTank) g.setColor(Color.RED);
		else g.setColor(Color.blue);
		g.fillOval(tank_x,tank_y, WIDTH, HEIGHT);
		g.setColor(c);
		
		switch(ptDir){
		case L:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x , tank_y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x , tank_y);
			break;
		case U:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH/2, tank_y);
			break;
		case RU:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH, tank_y);
			break;
		case R:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH , tank_y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH , tank_y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH/2 , tank_y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x , tank_y + Tank.HEIGHT);
			break;
	
		}
		move();
	}
		else tc.tanks.remove(this);
}	
	
	private void move(){
		switch(dir){
		case L:
			tank_x -= SPEED;
			break;
		case LU:
			tank_x -= SPEED;
			tank_y -= SPEED;
			break;
		case U:
			tank_y -= SPEED;
			break;
		case RU:
			tank_x += SPEED;
			tank_y -= SPEED;
			break;
		case R:
			tank_x += SPEED;
			break;
		case RD:
			tank_x += SPEED;
			tank_y += SPEED;
			break;
		case D:
			tank_y += SPEED;
			break;
		case LD:
			tank_x -= SPEED;
			tank_y += SPEED;
			break;
		case STOP:
			break;
	
		}
		
		if(tank_x < 5) tank_x = 5;
		if(tank_y < 30) tank_y = 30;
		if(tank_x + WIDTH > TankClient.frameWidth - 5) tank_x = TankClient.frameWidth - WIDTH - 5;
		if(tank_y + HEIGHT > TankClient.frameHeight - 5) tank_y = TankClient.frameHeight - HEIGHT - 5;
	    
		if(!bePlayerTank){
		Direction[] dire = Direction.values();
		dir = dire[r.nextInt(dire.length)];
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int Key = e.getKeyCode();
		switch (Key) {
		case KeyEvent.VK_RIGHT:
			bR = false;
			oldKeyss.remove(new Integer(Key));
			System.out.println(oldKeyss.size());
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			oldKeyss.remove(new Integer(Key));
			System.out.println(oldKeyss.size());
			break;
		case KeyEvent.VK_UP:
			bU = false;
			oldKeyss.remove(new Integer(Key));
			System.out.println(oldKeyss.size());
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			oldKeyss.remove(new Integer(Key));
			System.out.println(oldKeyss.size());
			break;
	
		}
		LocalDirection();
	}
	
	private boolean isOldKey(int i){
		boolean flag = false;
		for(int a = 0; a < oldKeyss.size(); a++){
			if(i == oldKeyss.get(a)){
				flag = true;
			}
		}
		return flag;
	}
	
	public void KeyPressed(KeyEvent e){
		int Key = e.getKeyCode();
			if (!isOldKey(Key) && oldKeyss.size() <= 2) {
				switch (Key) {
				case KeyEvent.VK_RIGHT:
					bR = true;
					oldKeyss.add(Key); 
					System.out.println(oldKeyss.size());
					break;
				case KeyEvent.VK_LEFT:
					bL = true;
					oldKeyss.add(Key);
					System.out.println(oldKeyss.size());
					break;
				case KeyEvent.VK_UP:
					bU = true;
					oldKeyss.add(Key);
					System.out.println(oldKeyss.size());
					break;
				case KeyEvent.VK_DOWN:
					bD = true;
					oldKeyss.add(Key);
					System.out.println(oldKeyss.size());
					break;
					
					
				case KeyEvent.VK_SPACE:
					   fire();
						break;
					
				}
				
				LocalDirection();
			
			}
	
		move();
		
		/*
		if((Key == KeyEvent.VK_RIGHT)){
			Tank_x += 5;
		}
		else if (Key == KeyEvent.VK_LEFT) {
			Tank_x += -5;
		}
		else if (Key == KeyEvent.VK_UP) {
		    Tank_y += 5;
		}
		else if (Key == KeyEvent.VK_DOWN) {
			Tank_y += -5;
		}
		*/	
	}

	private void fire(){
		 if(BulletNum <= 2){
				Bullet b = new Bullet(this, tc);
				BulletNum++;
				new Thread(b).start();
				tc.bullets.add(b);
			    }
	}
	
	private void LocalDirection() {
		if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;
		else {
			if (bL && !bU && !bR && !bD) {
				dir = Direction.L;
			} else if (bL && bU && !bR && !bD) {
				dir = Direction.LU;
			} else if (!bL && bU && !bR && !bD) {
				dir = Direction.U;
			} else if (!bL && bU && bR && !bD) {
				dir = Direction.RU;
			} else if (!bL && !bU && bR && !bD) {
				dir = Direction.R;
			} else if (!bL && !bU && bR && bD) {
				dir = Direction.RD;
			} else if (!bL && !bU && !bR && bD) {
				dir = Direction.D;
			} else if (bL && !bU && !bR && bD) {
				dir = Direction.LD;
			}	
		}
		
		if(dir != Direction.STOP){
			ptDir = dir;
		}

	}
	
	private class MakeBullet implements Runnable{
		public void run(){
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BulletNum = 0;
			}
		}
	}
	
	public void setLive(boolean live){
 		this.live = live;
	}
	
	 public Rectangle getRect(){
   	  return new Rectangle(tank_x, tank_y, WIDTH, HEIGHT);
     }
	 
	 
	 public  boolean  isMeetTanks(){
		 for(int a = 0; a < tc.tanks.size(); a++){
			 if(this.getRect().intersects(tc.tanks.get(a).getRect())){
				 return true;
			 }
		 }
		 return false;
	 }
}