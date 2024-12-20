import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Target {
	// 基本屬性
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
	private DifficultyLevel difficulty;

	// 殭屍靶相關
	private boolean isZombie;
	private int health;
	private Color zombieColor;
	private int zombieMovementPattern;
	private int oscillationAmplitude;
	private double oscillationAngle;
	private int currentRound;

	public Target(boolean start, DifficultyLevel difficulty, int currentRound) {
		this.difficulty = difficulty;
		this.currentRound = currentRound;
		initializeTarget(start);
		if (start && shouldBeZombie()) {
			initializeZombieTarget();
		}
	}

	public Target(boolean start) {
	}

	private boolean shouldBeZombie() {
		double zombieChance = 0.2; // 基礎機率 20%

		// 根據難度調整機率
		switch (difficulty) {
			case EASY:
				zombieChance = 0.1;
				break;
			case NORMAL:
				zombieChance = 0.2;
				break;
			case HARD:
				zombieChance = 0.3;
				break;
		}

		// 根據回合數增加機率
		zombieChance += (currentRound - 1) * 0.05;

		return Math.random() < zombieChance;
	}

	private void initializeTarget(boolean start) {
		if (start) {
			setupActiveTarget();
		} else {
			this.x = 800;
		}
		this.isLife = true;
		this.right = (this.x < 300);
	}

	private void setupActiveTarget() {
		int randomY = (int)(Math.random() * 100 + 400);
		int randomX = (int)(Math.random() * 10 + 1);

		// 根據難度調整速度和大小
		switch (difficulty) {
			case EASY:
				this.speed = (int)(Math.random() * 5 + 15);
				this.d = (int)(Math.random() * 10 + 50);
				break;
			case NORMAL:
				this.speed = (int)(Math.random() * 10 + 20);
				this.d = (int)(Math.random() * 20 + 40);
				break;
			case HARD:
				this.speed = (int)(Math.random() * 15 + 25);
				this.d = (int)(Math.random() * 15 + 35);
				break;
		}

		this.x = (randomX > 5) ? 0 : 700;
		this.y = randomY;
		this.a = (int)(Math.random() * 7 + 10);

		// 基礎分數計算
		this.score = calculateBaseScore();
	}

	private int calculateBaseScore() {
		int baseScore = 1;

		// 速度加分
		if (speed > 25) baseScore++;
		if (speed > 30) baseScore++;

		// 大小加分
		if (d < 50) baseScore++;
		if (d < 40) baseScore++;

		// 難度加分
		switch (difficulty) {
			case EASY:
				break;
			case NORMAL:
				baseScore *= 1.5;
				break;
			case HARD:
				baseScore *= 2;
				break;
		}

		// 回合加分
		baseScore += (currentRound - 1) * 0.5;

		return baseScore;
	}

	private void initializeZombieTarget() {
		this.isZombie = true;
		this.health = difficulty == DifficultyLevel.HARD ? 4 :
				difficulty == DifficultyLevel.NORMAL ? 3 : 2;
		this.zombieColor = new Color(34, 139, 34);
		this.score *= 2;
		this.zombieMovementPattern = (int)(Math.random() * 3);
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

		// 根據難度調整額外移動
		applyDifficultyMovement();
	}

	private void moveNormalTarget() {
		if (count % 2 == 0) {
			// 基本水平移動
			if (right) {
				this.x += this.speed;
			} else {
				this.x -= this.speed;
			}

			// 垂直移動
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
		double speedMultiplier = getSpeedMultiplier();

		switch (zombieMovementPattern) {
			case 0: // 鋸齒形移動
				if (right) {
					this.x += this.speed * 0.7 * speedMultiplier;
				} else {
					this.x -= this.speed * 0.7 * speedMultiplier;
				}
				this.y += Math.sin(count * 0.1) * 5 * speedMultiplier;
				break;

			case 1: // 跳躍式移動
				if (count % 20 == 0) {
					this.y -= 40 * speedMultiplier;
				} else if (count % 20 == 10) {
					this.y += 40 * speedMultiplier;
				}
				if (right) {
					this.x += this.speed * 0.5 * speedMultiplier;
				} else {
					this.x -= this.speed * 0.5 * speedMultiplier;
				}
				break;

			case 2: // 螺旋式移動
				oscillationAngle += 0.1 * speedMultiplier;
				if (right) {
					this.x += this.speed * 0.6 * speedMultiplier;
				} else {
					this.x -= this.speed * 0.6 * speedMultiplier;
				}
				this.y += Math.sin(oscillationAngle) * oscillationAmplitude * 0.1 * speedMultiplier;
				break;
		}
	}

	private double getSpeedMultiplier() {
		switch (difficulty) {
			case EASY:
				return 0.8;
			case NORMAL:
				return 1.0;
			case HARD:
				return 1.3;
			default:
				return 1.0;
		}
	}

	private void applyDifficultyMovement() {
		switch (difficulty) {
			case HARD:
				// 困難模式下的額外隨機移動
				if (count % 30 == 0) {
					this.y += (Math.random() - 0.5) * 20;
				}
				break;
			case NORMAL:
				// 普通模式下的輕微擾動
				if (count % 45 == 0) {
					this.y += (Math.random() - 0.5) * 10;
				}
				break;
			case EASY:
				// 容易模式下保持穩定移動
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
			// 如果是困難模式，添加特殊視覺效果
			if (difficulty == DifficultyLevel.HARD) {
				drawDifficultyEffects(g2);
			}
		} else {
			drawDestroyedTarget(g2);
		}
	}

	private void drawZombieTarget(Graphics2D g2) {
		// 殭屍靶的基本外觀
		g2.setColor(zombieColor);
		g2.fillOval(this.x, this.y, this.d, this.d);

		// 根據難度調整殭屍靶外觀
		switch (difficulty) {
			case HARD:
				drawHardZombieFeatures(g2);
				break;
			case NORMAL:
				drawNormalZombieFeatures(g2);
				break;
			case EASY:
				drawEasyZombieFeatures(g2);
				break;
		}

		// 生命值指示器
		drawHealthBar(g2);
	}

	private void drawHealthBar(Graphics2D g2) {
		int barWidth = 8;
		int barHeight = 5;
		int spacing = 2;
		int totalWidth = (barWidth + spacing) * health;
		int startX = this.x + (this.d - totalWidth) / 2;

		g2.setColor(Color.RED);
		for (int i = 0; i < health; i++) {
			g2.fillRect(startX + (i * (barWidth + spacing)), this.y - 10, barWidth, barHeight);
		}
	}

	private void drawHardZombieFeatures(Graphics2D g2) {
		// 紅色發光眼睛
		g2.setColor(Color.RED);
		g2.fillOval(this.x + d/4, this.y + d/3, d/5, d/5);
		g2.fillOval(this.x + d*2/3, this.y + d/3, d/5, d/5);

		// 添加牙齒
		g2.setColor(Color.WHITE);
		int teethWidth = d/8;
		g2.fillPolygon(
				new int[]{this.x + d/3, this.x + d/3 + teethWidth, this.x + d/3 + teethWidth/2},
				new int[]{this.y + d*2/3, this.y + d*2/3, this.y + d*2/3 + teethWidth},
				3
		);
	}

	private void drawNormalZombieFeatures(Graphics2D g2) {
		// 普通的眼睛
		g2.setColor(Color.RED);
		g2.fillOval(this.x + d/4, this.y + d/3, d/6, d/6);
		g2.fillOval(this.x + d*2/3, this.y + d/3, d/6, d/6);
	}

	private void drawEasyZombieFeatures(Graphics2D g2) {
		// 簡單的眼睛
		g2.setColor(Color.RED);
		g2.fillOval(this.x + d/4, this.y + d/3, d/7, d/7);
		g2.fillOval(this.x + d*2/3, this.y + d/3, d/7, d/7);
	}

	private void drawNormalTarget(Graphics2D g2) {
		g2.setColor(getColorByScore());
		g2.fillOval(this.x, this.y, this.d, this.d);
	}

	private void drawDestroyedTarget(Graphics2D g2) {
		Color targetColor = isZombie ? zombieColor : getColorByScore();
		g2.setColor(targetColor);

		// 爆炸效果
		int particles = difficulty == DifficultyLevel.HARD ? 15 :
				difficulty == DifficultyLevel.NORMAL ? 10 : 5;

		for (int i = 0; i < particles; i++) {
			int r1 = (int)(Math.random() * this.d + 1);
			int r2 = (int)(Math.random() * this.d + 1);
			int particleSize = (int)(Math.random() * 10 + 5);
			g2.fillOval(this.x + r1, this.y + r2, particleSize, particleSize);
		}
	}

	private void drawDifficultyEffects(Graphics2D g2) {
		// 困難模式特殊視覺效果
		g2.setColor(new Color(255, 0, 0, 50));
		g2.drawOval(this.x - 5, this.y - 5, this.d + 10, this.d + 10);
	}

	private Color getColorByScore() {
		switch(this.score) {
			case 2: return Color.BLUE;
			case 3: return Color.RED;
			case 4: return Color.MAGENTA;
			case 5: return Color.GREEN;
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
	public DifficultyLevel getDifficulty() { return this.difficulty; }

    public double getSpeed() {
        return 0;
    }
}
