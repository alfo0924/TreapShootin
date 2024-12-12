public class Weapon {
    // 基本屬性
    private String name;        // 武器名稱
    private int maxAmmo;       // 最大彈藥量
    private int currentAmmo;   // 目前彈藥量
    private int damage;        // 傷害值
    private int reloadSpeed;   // 換彈速度(毫秒)
    private int cost;          // 購買/升級成本
    private WeaponType type;   // 武器類型

    // 武器類型列舉
    public enum WeaponType {
        PISTOL,     // 手槍
        SMG,        // 衝鋒槍
        ROCKET,     // 火箭炮
        THROWING    // 飛刀
    }

    // 建構子
    public Weapon(WeaponType type) {
        this.type = type;
        initializeWeapon();
    }

    // 根據武器類型初始化屬性
    private void initializeWeapon() {
        switch(type) {
            case PISTOL:
                name = "手槍";
                maxAmmo = 6;
                damage = 1;
                reloadSpeed = 1000;
                cost = 0;
                break;
            case SMG:
                name = "衝鋒槍";
                maxAmmo = 30;
                damage = 1;
                reloadSpeed = 2000;
                cost = 1000;
                break;
            case ROCKET:
                name = "火箭炮";
                maxAmmo = 1;
                damage = 5;
                reloadSpeed = 3000;
                cost = 2000;
                break;
            case THROWING:
                name = "飛刀";
                maxAmmo = 10;
                damage = 2;
                reloadSpeed = 1500;
                cost = 1500;
                break;
        }
        currentAmmo = maxAmmo;
    }

    // 射擊方法
    public boolean shoot() {
        if(currentAmmo > 0) {
            currentAmmo--;
            return true;
        }
        return false;
    }

    // 重新裝填
    public void reload() {
        currentAmmo = maxAmmo;
    }

    // 升級方法
    public void upgradeDamage() {
        damage++;
    }

    public void upgradeAmmoCapacity() {
        maxAmmo += 2;
        currentAmmo = maxAmmo;
    }

    public void upgradeReloadSpeed() {
        reloadSpeed = Math.max(200, reloadSpeed - 200);
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getDamage() {
        return damage;
    }

    public int getReloadSpeed() {
        return reloadSpeed;
    }

    public int getCost() {
        return cost;
    }

    public WeaponType getType() {
        return type;
    }

    // 取得升級成本
    public int getUpgradeCost() {
        return cost / 2;
    }

    // 檢查是否需要重新裝填
    public boolean needsReload() {
        return currentAmmo == 0;
    }

    // 檢查是否可以射擊
    public boolean canShoot() {
        return currentAmmo > 0;
    }
}
