import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//ez jeleniti meg a fomenut
class MenuPanel extends JPanel implements MouseListener
{
    private final int W;
    private final int H;
    private final MainFrame frame;

    private Rectangle btnStart;
    private Rectangle btnHigh;
    private Rectangle btnSettings;
    private Rectangle btnQuit;

    private final Font fontTitle = techFont(46, true);
    private final Font fontUIMid = techFont(18, true);
    private final Font fontUISmall = techFont(14, false);

    MenuPanel(int w, int h, MainFrame frame)
    {
        this.W = w;
        this.H = h;
        this.frame = frame;

        setPreferredSize(new Dimension(W, H)); // emiatt kell a pack
        setFocusable(true); // tud fokuszt kapni
        addMouseListener(this);

        buildButtons();
    }

    private static Font techFont(int size, boolean bold)
    {
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
        int bw = 260;
        int bh = 56;
        int cx = W / 2 - bw / 2;

        // teglalapok lesznek a gombok
        btnStart    = new Rectangle(cx, 220, bw, bh);
        btnHigh     = new Rectangle(cx, 290, bw, bh);
        btnSettings = new Rectangle(cx, 360, bw, bh);
        btnQuit     = new Rectangle(cx, 430, bw, bh);
    }

    @Override  // itt rajzolom a gombokat
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

        String title = "TUX FLAPPY";
        int tw = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, W / 2 - tw / 2, 150);

        // hint
        g2.setFont(fontUISmall);
        String hint = "SPACE = FLAP | ESC = MENU ";
        int hw = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, W / 2 - hw / 2, 182);

        drawButton(g2, btnStart, "START");
        drawButton(g2, btnHigh, "HIGH SCORES");
        drawButton(g2, btnSettings, "SETTINGS");
        drawButton(g2, btnQuit, "QUIT");
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text)
    {
        g2.setColor(new Color(78, 82, 88, 245));
        g2.fillRect(r.x, r.y, r.width, r.height);

        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawRect(r.x, r.y, r.width - 1, r.height - 1);

        // top highlight
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

        //benne volt-e a teglalapba
        if (btnStart.contains(p))
        {
            frame.showGame();
        }
        else if (btnHigh.contains(p))
        {
            frame.showHighScores();
        }
        else if (btnSettings.contains(p))
        {
            frame.showSettings();
        }
        else if (btnQuit.contains(p))
        {
            System.exit(0);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
