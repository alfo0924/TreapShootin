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
	private JFrame JF;
	private myPanel MP;
	static TrapShooting shootGame;
	private GameRenderer renderer;
	private GameState gameState;

	// 常數區
	public static final int WIDTH = 600;
	public static final int HEIGHT = 800;

	// 遊戲模式常數
	public static final int MENU_MODE = 0;
	public static final int DIFFICULTY_SELECT = 1;
	public static final int GAME_PLAYING = 2;
	public static final int GAME_OVER = 3;

	// 難度常數
//	public static final int EASY = 1;
//	public static final int NORMAL = 2;
//	public static final int HARD = 3;

	public TrapShooting() {
		initializeGame();
		setupWindow();
		setupTimer();
	}

	private void initializeGame() {
		renderer = new GameRenderer();
		gameState = new GameState();
		gameState.T1 = new Target(false, DifficultyLevel.NORMAL, 1);
		gameState.T2 = new Target(false, DifficultyLevel.NORMAL, 1);
		gameState.background = new Background();
		gameState.bullet = 6;
		gameState.canShoot = true;
		gameState.mouseX = WIDTH / 2;
		gameState.mouseY = HEIGHT / 3;
		gameState.gameMod = MENU_MODE;
		gameState.currentRound = 1;
		gameState.maxRounds = 4;
		gameState.difficulty = DifficultyLevel.NORMAL;
		gameState.isEndlessMode = false;
		gameState.scoreMultiplier = 1.0;
	}

	private void setupWindow() {
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
	}

	private void setupTimer() {
		Timer T = new Timer(20, this);
		T.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gameState.ticks++;

		switch (gameState.gameMod) {
			case GAME_PLAYING:
				updateGamePlay();
				break;
			case MENU_MODE:
			case DIFFICULTY_SELECT:
				updateMenu();
				break;
			case GAME_OVER:
				updateGameOver();
				break;
		}

		MP.repaint();
	}

	private void updateGamePlay() {
		updateTimer();
		updateTargets();
		fillBullet();
		updateScoreMultiplier();
	}
	private void updateTimer() {
		if (gameState.ticks % 50 == 0) {
			if (gameState.timeSec > 0) {
				gameState.timeSec--;
				if (gameState.timeSec == 0) {
					if (!gameState.isEndlessMode && gameState.currentRound >= gameState.maxRounds) {
						gameState.gameMod = GAME_OVER;
					} else {
						gameState.isRoundEnding = true;
						gameState.roundEndCounter = 5;
					}
				}
			}

			if (gameState.isRoundEnding && gameState.roundEndCounter > 0) {
				gameState.roundEndCounter--;
				if (gameState.roundEndCounter == 0) {
					startNextRound();
				}
			}
		}
	}

	private void updateTargets() {
		if ((gameState.T1.getX() > 700 || gameState.T1.getX() < -20) &&
				gameState.timeSec > 0 && gameState.gameMod == GAME_PLAYING && gameState.canShoot) {
			gameState.T1 = new Target(true, gameState.difficulty, gameState.currentRound);
			if (gameState.bullet > 2 && shouldSpawnSecondTarget()) {
				gameState.T2 = new Target(true, gameState.difficulty, gameState.currentRound);
			}
		}
		gameState.T1.move();
		gameState.T2.move();
	}


	private boolean shouldSpawnSecondTarget() {
		int chance = 9;
		switch (gameState.difficulty) {
			case EASY:
				chance = 9;
				break;
			case NORMAL:
				chance = 8;
				break;
			case HARD:
				chance = 7;
				break;
		}
		return (int)(Math.random() * 10 + 1) > chance;
	}

	private void updateScoreMultiplier() {
		// 基礎倍率
		gameState.scoreMultiplier = 1.0;

		// 難度加成
		switch (gameState.difficulty) {
			case EASY:
				gameState.scoreMultiplier *= 1.0;
				break;
			case NORMAL:
				gameState.scoreMultiplier *= 1.5;
				break;
			case HARD:
				gameState.scoreMultiplier *= 2.0;
				break;
		}

		// 回合數加成
		gameState.scoreMultiplier += (gameState.currentRound - 1) * 0.25;
	}

	private void fillBullet() {
		if (!gameState.canShoot) {
			int reloadSpeed = gameState.difficulty == DifficultyLevel.HARD ? 20 :
					gameState.difficulty == DifficultyLevel.NORMAL ? 15 : 10;
			if (gameState.ticks % reloadSpeed == 0) {
				gameState.bullet++;
				if (gameState.bullet >= 6) {
					gameState.canShoot = true;
				}
			}
		}
	}

	private void updateMenu() {
		// 選單更新邏輯
		if (gameState.gameMod == MENU_MODE) {
			checkMenuSelection();
		} else if (gameState.gameMod == DIFFICULTY_SELECT) {
			checkDifficultySelection();
		}
	}

	private void updateGameOver() {
		// 遊戲結束畫面更新邏輯
	}

	private void checkMenuSelection() {
		// 根據滑鼠位置檢查選單選擇
		int menuY = HEIGHT / 2;
		if (gameState.clickY >= menuY - 50 && gameState.clickY <= menuY + 50) {
			if (gameState.clickY <= menuY) {
				gameState.isEndlessMode = false;
			} else {
				gameState.isEndlessMode = true;
			}
			gameState.gameMod = DIFFICULTY_SELECT;
		}
	}  private void checkDifficultySelection() {
		int diffY = HEIGHT / 2;
		if (gameState.clickY >= diffY - 75 && gameState.clickY <= diffY + 75) {
			if (gameState.clickY <= diffY - 25) {
				gameState.difficulty = DifficultyLevel.EASY;
			} else if (gameState.clickY <= diffY + 25) {
				gameState.difficulty = DifficultyLevel.NORMAL;
			} else {
				gameState.difficulty = DifficultyLevel.HARD;
			}
			startGame();
		}
	}

	private void startGame() {
		gameState.gameMod = GAME_PLAYING;
		gameState.timeSec = 5;
		gameState.ticks = 0;
		resetGame();
	}

	public void repaint(Graphics g) {
		renderer.render(g, gameState);
	}

	private void startNextRound() {
		gameState.currentRound++;
		gameState.isRoundEnding = false;
		gameState.timeSec = 5;
		gameState.bullet = 6;
		gameState.canShoot = true;
		gameState.background.nextTimeOfDay();
		updateScoreMultiplier();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		gameState.clickX = e.getX() - 8;
		gameState.clickY = e.getY() - 37;

		switch (gameState.gameMod) {
			case MENU_MODE:
			case DIFFICULTY_SELECT:
				handleMenuClick();
				break;
			case GAME_PLAYING:
				handleShooting();
				updateHitRate();
				updateBestScore();
				break;
			case GAME_OVER:
				handleGameOverClick();
				break;
		}
	}

	private void handleMenuClick() {
		if (gameState.gameMod == MENU_MODE) {
			checkMenuSelection();
		} else if (gameState.gameMod == DIFFICULTY_SELECT) {
			checkDifficultySelection();
		}
	}

	private void handleGameOverClick() {
		// 檢查是否點擊重新開始按鈕
		if (isClickOnRestartButton()) {
			gameState.gameMod = MENU_MODE;
		}
	}

	private boolean isClickOnRestartButton() {
		int buttonX = WIDTH / 2 - 50;
		int buttonY = HEIGHT / 2 + 100;
		return gameState.clickX >= buttonX && gameState.clickX <= buttonX + 100 &&
				gameState.clickY >= buttonY && gameState.clickY <= buttonY + 40;
	}

	private void handleShooting() {
		if (gameState.canShoot && gameState.timeSec > 0 && gameState.gameMod == GAME_PLAYING) {
			boolean hit = false;

			if (gameState.T1.isHit(gameState.clickX, gameState.clickY)) {
				int baseScore = gameState.T1.getScore();
				gameState.score += baseScore * gameState.scoreMultiplier;
				gameState.hitCount++;
				gameState.combo++;
				hit = true;
			}
			if (gameState.T2.isHit(gameState.clickX, gameState.clickY)) {
				int baseScore = gameState.T2.getScore();
				gameState.score += baseScore * gameState.scoreMultiplier;
				gameState.hitCount++;
				gameState.combo++;
				hit = true;
			}

			if (!hit) {
				gameState.combo = 0;
			}

			gameState.bullet--;
			gameState.shootCount++;

			if (gameState.bullet == 0) {
				gameState.canShoot = false;
			}
		}
	}

	private void updateHitRate() {
		if (gameState.shootCount != 0) {
			gameState.hitRate = (gameState.hitCount / (double)gameState.shootCount * 100);
			DecimalFormat df = new DecimalFormat("##.00");
			gameState.hitRate = Double.parseDouble(df.format(gameState.hitRate));
		}
	}

	private void updateBestScore() {
		if (gameState.score > gameState.best) {
			gameState.best = gameState.score;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		gameState.mouseX = e.getX() - 8;
		gameState.mouseY = e.getY() - 37;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_R:
				if (gameState.gameMod == GAME_PLAYING || gameState.gameMod == GAME_OVER) {
					gameState.gameMod = MENU_MODE;
					resetGame();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				if (gameState.gameMod == GAME_PLAYING) {
					gameState.gameMod = MENU_MODE;
				}
				break;
		}
	}

	private void resetGame() {
		gameState.timeSec = 5;
		gameState.score = 0;
		gameState.hitCount = 0;
		gameState.shootCount = 0;
		gameState.bullet = 6;
		gameState.hitRate = 0;
		gameState.canShoot = true;
		gameState.currentRound = 1;
		gameState.isRoundEnding = false;
		gameState.combo = 0;
		gameState.scoreMultiplier = 1.0;

		// 根據難度重置目標
		gameState.T1 = new Target(false, gameState.difficulty, gameState.currentRound);
		gameState.T2 = new Target(false, gameState.difficulty, gameState.currentRound);

		// 重置背景
		gameState.background = new Background();
	}


	// 必要的空實現
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

	public static void main(String[] args) {
		shootGame = new TrapShooting();
	}
}


