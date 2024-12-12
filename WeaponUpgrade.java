public class WeaponUpgrade {
    // 升級類型枚舉
    public enum UpgradeType {
        DAMAGE,         // 傷害升級
        CAPACITY,       // 彈藥容量升級
        RELOAD_SPEED   // 換彈速度升級
    }

    // 每種升級的基礎成本
    private static final int BASE_DAMAGE_COST = 100;
    private static final int BASE_CAPACITY_COST = 150;
    private static final int BASE_RELOAD_COST = 120;

    // 每種升級的最大等級
    private static final int MAX_DAMAGE_LEVEL = 5;
    private static final int MAX_CAPACITY_LEVEL = 3;
    private static final int MAX_RELOAD_LEVEL = 3;

    // 當前升級等級
    private int damageLevel;
    private int capacityLevel;
    private int reloadLevel;

    // 升級效果倍率
    private double damageMultiplier;
    private double capacityMultiplier;
    private double reloadSpeedMultiplier;

    public WeaponUpgrade() {
        resetUpgrades();
    }

    // 重置所有升級
    public void resetUpgrades() {
        damageLevel = 0;
        capacityLevel = 0;
        reloadLevel = 0;
        updateMultipliers();
    }

    // 更新所有倍率
    private void updateMultipliers() {
        damageMultiplier = 1.0 + (damageLevel * 0.2);      // 每級增加20%傷害
        capacityMultiplier = 1.0 + (capacityLevel * 0.3);  // 每級增加30%容量
        reloadSpeedMultiplier = 1.0 + (reloadLevel * 0.25); // 每級增加25%換彈速度
    }

    // 嘗試升級指定類型
    public boolean tryUpgrade(UpgradeType type, int availableScore) {
        int cost = getUpgradeCost(type);
        if (cost <= availableScore && canUpgrade(type)) {
            applyUpgrade(type);
            return true;
        }
        return false;
    }

    // 獲取升級成本
    public int getUpgradeCost(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                return BASE_DAMAGE_COST * (damageLevel + 1);
            case CAPACITY:
                return BASE_CAPACITY_COST * (capacityLevel + 1);
            case RELOAD_SPEED:
                return BASE_RELOAD_COST * (reloadLevel + 1);
            default:
                return Integer.MAX_VALUE;
        }
    }

    // 檢查是否可以升級
    public boolean canUpgrade(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                return damageLevel < MAX_DAMAGE_LEVEL;
            case CAPACITY:
                return capacityLevel < MAX_CAPACITY_LEVEL;
            case RELOAD_SPEED:
                return reloadLevel < MAX_RELOAD_LEVEL;
            default:
                return false;
        }
    }

    // 應用升級
    private void applyUpgrade(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                if (damageLevel < MAX_DAMAGE_LEVEL) {
                    damageLevel++;
                }
                break;
            case CAPACITY:
                if (capacityLevel < MAX_CAPACITY_LEVEL) {
                    capacityLevel++;
                }
                break;
            case RELOAD_SPEED:
                if (reloadLevel < MAX_RELOAD_LEVEL) {
                    reloadLevel++;
                }
                break;
        }
        updateMultipliers();
    }

    // 獲取升級描述
    public String getUpgradeDescription(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                return String.format("傷害 Lv.%d (%.1fx) - 下一級: %d分",
                        damageLevel, damageMultiplier, getUpgradeCost(type));
            case CAPACITY:
                return String.format("彈藥容量 Lv.%d (%.1fx) - 下一級: %d分",
                        capacityLevel, capacityMultiplier, getUpgradeCost(type));
            case RELOAD_SPEED:
                return String.format("換彈速度 Lv.%d (%.1fx) - 下一級: %d分",
                        reloadLevel, reloadSpeedMultiplier, getUpgradeCost(type));
            default:
                return "未知升級";
        }
    }

    // Getters
    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public double getCapacityMultiplier() {
        return capacityMultiplier;
    }

    public double getReloadSpeedMultiplier() {
        return reloadSpeedMultiplier;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public int getCapacityLevel() {
        return capacityLevel;
    }

    public int getReloadLevel() {
        return reloadLevel;
    }

    // 獲取特定升級類型的最大等級
    public int getMaxLevel(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                return MAX_DAMAGE_LEVEL;
            case CAPACITY:
                return MAX_CAPACITY_LEVEL;
            case RELOAD_SPEED:
                return MAX_RELOAD_LEVEL;
            default:
                return 0;
        }
    }

    // 檢查特定升級是否達到最大等級
    public boolean isMaxLevel(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                return damageLevel >= MAX_DAMAGE_LEVEL;
            case CAPACITY:
                return capacityLevel >= MAX_CAPACITY_LEVEL;
            case RELOAD_SPEED:
                return reloadLevel >= MAX_RELOAD_LEVEL;
            default:
                return true;
        }
    }

    // 獲取升級進度百分比
    public double getUpgradeProgress(UpgradeType type) {
        switch (type) {
            case DAMAGE:
                return (double) damageLevel / MAX_DAMAGE_LEVEL;
            case CAPACITY:
                return (double) capacityLevel / MAX_CAPACITY_LEVEL;
            case RELOAD_SPEED:
                return (double) reloadLevel / MAX_RELOAD_LEVEL;
            default:
                return 0.0;
        }
    }
}
