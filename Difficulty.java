public class Difficulty {
    // 難度等級枚舉
    public enum Level {
        EASY("簡單", 1.0),
        NORMAL("普通", 1.5),
        HARD("困難", 2.0);

        private final String displayName;
        private final double scoreMultiplier;

        Level(String displayName, double scoreMultiplier) {
            this.displayName = displayName;
            this.scoreMultiplier = scoreMultiplier;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getScoreMultiplier() {
            return scoreMultiplier;
        }
    }

    private Level currentLevel;
    private final DifficultySettings settings;

    public Difficulty() {
        this.currentLevel = Level.NORMAL;
        this.settings = new DifficultySettings();
        updateSettings();
    }

    // 設定難度等級
    public void setLevel(Level level) {
        this.currentLevel = level;
        updateSettings();
    }

    // 更新難度設定
    private void updateSettings() {
        switch (currentLevel) {
            case EASY:
                settings.setTargetSpeed(3);
                settings.setTargetSpawnRate(1500);
                settings.setTargetSize(50);
                settings.setZombieChance(0.1);
                settings.setMultiTargetChance(0.1);
                break;
            case NORMAL:
                settings.setTargetSpeed(5);
                settings.setTargetSpawnRate(1000);
                settings.setTargetSize(40);
                settings.setZombieChance(0.2);
                settings.setMultiTargetChance(0.2);
                break;
            case HARD:
                settings.setTargetSpeed(7);
                settings.setTargetSpawnRate(800);
                settings.setTargetSize(30);
                settings.setZombieChance(0.3);
                settings.setMultiTargetChance(0.3);
                break;
        }
    }

    // 內部類別：難度相關設定
    private static class DifficultySettings {
        private int targetSpeed;        // 目標移動速度
        private int targetSpawnRate;    // 目標生成間隔(毫秒)
        private int targetSize;         // 目標大小
        private double zombieChance;    // 殭屍靶出現機率
        private double multiTargetChance; // 多重目標出現機率

        public void setTargetSpeed(int speed) {
            this.targetSpeed = speed;
        }

        public void setTargetSpawnRate(int rate) {
            this.targetSpawnRate = rate;
        }

        public void setTargetSize(int size) {
            this.targetSize = size;
        }

        public void setZombieChance(double chance) {
            this.zombieChance = chance;
        }

        public void setMultiTargetChance(double chance) {
            this.multiTargetChance = chance;
        }
    }

    // 根據回合數調整難度
    public void adjustForRound(int round) {
        double roundMultiplier = 1.0 + (round - 1) * 0.1;
        settings.targetSpeed *= roundMultiplier;
        settings.targetSpawnRate *= (1.0 / roundMultiplier);
        settings.zombieChance = Math.min(0.5, settings.zombieChance * roundMultiplier);
        settings.multiTargetChance = Math.min(0.5, settings.multiTargetChance * roundMultiplier);
    }

    // Getters
    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getTargetSpeed() {
        return settings.targetSpeed;
    }

    public int getTargetSpawnRate() {
        return settings.targetSpawnRate;
    }

    public int getTargetSize() {
        return settings.targetSize;
    }

    public double getZombieChance() {
        return settings.zombieChance;
    }

    public double getMultiTargetChance() {
        return settings.multiTargetChance;
    }

    public double getScoreMultiplier() {
        return currentLevel.getScoreMultiplier();
    }

    // 計算最終分數
    public int calculateFinalScore(int baseScore, int round) {
        double roundBonus = 1.0 + (round - 1) * 0.2;
        return (int)(baseScore * currentLevel.getScoreMultiplier() * roundBonus);
    }

    // 取得難度描述
    public String getDifficultyDescription() {
        StringBuilder description = new StringBuilder();
        description.append("難度等級: ").append(currentLevel.getDisplayName()).append("\n");
        description.append("分數倍率: ").append(String.format("%.1f", currentLevel.getScoreMultiplier())).append("x\n");
        description.append("目標速度: ").append(settings.targetSpeed).append("\n");
        description.append("生成間隔: ").append(settings.targetSpawnRate).append("ms\n");
        description.append("目標大小: ").append(settings.targetSize).append("\n");
        description.append("殭屍靶機率: ").append(String.format("%.1f", settings.zombieChance * 100)).append("%\n");
        description.append("多重目標機率: ").append(String.format("%.1f", settings.multiTargetChance * 100)).append("%");
        return description.toString();
    }
}
