public class GameMode {
    // 遊戲模式枚舉
    public enum Mode {
        ENDLESS,    // 無盡模式
        NORMAL      // 一般模式(4回合)
    }

    // 遊戲難度枚舉
    public enum Difficulty {
        EASY(1.0),      // 簡單難度
        NORMAL(1.5),    // 普通難度
        HARD(2.0);      // 困難難度

        private final double scoreMultiplier;

        Difficulty(double multiplier) {
            this.scoreMultiplier = multiplier;
        }

        public double getScoreMultiplier() {
            return scoreMultiplier;
        }
    }

    // 遊戲模式相關屬性
    private Mode currentMode;
    private Difficulty currentDifficulty;
    private int currentRound;
    private int maxRounds;
    private double roundScoreMultiplier;

    // 構造函數
    public GameMode() {
        this.currentMode = Mode.NORMAL;
        this.currentDifficulty = Difficulty.NORMAL;
        this.currentRound = 1;
        this.maxRounds = 4;
        this.roundScoreMultiplier = 1.0;
    }

    // 設置遊戲模式
    public void setMode(Mode mode) {
        this.currentMode = mode;
        if (mode == Mode.ENDLESS) {
            this.maxRounds = Integer.MAX_VALUE;
        } else {
            this.maxRounds = 4;
        }
    }

    // 設置難度
    public void setDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }

    // 進入下一回合
    public boolean nextRound() {
        if (currentRound < maxRounds) {
            currentRound++;
            updateRoundScoreMultiplier();
            return true;
        }
        return false;
    }

    // 更新回合分數倍率
    private void updateRoundScoreMultiplier() {
        roundScoreMultiplier = 1.0 + (currentRound - 1) * 0.5;
    }

    // 計算最終分數
    public int calculateFinalScore(int baseScore) {
        return (int)(baseScore * currentDifficulty.getScoreMultiplier() * roundScoreMultiplier);
    }

    // 重置遊戲
    public void reset() {
        this.currentRound = 1;
        this.roundScoreMultiplier = 1.0;
    }

    // Getters
    public Mode getCurrentMode() {
        return currentMode;
    }

    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public double getRoundScoreMultiplier() {
        return roundScoreMultiplier;
    }

    // 檢查遊戲是否結束
    public boolean isGameOver() {
        return currentMode == Mode.NORMAL && currentRound >= maxRounds;
    }

    // 獲取當前回合的目標生成速度
    public double getTargetSpawnRate() {
        double baseRate = 1.0;
        switch (currentDifficulty) {
            case EASY:
                baseRate = 0.7;
                break;
            case NORMAL:
                baseRate = 1.0;
                break;
            case HARD:
                baseRate = 1.5;
                break;
        }
        return baseRate * (1 + (currentRound - 1) * 0.2);
    }

    // 獲取當前回合的目標移動速度
    public double getTargetSpeed() {
        double baseSpeed = 1.0;
        switch (currentDifficulty) {
            case EASY:
                baseSpeed = 0.8;
                break;
            case NORMAL:
                baseSpeed = 1.0;
                break;
            case HARD:
                baseSpeed = 1.3;
                break;
        }
        return baseSpeed * (1 + (currentRound - 1) * 0.15);
    }
}
