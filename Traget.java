import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Traget {
	
	//飛靶繪制用坐標
	private int x;
	private int y;
	//飛靶中心點
	private int cx;
	private int cy;
	//飛靶直徑
	private int d;
	//飛靶移速
	private int speed;
	//重力加速度
	private int a; 
	//飛靶方向
	private boolean right;
	//移動計步
	private int count;
	//飛靶是否存在
	private boolean isLife;
	//飛靶分數
	private int score;
	
	Traget(boolean start){
		//飛靶生成高度
		int randomY = (int)(Math.random()*100+400);
		//飛靶生成位置
		int randomX = (int)(Math.random()*10+1);
		int setSpeed = (int)(Math.random()*10+20);
		int setA = (int)(Math.random()*7+10);
		int setD = (int)(Math.random()*20+40);
		
		if(start) {
			
			switch((int)(Math.random()*3+1)) {
			case 1:
				if(randomX >5) {
					this.x = 0 ;
				}else { 
					this.x =700;
				}
				this.y = randomY;
				this.speed = setSpeed;
				this.a = setA;
				this.d = setD;
				this.score =1;
				if(setSpeed>25) this.score ++;
				if(setD <50) this.score ++;
				break;
				
			case 2:
				if(randomX >5) {
					this.x = 0 ;
				}else { 
					this.x =700;
				}
				this.y = randomY;
				this.speed = setSpeed;
				this.a = setA;
				this.d = setD;
				this.score =1;
				if(setSpeed>25) this.score ++;
				if(setD <50) this.score ++;
				break;
				
			case 3:
				if(randomX >5) {
					this.x = 0 ;
				}else { 
					this.x =700;
				}
				this.y = randomY;
				this.speed = setSpeed;
				this.a = setA;
				this.d = setD;
				this.score =1;
				if(setSpeed>25) this.score ++;
				if(setD <50) this.score ++;
				break;
				
			}
		}else {
			this.x = 800;
		}

		this.isLife = true;
				
		if( this.x < 300) {
			this.right = true;
		}else {
			this.right = false;
		}
	}
	
	public void move() {
		count ++;
		
		if(count %2 == 0) {
			
			if(right) {
				this.x += this.speed;
			}else {
				this.x -= this.speed;
			}
			
			
			if(a>0  &&  count % 2 == 0) {
				//向上
				this.y -= 2*a;
				a --;
			}else {
				//向下
				this.y += (-1*a);
				a --;
			}
		}
			
	}
	//畫飛靶
	public void drawTraget(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3));
		
		if(this.isLife) {
			
			switch(this.score) {
			//分數決定顏色
			case 1:
				g.setColor(Color.yellow);
				g.fillOval(this.getX(), this.getY(), this.d, this.d);
				
				
				break;
			case 2:
				g.setColor(Color.blue);
				g.fillOval(this.getX(), this.getY(), this.d, this.d);
				
				break;
			case 3:
				g.setColor(Color.red);
				g.fillOval(this.getX(), this.getY(), this.d, this.d);
				
				break;
			
			}
			
		}else {
			//如果擊中，飛靶碎掉
			switch(this.score) {
			
			case 1:
				g.setColor(Color.yellow);
				for(int i=0 ; i <10 ; i ++) {
					int r1 = (int)(Math.random()*this.d+1);
					int r2 = (int)(Math.random()*this.d+1);
					g.fillOval(this.getX()+r1, this.getY()+r2, 10, 10);
				}
				
				break;
			case 2:
				g.setColor(Color.blue);
				for(int i=0 ; i <10 ; i ++) {
					int r1 = (int)(Math.random()*this.d+1);
					int r2 = (int)(Math.random()*this.d+1);
					g.fillOval(this.getX()+r1, this.getY()+r2, 10, 10);
				};
				
				break;
			case 3:
				g.setColor(Color.red);
				for(int i=0 ; i <10 ; i ++) {
					int r1 = (int)(Math.random()*this.d+1);
					int r2 = (int)(Math.random()*this.d+1);
					g.fillOval(this.getX()+r1, this.getY()+r2, 10, 10);
				}
				
				break;
			
			}
		}
		
	}
	//飛靶命中判定
	public boolean isHit(int x , int y) {
		int r = this.d / 2;
		this.cx = this.x+ d/2;
		this.cy = this.y+ d/2;
		//兩點座標距離計算，小於物件半徑，即命中
		if( r > Math.sqrt( Math.pow((x-cx),2) +  Math.pow((y-cy),2) ) ) {
			System.out.println("命中");
			this.isLife = false;
			return true;
			
		}
		System.out.println("miss"+Math.sqrt( Math.pow((x-cx),2) +  Math.pow((y-cy),2) ));
		return false;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getD() {
		return this.d;
	}
	public int getScore() {
		return this.score;
	}
	public boolean getLift() {
		return this.isLife;
	}

}
