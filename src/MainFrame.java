import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final int W;
    private final int H;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);

    private final HighScoreStore highScoreStore;
    private final SettingsStore settingsStore;
    private final Audio audio;

    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;
    private final GameOverPanel gameOverPanel;
    private final HighScorePanel highScorePanel;
    private final SettingsPanel settingsPanel;

    public MainFrame(int w, int h) {
        super("Tux Flappy");

        this.W = w;
        this.H = h;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // elore letrehozom mert kell hasznlaja a panel
        highScoreStore = new HighScoreStore();
        settingsStore = new SettingsStore();

        // az hatterzene setting alapjan menjen/ne menjen
        audio = new Audio("bg.wav");
        audio.setMusicEnabled(settingsStore.isMusicOn());

        //  panelek es viewok
        menuPanel = new MenuPanel(W, H, this);
        gamePanel = new GamePanel(W, H, this, audio);
        gameOverPanel = new GameOverPanel(W, H, this, highScoreStore);
        highScorePanel = new HighScorePanel(W, H, this, highScoreStore);
        settingsPanel = new SettingsPanel(W, H, this, settingsStore, audio);

        // cardlayout
        root.add(menuPanel, "menu");
        root.add(gamePanel, "game");
        root.add(gameOverPanel, "over");
        root.add(highScorePanel, "high");
        root.add(settingsPanel, "settings");

        setContentPane(root);
        pack(); // ablakot meretez
        setLocationRelativeTo(null);

        showMenu();
    }

    public Audio getAudio() {
        return audio;
    }

    // ezzel atvalt a menure
    public void showMenu() {
        cardLayout.show(root, "menu");
        menuPanel.requestFocusInWindow(); // itt kell bilentyuzet bementhez
    }

    //valtas a jatek kepernyore
    public void showGame() {
        cardLayout.show(root, "game");
        gamePanel.startNewRun(); // uj jatekot kezd
        gamePanel.requestFocusInWindow();
    }

    public void showGameOver(int score) {
        highScoreStore.addScore(score); // elmenti a pontszamot

        gameOverPanel.setScore(score); // bealltija a gameover panelen a kiirando pontot
        cardLayout.show(root, "over"); // atvalt
        gameOverPanel.requestFocusInWindow();
    }


    // ujratolti a scoreokat filebol
    public void showHighScores() {
        highScoreStore.reload();
        highScorePanel.refresh();
        cardLayout.show(root, "high");
        highScorePanel.requestFocusInWindow();
    }


    // betolti a panelhez a jelenlegi beallitasokat
    public void showSettings() {
        settingsPanel.refresh();
        cardLayout.show(root, "settings");
        settingsPanel.requestFocusInWindow();
    }
}
