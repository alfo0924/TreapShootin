import java.awt.*;

public class GameRenderer {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;

    public void render(Graphics g, GameState gameState) {
        drawBackground(g);
        drawGameElements(g, gameState);
        drawUI(g, gameState);
        drawFrontSight(g, gameState);
    }

    private void drawBackground(Graphics g) {
        // 白底
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawGameElements(Graphics g, GameState gameState) {
        // 畫背景
        gameState.background.drawBackground(g);

        // 畫靶
        gameState.T1.drawTraget(g);
        gameState.T2.drawTraget(g);
    }

    private void drawUI(Graphics g, GameState gameState) {
        // 分數和基本資訊
        drawScoreInfo(g, gameState);

        // 時間
        drawTimer(g, gameState.timeSec);

        // 輪次結束倒數
        if (gameState.isRoundEnding) {
            drawRoundEndCounter(g, gameState.roundEndCounter);
        }

        // 遊戲結束
        if (gameState.timeSec <= 0 && gameState.gameMod == 2 && gameState.currentRound >= gameState.maxRounds) {
            drawGameOver(g);
        }

        // 畫子彈
        drawBullets(g, gameState.bullet);

        // 子彈裝填提示
        if (!gameState.canShoot) {
            drawBulletLoading(g, gameState.bullet);
        }

        // 開場文字
        if (gameState.gameMod == 1) {
            drawStartScreen(g);
        }
    }

    private void drawScoreInfo(Graphics g, GameState gameState) {
        g.setColor(Color.white);
        g.setFont(new Font("", Font.BOLD, 30));
        g.drawString("SCORE: " + gameState.score, 400, 30);
        g.drawString("BEST: " + gameState.best, 30, 30);
        g.drawString("Round: " + gameState.currentRound + "/" + gameState.maxRounds, 30, 70);
        g.drawString(gameState.hitRate + "%", 10, 750);
    }

    private void drawTimer(Graphics g, int timeSec) {
        g.setFont(new Font("", Font.BOLD, 60));
        g.setColor(Color.gray);
        g.drawString("" + timeSec, WIDTH/2-43, 52);
        g.setColor(Color.white);
        g.drawString("" + timeSec, WIDTH/2-45, 50);
    }

    private void drawRoundEndCounter(Graphics g, int counter) {
        g.setFont(new Font("", Font.BOLD, 60));
        g.setColor(Color.red);
        g.drawString("Next Round in: " + counter, WIDTH/2-200, HEIGHT/2);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Game Over", WIDTH/2-150, 250);
        g.setColor(Color.red);
        g.drawString("Game Over", WIDTH/2-152, 248);

        g.setFont(new Font("", Font.BOLD, 30));
        g.setColor(Color.black);
        g.drawString("Type -R- New Game", 152, 502);
        g.setColor(Color.white);
        g.drawString("Type -R- New Game", 150, 500);
    }

    private void drawBulletLoading(Graphics g, int bullet) {
        g.setColor(Color.gray);
        g.setFont(new Font("", Font.BOLD, 30));
        g.drawString("Loading Bullets", 180, 300);
        g.setColor(Color.red);
        g.fillRect(180, 305, 220, 5);
        double x = (double)bullet / 6;
        g.setColor(Color.green);
        g.fillRect(180, 305, (int)(x*220), 5);
    }

    private void drawStartScreen(Graphics g) {
        g.setFont(new Font("", Font.BOLD, 50));
        g.setColor(Color.black);
        g.drawString("Trap Shooting!", WIDTH/2-167, 283);
        g.setColor(Color.blue);
        g.drawString("Trap Shooting!", WIDTH/2-170, 280);
        g.setColor(Color.white);
        g.setFont(new Font("", Font.BOLD, 40));
        g.drawString("- Click to Start -", WIDTH/2-150, 450);
    }

    public void drawBullets(Graphics g, int bulletCount) {
        for (int i = 0; i < bulletCount; i++) {
            drawBullet(300 + (i * 50), 700, g);
        }
    }

    private void drawBullet(int x, int y, Graphics g) {
        g.setColor(Color.yellow.darker().darker());
        g.fillRect(x, y+40, 18, 10);
        g.setColor(Color.white);
        g.fillOval(x+2, y, 14, 14);
        g.setColor(Color.yellow.darker());
        g.fillRect(x+2, y+5, 14, 10);
        g.fillRect(x, y+10, 18, 35);
    }

    private void drawFrontSight(Graphics g, GameState gameState) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(gameState.canShoot ? Color.red : Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(gameState.mouseX-23, gameState.mouseY-23, 50, 50);
        g2.fillOval(gameState.mouseX-1, gameState.mouseY-1, 6, 6);
        g2.drawLine(gameState.mouseX-30, gameState.mouseY+1, gameState.mouseX-15, gameState.mouseY+1);
        g2.drawLine(gameState.mouseX+18, gameState.mouseY+1, gameState.mouseX+33, gameState.mouseY+1);
        g2.drawLine(gameState.mouseX+1, gameState.mouseY+18, gameState.mouseX+1, gameState.mouseY+33);
        g2.drawLine(gameState.mouseX+1, gameState.mouseY-30, gameState.mouseX+1, gameState.mouseY-15);
    }
}

