import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SettingsPanel extends JPanel implements MouseListener
{
    // panel merete
    private final int W;
    private final int H;

    // fo ablak (vissza menu-be)
    private final MainFrame frame;

    // beallitas tarolo (music on/off)
    private final SettingsStore settings;

    // hangkezelo (itt kapcsoljuk a zenet)
    private final Audio audio;

    // "gombok" kattinthato teruletei
    private Rectangle btnToggleMusic;
    private Rectangle btnBack;

    // betutipusok
    private final Font fontTitle = techFont(34, true);
    private final Font fontUIMid = techFont(18, true);
    private final Font fontMono  = techFont(15, false);

    SettingsPanel(int w, int h, MainFrame frame, SettingsStore settings, Audio audio)
    {
        // alap adatok eltarolasa
        this.W = w;
        this.H = h;
        this.frame = frame;
        this.settings = settings;
        this.audio = audio;

        // meret + fokusz + eger esemeny
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addMouseListener(this);

        // gombok pozicionalasa kozepre
        int bw = 320;
        int bh = 56;
        int cx = W / 2 - bw / 2;

        btnToggleMusic = new Rectangle(cx, 250, bw, bh);
        btnBack = new Rectangle(cx, 330, bw, bh);
    }

    void refresh()
    {
        // kepernyo megnyitasakor frissitjuk a beallitast
        settings.load();
        repaint();
    }

    private static Font techFont(int size, boolean bold)
    {
        // monospace font valasztas (ha nincs, fallback)
        String[] prefs = {"JetBrains Mono", "Cascadia Mono", "Consolas", "Menlo", "Monospaced"};
        int style = bold ? Font.BOLD : Font.PLAIN;
        for (String name : prefs)
        {
            Font f = new Font(name, style, size);
            if (f != null) return f;
        }
        return new Font("Monospaced", style, size);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // hatter
        g2.setColor(new Color(108, 112, 118));
        g2.fillRect(0, 0, W, H);

        // cim
        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontTitle);

        String t = "SETTINGS";
        int tw = g2.getFontMetrics().stringWidth(t);
        g2.drawString(t, W / 2 - tw / 2, 130);

        // info szoveg
        g2.setFont(fontMono);
        g2.drawString("Saved in: tuxflappy_settings.properties", W / 2 - 190, 185);

        // zene allapot kiirasa a gomb szovegeben
        String musicText = settings.isMusicOn() ? "MUSIC: ON" : "MUSIC: OFF";
        drawButton(g2, btnToggleMusic, "TOGGLE " + musicText);
        drawButton(g2, btnBack, "BACK");
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text)
    {
        // gomb rajzolas (egyedi ui)
        g2.setColor(new Color(78, 82, 88, 245));
        g2.fillRect(r.x, r.y, r.width, r.height);

        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawRect(r.x, r.y, r.width - 1, r.height - 1);

        g2.setColor(new Color(255, 255, 255, 35));
        g2.drawLine(r.x + 1, r.y + 1, r.x + r.width - 3, r.y + 1);

        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontUIMid);
        int tw = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, r.x + r.width / 2 - tw / 2, r.y + r.height / 2 + 7);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        requestFocusInWindow();
        Point p = e.getPoint();

        // zene kapcsolo
        if (btnToggleMusic.contains(p))
        {
            boolean newVal = !settings.isMusicOn();
            settings.setMusicOn(newVal);   // menti fajlba
            audio.setMusicEnabled(newVal); // tenylegesen kapcsolja a zenet
            repaint();
            return;
        }

        // vissza menu-be
        if (btnBack.contains(p))
        {
            frame.showMenu();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
