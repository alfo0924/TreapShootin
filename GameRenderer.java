import java.awt.*;
import java.awt.geom.AffineTransform;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

public class GameRenderer {

    public static final int BUTTON_HEIGHT =60 ;
    public static final int BUTTON_SPACING =20;
    private static final int BASE_WIDTH = 600;
    private static final int BASE_HEIGHT = 800;
    private static final int BUTTON_WIDTH = 250;

    static final int FIRST_BUTTON_Y =300;
    private static final int TITLE_Y = 150;

    private double scaleX = 1.0;
    private double scaleY = 1.0;
    // 遊戲狀態常量
    public static final int MENU_MODE = 0;        // 主選單
    public static final int MODE_SELECT = 1;      // 模式選擇
    public static final int DIFFICULTY_SELECT = 2; // 難度選擇
    public static final int GAME_PLAYING = 3;     // 遊戲進行中
    public static final int GAME_OVER = 4;        // 遊戲結束


    private static final Color BUTTON_COLOR = new Color(50, 50, 50, 220);
    private static final Color BUTTON_HOVER_COLOR = new Color(70, 70, 70, 220);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color MENU_BACKGROUND = new Color(0, 0, 0, 180);



    private void drawModeSelect(Graphics g, GameState gameState) {
        // 半透明背景
        g.setColor(MENU_BACKGROUND);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 模式選擇標題
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText((Graphics2D) g, "Select Game Mode", WIDTH/2, TITLE_Y, Color.WHITE);

        // 繪製模式按鈕
        int buttonY = FIRST_BUTTON_Y;
        drawMenuButton(g, "Normal Mode", WIDTH/2, buttonY, gameState.menuButtons[0]);
        drawMenuButton(g, "Endless Mode", WIDTH/2, buttonY + BUTTON_HEIGHT + BUTTON_SPACING,
                gameState.menuButtons[1]);
    }

