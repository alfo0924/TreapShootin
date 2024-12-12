import java.awt.*;

public class MenuScreen {
    // 常數定義
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int PADDING = 20;

    // 選單狀態
    private enum MenuState {
        MAIN,           // 主選單
        MODE_SELECT,    // 模式選擇
        DIFFICULTY,     // 難度選擇
        WEAPON_SELECT   // 武器選擇
    }

    // 界面元素
    private Rectangle[] buttons;
    private String[] buttonTexts;
    private MenuState currentState;
    private Color backgroundColor;
    private Color buttonColor;
    private Color textColor;
    private Font titleFont;
    private Font buttonFont;

    // 遊戲設定
    private GameMode.Mode selectedMode;
    private GameMode.Difficulty selectedDifficulty;
    private Weapon selectedWeapon;

    public MenuScreen() {
        initializeComponents();
        setMainMenu();
    }

    private void initializeComponents() {
        backgroundColor = new Color(40, 44, 52);
        buttonColor = new Color(97, 175, 239);
        textColor = Color.WHITE;
        titleFont = new Font("Arial", Font.BOLD, 36);
        buttonFont = new Font("Arial", Font.PLAIN, 20);
        currentState = MenuState.MAIN;
        buttons = new Rectangle[4];
        buttonTexts = new String[4];
    }

    private void setMainMenu() {
        currentState = MenuState.MAIN;
        buttonTexts = new String[]{"開始遊戲", "設定", "排行榜", "退出"};
        updateButtonPositions();
    }

    private void setModeSelect() {
        currentState = MenuState.MODE_SELECT;
        buttonTexts = new String[]{"一般模式", "無盡模式", "返回", ""};
        updateButtonPositions();
    }

    private void setDifficultySelect() {
        currentState = MenuState.DIFFICULTY;
        buttonTexts = new String[]{"簡單", "普通", "困難", "返回"};
        updateButtonPositions();
    }

    private void setWeaponSelect() {
        currentState = MenuState.WEAPON_SELECT;
        buttonTexts = new String[]{"手槍", "衝鋒槍", "火箭炮", "飛刀"};
        updateButtonPositions();
    }

    private void updateButtonPositions() {
        int startY = 300;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Rectangle(
                    (TrapShooting.WIDTH - BUTTON_WIDTH) / 2,
                    startY + (BUTTON_HEIGHT + PADDING) * i,
                    BUTTON_WIDTH,
                    BUTTON_HEIGHT
            );
        }
    }

    public void draw(Graphics2D g) {
        // 繪製背景
        g.setColor(backgroundColor);
        g.fillRect(0, 0, TrapShooting.WIDTH, TrapShooting.HEIGHT);

        // 繪製標題
        g.setFont(titleFont);
        g.setColor(textColor);
        String title = getTitleText();
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (TrapShooting.WIDTH - titleWidth) / 2, 200);

        // 繪製按鈕
        g.setFont(buttonFont);
        for (int i = 0; i < buttons.length; i++) {
            if (buttonTexts[i].isEmpty()) continue;

            Rectangle button = buttons[i];
            // 繪製按鈕背景
            g.setColor(buttonColor);
            g.fill(button);

            // 繪製按鈕文字
            g.setColor(textColor);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(buttonTexts[i]);
            int textHeight = fm.getHeight();
            g.drawString(buttonTexts[i],
                    button.x + (button.width - textWidth) / 2,
                    button.y + (button.height + textHeight) / 2 - 5);
        }
    }

    private String getTitleText() {
        switch (currentState) {
            case MAIN:
                return "打靶遊戲";
            case MODE_SELECT:
                return "選擇遊戲模式";
            case DIFFICULTY:
                return "選擇難度";
            case WEAPON_SELECT:
                return "選擇武器";
            default:
                return "";
        }
    }

    public boolean handleClick(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(x, y) && !buttonTexts[i].isEmpty()) {
                return processButtonClick(i);
            }
        }
        return false;
    }

    private boolean processButtonClick(int buttonIndex) {
        switch (currentState) {
            case MAIN:
                return handleMainMenuClick(buttonIndex);
            case MODE_SELECT:
                return handleModeSelectClick(buttonIndex);
            case DIFFICULTY:
                return handleDifficultyClick(buttonIndex);
            case WEAPON_SELECT:
                return handleWeaponSelectClick(buttonIndex);
            default:
                return false;
        }
    }

    private boolean handleMainMenuClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0: // 開始遊戲
                setModeSelect();
                return false;
            case 1: // 設定
                // TODO: 實現設定功能
                return false;
            case 2: // 排行榜
                // TODO: 實現排行榜功能
                return false;
            case 3: // 退出
                System.exit(0);
                return false;
            default:
                return false;
        }
    }

    private boolean handleModeSelectClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0: // 一般模式
                selectedMode = GameMode.Mode.NORMAL;
                setDifficultySelect();
                return false;
            case 1: // 無盡模式
                selectedMode = GameMode.Mode.ENDLESS;
                setDifficultySelect();
                return false;
            case 2: // 返回
                setMainMenu();
                return false;
            default:
                return false;
        }
    }

    private boolean handleDifficultyClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0: // 簡單
                selectedDifficulty = GameMode.Difficulty.EASY;
                setWeaponSelect();
                return false;
            case 1: // 普通
                selectedDifficulty = GameMode.Difficulty.NORMAL;
                setWeaponSelect();
                return false;
            case 2: // 困難
                selectedDifficulty = GameMode.Difficulty.HARD;
                setWeaponSelect();
                return false;
            case 3: // 返回
                setModeSelect();
                return false;
            default:
                return false;
        }
    }

    private boolean handleWeaponSelectClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0: // 手槍
                selectedWeapon = new Weapon(Weapon.WeaponType.PISTOL);
                return true;
            case 1: // 衝鋒槍
                selectedWeapon = new Weapon(Weapon.WeaponType.SMG);
                return true;
            case 2: // 火箭炮
                selectedWeapon = new Weapon(Weapon.WeaponType.ROCKET);
                return true;
            case 3: // 飛刀
                selectedWeapon = new Weapon(Weapon.WeaponType.THROWING);
                return true;
            default:
                return false;
        }
    }

    // Getters for selected options
    public GameMode.Mode getSelectedMode() {
        return selectedMode;
    }

    public GameMode.Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public Weapon getSelectedWeapon() {
        return selectedWeapon;
    }
}
