import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Target {
	// 原有的成員變數
	private int x;
	private int y;
	private int cx;
	private int cy;
	private int d;
	private int speed;
	private int a;
	private boolean right;
	private int count;
	private boolean isLife;
	private int score;

	// 新增殭屍靶相關變數
	private boolean isZombie;
	private int health;
	private Color zombieColor;
	private int zombieMovementPattern;
	private int oscillationAmplitude;
	private double oscillationAngle;

	Target(boolean start) {
		initializeTarget(start);
		if (start && Math.random() < 0.2) { // 20% 機率生成殭屍靶
			initializeZombieTarget();
		}
	}

	private void initializeTarget(boolean start) {
		// 原有的初始化邏輯
		int randomY = (int)(Math.random() * 100 + 400);
		int randomX = (int)(Math.random() * 10 + 1);
		int setSpeed = (int)(Math.random() * 10 + 20);
		int setA = (int)(Math.random() * 7 + 10);
		int setD = (int)(Math.random() * 20 + 40);

		if (start) {
			this.x = (randomX > 5) ? 0 : 700;
			this.y = randomY;
			this.speed = setSpeed;
			this.a = setA;
			this.d = setD;
			this.score = 1;
			if (setSpeed > 25) this.score++;
			if (setD < 50) this.score++;
		} else {
			this.x = 800;
		}

		this.isLife = true;
		this.right = (this.x < 300);
	}

	private void initializeZombieTarget() {
		this.isZombie = true;
		this.health = 3;
		this.zombieColor = new Color(34, 139, 34); // 殭屍綠色
		this.score *= 2; // 殭屍靶分數加倍
		this.zombieMovementPattern = (int)(Math.random() * 3); // 隨機選擇移動模式
		this.oscillationAmplitude = 30;
		this.oscillationAngle = 0;
	}

	public void move() {
		count++;

		if (isZombie) {
			moveZombie();
		} else {
			moveNormalTarget();
		}
	}

	private void moveNormalTarget() {
		if (count % 2 == 0) {
			if (right) {
				this.x += this.speed;
			} else {
				this.x -= this.speed;
			}

			if (a > 0 && count % 2 == 0) {
				this.y -= 2 * a;
				a--;
			} else {
				this.y += (-1 * a);
				a--;
			}
		}
	}

	private void moveZombie() {
		// 殭屍靶的特殊移動模式
		switch (zombieMovementPattern) {
			case 0: // 鋸齒形移動
				if (right) {
					this.x += this.speed * 0.7;
				} else {
					this.x -= this.speed * 0.7;
				}
				this.y += Math.sin(count * 0.1) * 5;
				break;

			case 1: // 跳躍式移動
				if (count % 20 == 0) {
					this.y -= 40;
				} else if (count % 20 == 10) {
					this.y += 40;
				}
				if (right) {
					this.x += this.speed * 0.5;
				} else {
					this.x -= this.speed * 0.5;
				}
				break;

			case 2: // 螺旋式移動
				oscillationAngle += 0.1;
				if (right) {
					this.x += this.speed * 0.6;
				} else {
					this.x -= this.speed * 0.6;
				}
				this.y += Math.sin(oscillationAngle) * oscillationAmplitude * 0.1;
				break;
		}
	}

	public void drawTarget(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3));

		if (isLife) {
			if (isZombie) {
				drawZombieTarget(g2);
			} else {
				drawNormalTarget(g2);
			}
		} else {
			drawDestroyedTarget(g2);
		}
	}

	private void drawZombieTarget(Graphics2D g2) {
		// 殭屍靶的外觀
		g2.setColor(zombieColor);
		g2.fillOval(this.x, this.y, this.d, this.d);

		// 畫殭屍靶的特徵
		g2.setColor(Color.RED);
		// 眼睛
		g2.fillOval(this.x + d/4, this.y + d/3, d/6, d/6);
		g2.fillOval(this.x + d*2/3, this.y + d/3, d/6, d/6);

		// 生命值指示器
		g2.setColor(Color.RED);
		for (int i = 0; i < health; i++) {
			g2.fillRect(this.x + (i * 10), this.y - 10, 8, 5);
		}
	}

	private void drawNormalTarget(Graphics g) {
		g.setColor(getColorByScore());
		g.fillOval(this.x, this.y, this.d, this.d);
	}

	private void drawDestroyedTarget(Graphics g) {
		Color targetColor = isZombie ? zombieColor : getColorByScore();
		g.setColor(targetColor);
		for (int i = 0; i < 10; i++) {
			int r1 = (int)(Math.random() * this.d + 1);
			int r2 = (int)(Math.random() * this.d + 1);
			g.fillOval(this.x + r1, this.y + r2, 10, 10);
		}
	}

	private Color getColorByScore() {
		switch(this.score) {
			case 2: return Color.BLUE;
			case 3: return Color.RED;
			default: return Color.YELLOW;
		}
	}

	public boolean isHit(int x, int y) {
		int r = this.d / 2;
		this.cx = this.x + d/2;
		this.cy = this.y + d/2;

		if (r > Math.sqrt(Math.pow((x-cx), 2) + Math.pow((y-cy), 2))) {
			if (isZombie) {
				health--;
				if (health <= 0) {
					this.isLife = false;
				}
				return health <= 0;
			} else {
				this.isLife = false;
				return true;
			}
		}
		return false;
	}

	// Getters
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getD() { return this.d; }
	public int getScore() { return this.score; }
	public boolean getLife() { return this.isLife; }
	public boolean isZombie() { return this.isZombie; }
	public int getHealth() { return this.health; }
}
