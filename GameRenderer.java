import java.awt.*;

public class GameRenderer {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private static final Color MENU_BACKGROUND = new Color(0, 0, 0, 180);
    private static final Color BUTTON_COLOR = new Color(50, 50, 50, 220);
    private static final Color BUTTON_HOVER_COLOR = new Color(70, 70, 70, 220);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    public void render(Graphics g, GameState gameState) {
        drawBackground(g);

        switch (gameState.gameMod) {
            case 0: // MENU_MODE
                drawGameElements(g, gameState);
                drawMainMenu(g, gameState);
                break;
            case 1: // DIFFICULTY_SELECT
                drawGameElements(g, gameState);
                drawDifficultyMenu(g, gameState);
                break;
            case 2: // GAME_PLAYING
                drawGameElements(g, gameState);
                drawGameplayUI(g, gameState);
                drawFrontSight(g, gameState);
                break;
            case 3: // GAME_OVER
                drawGameElements(g, gameState);
                drawGameOver(g, gameState);
                break;
        }
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawMainMenu(Graphics g, GameState gameState) {
        // 半透明背景
        g.setColor(MENU_BACKGROUND);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 標題
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawShadowText(g, "Trap Shooting!", WIDTH/2, 200, Color.WHITE);

        // 繪製按鈕
        drawMenuButton(g, "Normal Mode", WIDTH/2, 350, gameState.menuButtons[0]);
        drawMenuButton(g, "Endless Mode", WIDTH/2, 420, gameState.menuButtons[1]);
    }

    private void drawDifficultyMenu(Graphics g, GameState gameState) {
        // 半透明背景
        g.setColor(MENU_BACKGROUND);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 標題
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText(g, "Select Difficulty", WIDTH/2, 200, Color.WHITE);

        // 繪製按鈕
        drawMenuButton(g, "Easy", WIDTH/2, 350, gameState.difficultyButtons[0]);
        drawMenuButton(g, "Normal", WIDTH/2, 420, gameState.difficultyButtons[1]);
        drawMenuButton(g, "Hard", WIDTH/2, 490, gameState.difficultyButtons[2]);
    }
    private void drawGameElements(Graphics g, GameState gameState) {
        // 畫背景
        gameState.background.drawBackground(g);

        // 畫靶
        gameState.T1.drawTarget(g);
        gameState.T2.drawTarget(g);
    }

    private void drawGameplayUI(Graphics g, GameState gameState) {
        drawScoreInfo(g, gameState);
        drawTimer(g, gameState.timeSec);
        drawBullets(g, gameState.bullet);
        drawComboCounter(g, gameState);

        if (gameState.isRoundEnding) {
            drawRoundEndCounter(g, gameState.roundEndCounter);
        }

        if (!gameState.canShoot) {
            drawBulletLoading(g, gameState.bullet);
        }
    }

    private void drawScoreInfo(Graphics g, GameState gameState) {
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // 分數
        drawShadowText(g, "SCORE: " + (int)gameState.score, 400, 30, Color.WHITE);
        drawShadowText(g, "BEST: " + gameState.best, 30, 30, Color.WHITE);

        // 回合資訊
        String roundInfo = gameState.isEndlessMode ?
                "Round: " + gameState.currentRound :
                "Round: " + gameState.currentRound + "/" + gameState.maxRounds;
        drawShadowText(g, roundInfo, 30, 70, Color.WHITE);

        // 命中率
        drawShadowText(g, String.format("%.2f%%", gameState.hitRate), 10, 750, Color.WHITE);

        // 分數倍率
        drawShadowText(g, String.format("x%.2f", gameState.scoreMultiplier),
                WIDTH - 100, 70, Color.YELLOW);
    }

    private void drawComboCounter(Graphics g, GameState gameState) {
        if (gameState.combo > 1) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String comboText = "COMBO x" + gameState.combo;
            drawShadowText(g, comboText, WIDTH/2, 150, Color.ORANGE);
        }
    }

    private void drawTimer(Graphics g, int timeSec) {
        g.setFont(new Font("Arial", Font.BOLD, 60));
        // 陰影效果
        g.setColor(Color.gray);
        g.drawString("" + timeSec, WIDTH/2-43, 52);
        // 主要文字
        g.setColor(Color.white);
        g.drawString("" + timeSec, WIDTH/2-45, 50);
    }

    private void drawRoundEndCounter(Graphics g, int counter) {
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String text = "Next Round in: " + counter;
        drawShadowText(g, text, WIDTH/2, HEIGHT/2, Color.RED);
    }

