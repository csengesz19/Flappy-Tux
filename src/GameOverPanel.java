import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class GameOverPanel extends JPanel implements MouseListener
{
    // panel merete
    private final int W;
    private final int H;

    // fo ablak, ezen keresztul tudunk "kepernyot valtani"
    private final MainFrame frame;

    // highscore tarolo, innen olvassuk ki a best pontot
    private final HighScoreStore store;

    // az aktualis futas pontszama
    private int score = 0;

    // "gombok" kattinthato teruletei (valojaban teglalapok)
    private Rectangle btnRetry;
    private Rectangle btnMenu;
    private Rectangle btnHigh;

    // betutipusok a cimhez es gombokhoz
    private final Font fontTitle = techFont(42, true);
    private final Font fontUIMid = techFont(18, true);

    GameOverPanel(int w, int h, MainFrame frame, HighScoreStore store)
    {
        // alap adatok eltarolasa
        this.W = w;
        this.H = h;
        this.frame = frame;
        this.store = store;

        // panel meret + fokusz + eger esemeny
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addMouseListener(this);

        // gombok helyenek letrehozasa
        buildButtons();
    }

    void setScore(int score)
    {
        // mainframe innen allitja be a friss pontszamot
        this.score = score;

        // ujrarajzolas, hogy az uj score latszodjon
        repaint();
    }

    private static Font techFont(int size, boolean bold)
    {
        // par preferalt monospace font, ha nincs, akkor monospaced fallback
        String[] prefs = {"JetBrains Mono", "Cascadia Mono", "Consolas", "Menlo", "Monospaced"};
        int style = bold ? Font.BOLD : Font.PLAIN;

        for (String name : prefs)
        {
            Font f = new Font(name, style, size);
            if (f != null) return f;
        }

        return new Font("Monospaced", style, size);
    }

    private void buildButtons()
    {
        // gomb meretek es kozepre igazitas
        int bw = 260;
        int bh = 56;
        int cx = W / 2 - bw / 2;

        // gombok pozicioja
        btnRetry = new Rectangle(cx, 265, bw, bh);
        btnMenu  = new Rectangle(cx, 335, bw, bh);
        btnHigh  = new Rectangle(cx, 405, bw, bh);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        // panel alap rajzolas
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // szebb szoveg/elek
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // hatter
        g2.setColor(new Color(108, 112, 118));
        g2.fillRect(0, 0, W, H);

        // sotet atfedo reteg, hogy "game over" hatasa legyen
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, W, H);

        // cim: game over
        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontTitle);

        String t = "GAME OVER";
        int tw = g2.getFontMetrics().stringWidth(t);
        g2.drawString(t, W / 2 - tw / 2, 175);

        // score sor: aktualis pont + best pont
        g2.setFont(fontUIMid);

        String s = "SCORE: " + score + "   |   BEST: " + store.getBest();
        int sw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, W / 2 - sw / 2, 210);

        // gombok kirajzolasa
        drawButton(g2, btnRetry, "RETRY");
        drawButton(g2, btnMenu, "MENU");
        drawButton(g2, btnHigh, "HIGH SCORES");
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text)
    {
        // gomb hattere
        g2.setColor(new Color(78, 82, 88, 245));
        g2.fillRect(r.x, r.y, r.width, r.height);

        // keret
        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawRect(r.x, r.y, r.width - 1, r.height - 1);

        // felso highlight vonal
        g2.setColor(new Color(255, 255, 255, 35));
        g2.drawLine(r.x + 1, r.y + 1, r.x + r.width - 3, r.y + 1);

        // gomb szoveg kozepen
        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontUIMid);

        int tw = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, r.x + r.width / 2 - tw / 2, r.y + r.height / 2 + 7);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // fokusz vissza a panelre
        requestFocusInWindow();

        Point p = e.getPoint();

        // kattintas alapjan navigacio
        if (btnRetry.contains(p)) frame.showGame();
        else if (btnMenu.contains(p)) frame.showMenu();
        else if (btnHigh.contains(p)) frame.showHighScores();
    }

    // a tobbi eger esemenyt itt nem hasznaljuk
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
