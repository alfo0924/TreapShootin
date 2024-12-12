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

	public TrapShooting() {
		initializeGame();
		setupWindow();
		setupTimer();
	}

	private void initializeGame() {
		renderer = new GameRenderer();
		gameState = new GameState();
		gameState.T1 = new Traget(false);
		gameState.T2 = new Traget(false);
		gameState.background = new Background();
		gameState.bullet = 6;
		gameState.canShoot = true;
		gameState.mouseX = WIDTH / 2;
		gameState.mouseY = HEIGHT / 3;
		gameState.gameMod = 1;
		gameState.currentRound = 1;
		gameState.maxRounds = 4;
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

		updateTimer();
		updateTargets();
		fillBullet();

		MP.repaint();
	}

	private void updateTimer() {
		if (gameState.ticks % 50 == 0) {
			if (gameState.timeSec > 0) {
				gameState.timeSec--;
				if (gameState.timeSec == 0 && gameState.currentRound < gameState.maxRounds) {
					gameState.isRoundEnding = true;
					gameState.roundEndCounter = 5;
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
				gameState.timeSec > 0 && gameState.gameMod == 2 && gameState.canShoot) {
			gameState.T1 = new Traget(true);
			if (gameState.bullet > 2 && (int)(Math.random() * 10 + 1) > 9) {
				gameState.T2 = new Traget(true);
			}
		}

		gameState.T1.move();
		gameState.T2.move();
	}

	private void fillBullet() {
		if (!gameState.canShoot) {
			if (gameState.ticks % 15 == 0) {
				gameState.bullet++;
				if (gameState.bullet >= 6) {
					gameState.canShoot = true;
				}
			}
		}
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
	}

	@Override
	public void mousePressed(MouseEvent e) {
		gameState.clickX = e.getX()-8;
		gameState.clickY = e.getY()-37;

		handleShooting();
		handleGameStart();
		updateHitRate();
		updateBestScore();
	}

	private void handleShooting() {
		if (gameState.canShoot && gameState.timeSec > 0 && gameState.gameMod == 2) {
			if (gameState.T1.isHit(gameState.clickX, gameState.clickY)) {
				gameState.score += gameState.T1.getScore();
				gameState.hitCount++;
			}
			if (gameState.T2.isHit(gameState.clickX, gameState.clickY)) {
				gameState.score += gameState.T2.getScore();
				gameState.hitCount++;
			}
			gameState.bullet--;
			gameState.shootCount++;

			if (gameState.bullet == 0) {
				gameState.canShoot = false;
			}
		}
	}

	private void handleGameStart() {
		if (gameState.gameMod == 1) {
			gameState.gameMod = 2;
			gameState.timeSec = 5;
			gameState.ticks = 0;
		}
	}

	private void updateHitRate() {
		if (gameState.shootCount != 0) {
			gameState.hitRate = (gameState.hitCount / gameState.shootCount * 100);
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
		gameState.mouseX = e.getX()-8;
		gameState.mouseY = e.getY()-37;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R && gameState.gameMod == 2) {
			resetGame();
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
		gameState.gameMod = 2;
		gameState.currentRound = 1;
		gameState.isRoundEnding = false;
		gameState.T1 = new Traget(false);
		gameState.T2 = new Traget(false);
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
