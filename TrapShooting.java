import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrapShooting implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
	// 物件區
	JFrame JF;
	myPanel MP;
	static TrapShooting shootGame;
	Traget T1 = new Traget(false);
	Traget T2 = new Traget(false);
	Background background = new Background();

	// 常數區
	public static final int WIDTH = 600;
	public static final int HEIGHT = 800;
	public int score;
	public int best; //最高分
	public int bullet = 6;
	public boolean canShoot = true;
	public int timeSec = 0;
	public double shootCount = 0;
	public double hitCount = 0;
	public double hitRate = 0;

	// 控制區
	public static int mouseX = WIDTH / 2;
	public static int mouseY = HEIGHT / 3;
	public static int clickX;
	public static int clickY;
	public int ticks;
	public int gameMod = 1; // 1遊戲開始畫面 2遊戲執行

	// 輪次相關變數
	private int currentRound = 1;
	private int maxRounds = 4;
	private boolean isRoundEnding = false;
	private int roundEndCounter = 5;

	TrapShooting() {
		JF = new JFrame("Trap Game");
		MP = new myPanel();

		JF.setBounds(100, 100, WIDTH, HEIGHT);
		JF.setVisible(true);
		JF.setResizable(false);
		JF.setAlwaysOnTop(true);
		JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JF.add(MP);
		JF.addMouseListener(this);
		JF.addMouseMotionListener(this);
		JF.addKeyListener(this);

		Timer T = new Timer(20, this);
		T.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;

		// 時間倒數
		if (ticks % 50 == 0) {
			if (timeSec > 0) {
				timeSec--;
				if (timeSec == 0 && currentRound < maxRounds) {
					isRoundEnding = true;
					roundEndCounter = 5;
				}
			}

			// 處理輪次結束倒數
			if (isRoundEnding && roundEndCounter > 0) {
				roundEndCounter--;
				if (roundEndCounter == 0) {
					startNextRound();
				}
			}
		}

		// 飛靶生成
		if ((T1.getX() > 700 || T1.getX() < -20) && timeSec > 0 && gameMod == 2 && canShoot) {
			T1 = new Traget(true);
			if (bullet > 2 && (int)(Math.random() * 10 + 1) > 9) {
				T2 = new Traget(true);
			}
		}

		// 飛靶移動
		T1.move();
		T2.move();

		// 填充子彈
		fillBullet();

		MP.repaint();
	}

	// 填充子彈
	public void fillBullet() {
		if (!canShoot) {
			if (ticks % 15 == 0) {
				bullet++;
				if (bullet >= 6) {
					canShoot = true;
				}
			}
		}
	}
	public void repaint(Graphics g) {
		// 白底
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// 畫背景
		background.drawBackground(g);

		// 畫靶
		T1.drawTraget(g);
		T2.drawTraget(g);

		// 畫分數、時間、命中率
		g.setColor(Color.white);
		g.setFont(new Font("", 1, 30));
		g.drawString("SCORE: " + score, 400, 30);
		g.drawString("BEST: " + best, 30, 30);
		g.drawString("Round: " + currentRound + "/" + maxRounds, 30, 70);
		g.drawString(hitRate + "%", 10, 750);

		g.setFont(new Font("", 1, 60));
		g.setColor(Color.gray);
		g.drawString("" + timeSec, WIDTH/2-43, 52);
		g.setColor(Color.white);
		g.drawString("" + timeSec, WIDTH/2-45, 50);

		// 輪次結束倒數
		if (isRoundEnding) {
			g.setFont(new Font("", 1, 60));
			g.setColor(Color.red);
			g.drawString("Next Round in: " + roundEndCounter, WIDTH/2-200, HEIGHT/2);
		}

		// 時間結束
		if (timeSec <= 0 && gameMod == 2 && currentRound >= maxRounds) {
			g.setColor(Color.white);
			g.drawString("Game Over", WIDTH/2-150, 250);
			g.setColor(Color.red);
			g.drawString("Game Over", WIDTH/2-152, 248);

			g.setFont(new Font("", 1, 30));
			g.setColor(Color.black);
			g.drawString("Type -R- New Game", 152, 502);
			g.setColor(Color.white);
			g.drawString("Type -R- New Game", 150, 500);
		}

		// 畫子彈
		bullets(bullet, g);
		if (!canShoot) {
			g.setColor(Color.gray);
			g.setFont(new Font("", 1, 30));
			g.drawString("Loading Bullets", 180, 300);
			g.setColor(Color.red);
			g.fillRect(180, 305, 220, 5);
			double x = (double)bullet / 6;
			g.setColor(Color.green);
			g.fillRect(180, 305, (int)(x*220), 5);
		}

		// 開場文字
		if (gameMod == 1) {
			g.setFont(new Font("", 1, 50));
			g.setColor(Color.black);
			g.drawString("Trap Shooting!", WIDTH/2-167, 283);
			g.setColor(Color.blue);
			g.drawString("Trap Shooting!", WIDTH/2-170, 280);
			g.setColor(Color.white);
			g.setFont(new Font("", 1, 40));
			g.drawString("- Click to Start -", WIDTH/2-150, 450);
		}

		// 畫準星
		FrontSight(g);
	}
	// 畫準星
	public void FrontSight(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (canShoot) {
			g2.setColor(Color.red);
		} else {
			g2.setColor(Color.black);
		}
		g2.setStroke(new BasicStroke(3));
		g2.drawOval(mouseX-23, mouseY-23, 50, 50);
		g2.fillOval(mouseX-1, mouseY-1, 6, 6);
		g2.drawLine(mouseX-30, mouseY+1, mouseX-15, mouseY+1);
		g2.drawLine(mouseX+18, mouseY+1, mouseX+33, mouseY+1);
		g2.drawLine(mouseX+1, mouseY+18, mouseX+1, mouseY+33);
		g2.drawLine(mouseX+1, mouseY-30, mouseX+1, mouseY-15);
	}

	// 畫現有子彈數量
	public void bullets(int bullet, Graphics g) {
		switch(bullet) {
			case 6:
				bullet(300, 700, g);
			case 5:
				bullet(350, 700, g);
			case 4:
				bullet(400, 700, g);
			case 3:
				bullet(450, 700, g);
			case 2:
				bullet(500, 700, g);
			case 1:
				bullet(550, 700, g);
			case 0:
		}
	}

	// 子彈模型
	public void bullet(int x, int y, Graphics g) {
		g.setColor(Color.yellow.darker().darker());
		g.fillRect(x, y+40, 18, 10);
		g.setColor(Color.white);
		g.fillOval(x+2, y, 14, 14);
		g.setColor(Color.yellow.darker());
		g.fillRect(x+2, y+5, 14, 10);
		g.fillRect(x, y+10, 18, 35);
	}

	private void startNextRound() {
		currentRound++;
		isRoundEnding = false;
		timeSec = 5;
		bullet = 6;
		canShoot = true;
		background.nextTimeOfDay();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// 射擊點
		clickX = e.getX()-8;
		clickY = e.getY()-37;

		// 射擊判定
		if (canShoot && timeSec > 0 && gameMod == 2) {
			if (T1.isHit(clickX, clickY)) {
				score += T1.getScore();
				hitCount++;
			}
			if (T2.isHit(clickX, clickY)) {
				score += T2.getScore();
				hitCount++;
			}
			bullet--;
			shootCount++;
		}

		// 點擊進入遊戲
		if (gameMod == 1) {
			gameMod = 2;
			timeSec = 5;
			ticks = 0;
		}

		// 沒子彈不能射擊
		if (bullet == 0) {
			canShoot = false;
		}

		// 命中率計算
		if (shootCount != 0) {
			hitRate = (hitCount / shootCount * 100);
			DecimalFormat df = new DecimalFormat("##.00");
			hitRate = Double.parseDouble(df.format(hitRate));
		}

		// 最高分計算
		if (score > best) {
			best = score;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX()-8;
		mouseY = e.getY()-37;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseDragged(MouseEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {
		// 重新開始 - 修改判斷條件，移除timeSec == 0的限制
		if (e.getKeyCode() == KeyEvent.VK_R && gameMod == 2) {
			// 重置所有遊戲數據
			timeSec = 15;
			score = 0;
			hitCount = 0;
			shootCount = 0;
			bullet = 6;
			hitRate = 0;
			canShoot = true;
			gameMod = 2;
			currentRound = 1;
			isRoundEnding = false;

			// 重置飛靶
			T1 = new Traget(false);
			T2 = new Traget(false);
		}
	}


	public static void main(String[] args) {
		shootGame = new TrapShooting();
	}
}
