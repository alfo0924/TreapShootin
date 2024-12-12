import java.awt.*;
import java.text.DecimalFormat;

public class GameState {
    // 基本遊戲數據
    public int score;
    public int best;
    public int bullet;
    public int timeSec;
    public int ticks;

    // 遊戲狀態控制
    public boolean canShoot;
    public int currentRound;
    public int maxRounds;
    public boolean isRoundEnding;
    public int roundEndCounter;
    public int gameState; // 0:主選單, 1:模式選擇, 2:難度選擇, 3:遊戲中

    // 射擊統計
    public double hitRate;
    public int hitCount;
    public int shootCount;
    public int combo;
    public int maxCombo;

    // 滑鼠位置
    public int mouseX;
    public int mouseY;
    public int clickX;
    public int clickY;

    // 遊戲物件
    public Background background;
    public Target T1;
    public Target T2;

    // 遊戲設定
    public GameMode gameMode;
    public DifficultyLevel difficulty;
    public double scoreMultiplier;

    // 選單相關
    public boolean showMenu;
    public Rectangle[] menuButtons;
    public Rectangle[] difficultyButtons;
    public boolean isEndlessMode;
    public int gameMod;

    public GameState() {
        initializeGameState();
        initializeButtons();
    }

    private void initializeGameState() {
        score = 0;
        best = 0;
        bullet = 6;
        timeSec = 5;
        ticks = 0;

        canShoot = true;
        currentRound = 1;
        maxRounds = 4;
        isRoundEnding = false;
        roundEndCounter = 0;
        gameState = 0;

        hitRate = 0.0;
        hitCount = 0;
        shootCount = 0;
        combo = 0;
        maxCombo = 0;

        mouseX = TrapShooting.WIDTH / 2;
        mouseY = TrapShooting.HEIGHT / 3;

        gameMode = null;
        difficulty = null;
        scoreMultiplier = 1.0;

        showMenu = true;
    }

    private void initializeButtons() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int startX = (TrapShooting.WIDTH - buttonWidth) / 2;

        // 初始化模式選擇按鈕
        menuButtons = new Rectangle[2];
        menuButtons[0] = new Rectangle(startX, 300, buttonWidth, buttonHeight); // 無盡模式
        menuButtons[1] = new Rectangle(startX, 370, buttonWidth, buttonHeight); // 一般模式

        // 初始化難度選擇按鈕
        difficultyButtons = new Rectangle[3];
        difficultyButtons[0] = new Rectangle(startX, 300, buttonWidth, buttonHeight); // 簡單
        difficultyButtons[1] = new Rectangle(startX, 370, buttonWidth, buttonHeight); // 普通
        difficultyButtons[2] = new Rectangle(startX, 440, buttonWidth, buttonHeight); // 困難
    }

    public void updateScoreMultiplier() {
        double roundBonus = Math.pow(1.2, currentRound - 1);
        scoreMultiplier = roundBonus * (difficulty != null ? difficulty.getScoreMultiplier() : 1.0);
    }

    public void incrementCombo() {
        combo++;
        if (combo > maxCombo) {
            maxCombo = combo;
        }
    }

    public void resetCombo() {
        combo = 0;
    }

    public void calculateHitRate() {
        if (shootCount > 0) {
            hitRate = (double) hitCount / shootCount * 100;
            DecimalFormat df = new DecimalFormat("##.00");
            hitRate = Double.parseDouble(df.format(hitRate));
        }
    }

    public boolean isEndless() {
        return gameMode == GameMode.ENDLESS;
    }

    public boolean isGameOver() {
        return !isEndless() && currentRound > maxRounds;
    }

    public void nextRound() {
        currentRound++;
        isRoundEnding = false;
        timeSec = 5;
        bullet = 6;
        canShoot = true;
        updateScoreMultiplier();
    }

    public void reset() {
        score = 0;
        hitCount = 0;
        shootCount = 0;
        bullet = 6;
        hitRate = 0;
        combo = 0;
        canShoot = true;
        currentRound = 1;
        isRoundEnding = false;
        timeSec = 5;
        T1 = new Target(false);
        T2 = new Target(false);
        updateScoreMultiplier();
    }
}