    private void updateScale(int currentWidth, int currentHeight) {
        scaleX = (double) currentWidth / BASE_WIDTH;
        scaleY = (double) currentHeight / BASE_HEIGHT;
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);
    }

    private void drawMainMenu(Graphics2D g, GameState gameState) {
        g.setColor(MENU_BACKGROUND);
        g.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawShadowText(g, "Trap Shooting!", BASE_WIDTH/2, 150, Color.WHITE);

        int buttonY = 300;
        drawMenuButton(g, "Normal Mode", BASE_WIDTH/2, buttonY, gameState.menuButtons[0]);
        drawMenuButton(g, "Endless Mode", BASE_WIDTH/2, buttonY + 80, gameState.menuButtons[1]);
    }

    private void drawMenuButton(Graphics g, String text, int x, int y, Rectangle bounds) {
        // 檢查滑鼠是否懸停在按鈕上
        boolean isHovered = bounds.contains(new Point(x, y));

        // 繪製按鈕背景
        g.setColor(isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR);
        g.fillRoundRect(bounds.x, bounds.y, BUTTON_WIDTH, BUTTON_HEIGHT, 15, 15);

        // 繪製按鈕文字
        g.setFont(new Font("Arial", Font.BOLD, 24));
        drawCenteredString((Graphics2D) g, text, bounds);
    }


    private void drawDifficultyMenu(Graphics2D g, GameState gameState) {
        g.setColor(MENU_BACKGROUND);
        g.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText(g, "Select Difficulty", BASE_WIDTH/2, 150, Color.WHITE);

        int buttonY = 300;
        drawMenuButton(g, "Easy", BASE_WIDTH/2, buttonY, gameState.difficultyButtons[0]);
        drawMenuButton(g, "Normal", BASE_WIDTH/2, buttonY + 80, gameState.difficultyButtons[1]);
        drawMenuButton(g, "Hard", BASE_WIDTH/2, buttonY + 160, gameState.difficultyButtons[2]);
    }

    private void drawGameElements(Graphics2D g, GameState gameState) {
        gameState.background.drawBackground(g);
        gameState.T1.drawTarget(g);
        gameState.T2.drawTarget(g);
    }

    private void drawGameplayUI(Graphics2D g, GameState gameState) {
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
    private void drawScoreInfo(Graphics2D g, GameState gameState) {
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // 分數顯示 - 左上角
        drawShadowText(g, "SCORE: " + (int)gameState.score, 30, 40, Color.WHITE);

        // 最高分 - 右上角
        String bestScore = "BEST: " + gameState.best;
        FontMetrics metrics = g.getFontMetrics();
        int bestScoreWidth = metrics.stringWidth(bestScore);
        drawShadowText(g, bestScore, BASE_WIDTH - bestScoreWidth - 30, 40, Color.WHITE);

        // 回合資訊 - 左上角第二行
        String roundInfo = gameState.isEndlessMode ?
                "Round: " + gameState.currentRound :
                "Round: " + gameState.currentRound + "/" + gameState.maxRounds;
        drawShadowText(g, roundInfo, 30, 80, Color.WHITE);

        // 命中率 - 左下角
        drawShadowText(g, String.format("%.2f%%", gameState.hitRate),
                30, BASE_HEIGHT - 60, Color.WHITE);

        // 分數倍率 - 右上角第二行
        if (gameState.scoreMultiplier > 1.0) {
            String multiplier = String.format("x%.2f", gameState.scoreMultiplier);
            drawShadowText(g, multiplier, BASE_WIDTH - 100, 80, Color.YELLOW);
        }
    }

    private void drawComboCounter(Graphics2D g, GameState gameState) {
        if (gameState.combo > 1) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String comboText = "COMBO x" + gameState.combo;

            // 計算位置使combo顯示在畫面中央上方
            FontMetrics metrics = g.getFontMetrics();
            int comboWidth = metrics.stringWidth(comboText);
            int comboX = (BASE_WIDTH - comboWidth) / 2;
            int comboY = 150;

            // 添加發光效果
            drawGlowingText(g, comboText, comboX, comboY, Color.ORANGE);
        }
    }

    private void drawGlowingText(Graphics2D g, String text, int x, int y, Color color) {
        // 發光效果
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
        for (int i = 0; i < 5; i++) {
            g.drawString(text, x - i, y);
            g.drawString(text, x + i, y);
            g.drawString(text, x, y - i);
            g.drawString(text, x, y + i);
        }

        // 主要文字
        g.setColor(color);
        g.drawString(text, x, y);
    }

    private void drawTimer(Graphics2D g, int timeSec) {
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String timeText = String.valueOf(timeSec);

        // 計算位置使計時器顯示在畫面上方中央
        FontMetrics metrics = g.getFontMetrics();
        int timeWidth = metrics.stringWidth(timeText);
        int timeX = (BASE_WIDTH - timeWidth) / 2;

        // 陰影效果
        g.setColor(Color.GRAY);
        g.drawString(timeText, timeX + 2, 52);

        // 主要文字
        g.setColor(Color.WHITE);
        g.drawString(timeText, timeX, 50);
    }
    private void drawRoundEndCounter(Graphics2D g, int gameState) {
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String text = "Next Round in: " + gameState;

        // 計算位置使文字置中
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int x = (BASE_WIDTH - textWidth) / 2;
        int y = BASE_HEIGHT / 2;

        // 添加動畫效果
        double scale = 1.0 + Math.sin(gameState * 0.1) * 0.1;
        AffineTransform oldTransform = g.getTransform();
        g.translate(x + textWidth/2, y);
        g.scale(scale, scale);
        g.translate(-(x + textWidth/2), -y);

        // 繪製文字
        drawShadowText(g, text, x + textWidth/2, y, Color.RED);

        g.setTransform(oldTransform);
    }

    private void drawBulletLoading(Graphics2D g, int gameState) {
        // 計算進度條位置
        int barWidth = 220;
        int barHeight = 5;
        int barX = (BASE_WIDTH - barWidth) / 2;
        int barY = BASE_HEIGHT / 2 + 50;

        // 繪製文字
        g.setFont(new Font("Arial", Font.BOLD, 30));
        drawShadowText(g, "Loading Bullets", BASE_WIDTH/2, barY - 20, Color.WHITE);

        // 進度條背景
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);

        // 進度條
        double progress = (double) gameState / 6;
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int)(progress * barWidth), barHeight);
    }

    private void drawBullets(Graphics2D g, int bulletCount) {
        int startX = (BASE_WIDTH - (bulletCount * 50)) / 2;
        for (int i = 0; i < bulletCount; i++) {
            drawBullet(startX + (i * 50), BASE_HEIGHT - 100, g);
        }
    }

    private void drawBullet(int x, int y, Graphics2D g) {
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

        // 添加光澤效果
        g.setColor(new Color(255, 255, 255, 50));
        g.fillRect(x+4, y+15, 4, 25);
    }

    private void drawFrontSight(Graphics2D g, GameState gameState) {
        g.setColor(gameState.canShoot ? Color.RED : Color.BLACK);
        g.setStroke(new BasicStroke(3));

        int x = gameState.mouseX;
        int y = gameState.mouseY;

        // 準心圓圈
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawOval(x-23, y-23, 50, 50);
        g.fillOval(x-1, y-1, 6, 6);

        // 準心十字線
        g.drawLine(x-30, y+1, x-15, y+1);
        g.drawLine(x+18, y+1, x+33, y+1);
        g.drawLine(x+1, y+18, x+1, y+33);
        g.drawLine(x+1, y-30, x+1, y-15);
    }
    private void drawGameOver(Graphics2D g, GameState gameState) {
        // 半透明背景
        g.setColor(MENU_BACKGROUND);
        g.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        // Game Over 文字
        g.setFont(new Font("Arial", Font.BOLD, 60));
        drawShadowText(g, "Game Over", BASE_WIDTH/2, 200, Color.RED);

        // 最終分數統計
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawShadowText(g, "Final Score: " + (int)gameState.score, BASE_WIDTH/2, 300, Color.WHITE);
        drawShadowText(g, "Best Score: " + gameState.best, BASE_WIDTH/2, 360, Color.WHITE);
        drawShadowText(g, String.format("Hit Rate: %.2f%%", gameState.hitRate),
                BASE_WIDTH/2, 420, Color.WHITE);
        drawShadowText(g, "Max Combo: " + gameState.maxCombo, BASE_WIDTH/2, 480, Color.ORANGE);

        // 重新開始提示
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String restartText = "Press R to Return to Menu";
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(restartText);
        int x = (BASE_WIDTH - textWidth) / 2;

        // 添加閃爍效果
        if ((gameState.ticks / 30) % 2 == 0) {
            drawShadowText(g, restartText, BASE_WIDTH/2, BASE_HEIGHT - 100, Color.WHITE);
        }
    }

    // 輔助方法：繪製帶陰影的文字
    private void drawShadowText(Graphics2D g, String text, int x, int y, Color color) {
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        x = x - textWidth / 2;

        // 陰影效果
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(text, x + 2, y + 2);

        // 主要文字
        g.setColor(color);
        g.drawString(text, x, y);
    }

    // 輔助方法：在矩形中置中繪製文字
    private void drawCenteredString(Graphics2D g, String text, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics();
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        // 陰影
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(text, x + 1, y + 1);

        // 主要文字
        g.setColor(BUTTON_TEXT_COLOR);
        g.drawString(text, x, y);
    }

    // 輔助方法：繪製特效
    private void drawEffects(Graphics2D g, GameState gameState) {
        if (gameState.combo >= 5) {
            drawComboEffect(g, gameState);
        }
    }

    private void drawComboEffect(Graphics2D g, GameState gameState) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        // 添加光暈效果
        int size = 100 + (int)(Math.sin(gameState.ticks * 0.1) * 20);
        g.setColor(new Color(255, 165, 0, 50));
        g.fillOval(BASE_WIDTH/2 - size/2, 150 - size/2, size, size);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    public void render(Graphics g, GameState gameState) {
        drawBackground((Graphics2D) g);

        switch (gameState.gameMod) {
            case 0: // MENU_MODE
                drawGameElements((Graphics2D) g, gameState);
                drawMainMenu((Graphics2D) g, gameState);
                break;
            case 1: // DIFFICULTY_SELECT
                drawGameElements((Graphics2D) g, gameState);
                drawDifficultyMenu((Graphics2D) g, gameState);
                break;
            case 2: // GAME_PLAYING
                drawGameElements((Graphics2D) g, gameState);
                drawGameplayUI((Graphics2D) g, gameState);
                drawFrontSight((Graphics2D) g, gameState);
                break;
            case 3: // GAME_OVER
                drawGameElements((Graphics2D) g, gameState);
                drawGameOver((Graphics2D) g, gameState);
                break;
        }
    }

}