    private void drawMenuButton(Graphics g, String text, int x, int y, Rectangle bounds) {
        // 檢查滑鼠是否懸停在按鈕上
        boolean isHovered = bounds.contains(new Point(x, y));

        // 繪製按鈕背景
        g.setColor(isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR);
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

        // 繪製按鈕文字
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(BUTTON_TEXT_COLOR);
        drawCenteredString(g, text, bounds);
    }
    private void drawGameOver(Graphics g, GameState gameState) {
        // 半透明背景
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Game Over 文字
        g.setFont(new Font("Arial", Font.BOLD, 60));
        drawShadowText(g, "Game Over", WIDTH/2, 200, Color.RED);

        // 最終分數
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText(g, "Final Score: " + (int)gameState.score, WIDTH/2, 300, Color.WHITE);
        drawShadowText(g, "Best Score: " + gameState.best, WIDTH/2, 350, Color.WHITE);

        // 命中率
        g.setFont(new Font("Arial", Font.BOLD, 30));
        drawShadowText(g, String.format("Hit Rate: %.2f%%", gameState.hitRate),
                WIDTH/2, 400, Color.WHITE);

        // 最大連擊數
        drawShadowText(g, "Max Combo: " + gameState.maxCombo, WIDTH/2, 450, Color.ORANGE);

        // 重新開始提示
        g.setFont(new Font("Arial", Font.BOLD, 25));
        drawShadowText(g, "Press R to Return to Menu", WIDTH/2, 550, Color.WHITE);
    }

    private void drawBulletLoading(Graphics g, int bullet) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        drawShadowText(g, "Loading Bullets", 180, 300, Color.WHITE);

        // 進度條背景
        g.setColor(Color.RED);
        g.fillRect(180, 305, 220, 5);

        // 進度條
        double progress = (double)bullet / 6;
        g.setColor(Color.GREEN);
        g.fillRect(180, 305, (int)(progress * 220), 5);
    }

    public void drawBullets(Graphics g, int bulletCount) {
        for (int i = 0; i < bulletCount; i++) {
            drawBullet(300 + (i * 50), 700, g);
        }
    }

    private void drawBullet(int x, int y, Graphics g) {
        // 子彈底部
        g.setColor(Color.yellow.darker().darker());
        g.fillRect(x, y+40, 18, 10);

        // 子彈頭
        g.setColor(Color.white);
        g.fillOval(x+2, y, 14, 14);

        // 子彈身體
        g.setColor(Color.yellow.darker());
        g.fillRect(x+2, y+5, 14, 10);
        g.fillRect(x, y+10, 18, 35);
    }

    private void drawFrontSight(Graphics g, GameState gameState) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(gameState.canShoot ? Color.RED : Color.BLACK);
        g2.setStroke(new BasicStroke(3));

        // 準心圓圈
        g2.drawOval(gameState.mouseX-23, gameState.mouseY-23, 50, 50);
        g2.fillOval(gameState.mouseX-1, gameState.mouseY-1, 6, 6);

        // 準心十字線
        g2.drawLine(gameState.mouseX-30, gameState.mouseY+1, gameState.mouseX-15, gameState.mouseY+1);
        g2.drawLine(gameState.mouseX+18, gameState.mouseY+1, gameState.mouseX+33, gameState.mouseY+1);
        g2.drawLine(gameState.mouseX+1, gameState.mouseY+18, gameState.mouseX+1, gameState.mouseY+33);
        g2.drawLine(gameState.mouseX+1, gameState.mouseY-30, gameState.mouseX+1, gameState.mouseY-15);
    }
    // 輔助方法：繪製帶陰影的文字
    private void drawShadowText(Graphics g, String text, int x, int y, Color color) {
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        x = x - textWidth / 2; // 置中

        // 繪製陰影
        g.setColor(Color.BLACK);
        g.drawString(text, x + 2, y + 2);

        // 繪製主要文字
        g.setColor(color);
        g.drawString(text, x, y);
    }

    // 輔助方法：在矩形中置中繪製文字
    private void drawCenteredString(Graphics g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics();
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        // 繪製陰影
        g.setColor(Color.BLACK);
        g.drawString(text, x + 1, y + 1);

        // 繪製主要文字
        g.setColor(BUTTON_TEXT_COLOR);
        g.drawString(text, x, y);
    }

    // 輔助方法：檢查按鈕是否被懸停
    private boolean isButtonHovered(Rectangle button, int mouseX, int mouseY) {
        return button.contains(mouseX, mouseY);
    }

    // 輔助方法：繪製漸層背景
    private void drawGradientBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(0, 0, 50),
                0, HEIGHT, new Color(0, 0, 100)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
    }

    // 輔助方法：繪製圓角按鈕
    private void drawRoundButton(Graphics g, Rectangle bounds, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);

        // 添加按鈕邊框
        g2d.setColor(color.brighter());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
    }

    // 輔助方法：繪製特效
    private void drawEffects(Graphics g, GameState gameState) {
        if (gameState.combo >= 5) {
            drawComboEffect(g, gameState);
        }
    }

    private void drawComboEffect(Graphics g, GameState gameState) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        // 這裡可以添加更多視覺特效
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}
