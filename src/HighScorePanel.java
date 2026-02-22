import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

class HighScorePanel extends JPanel implements MouseListener
{
    // panel merete
    private final int W;
    private final int H;

    // fo ablak (innen tudunk visszamenni menu-be)
    private final MainFrame frame;

    // highscore tarolo (innen kerjuk le a top 10 listat)
    private final HighScoreStore store;

    // vissza gomb kattinthato terulete
    private Rectangle btnBack;

    // betutipusok
    private final Font fontTitle = techFont(34, true);
    private final Font fontUIMid = techFont(18, true);
    private final Font fontMono  = techFont(16, false);

    HighScorePanel(int w, int h, MainFrame frame, HighScoreStore store)
    {
        // alap adatok eltarolasa
        this.W = w;
        this.H = h;
        this.frame = frame;
        this.store = store;

        // meret + fokusz + eger esemeny
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addMouseListener(this);

        // back gomb helye es merete
        int bw = 260;
        int bh = 56;
        int cx = W / 2 - bw / 2;
        btnBack = new Rectangle(cx, 410, bw, bh);
    }

    void refresh()
    {
        // csak ujrarajzolast kerunk (a store mar frissitve van a mainframe-ben)
        repaint();
    }

    private static Font techFont(int size, boolean bold)
    {
        // probalunk monospace fontot, ha nincs, fallback monospaced
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
        // alap panel rajzolas
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // hatter
        g2.setColor(new Color(108, 112, 118));
        g2.fillRect(0, 0, W, H);

        // cim
        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontTitle);

        String t = "HIGH SCORES";
        int tw = g2.getFontMetrics().stringWidth(t);
        g2.drawString(t, W / 2 - tw / 2, 120);

        // lista kiirasa
        g2.setFont(fontMono);

        List<Integer> scores = store.getScores();
        int startY = 190;
        int lineH = 26;

        // ha nincs score, irunk egy uzenetet
        if (scores.isEmpty())
        {
            g2.drawString("No scores yet. Play a game!", W / 2 - 140, startY);
        }
        else
        {
            // top lista sorszammal
            for (int i = 0; i < scores.size(); i++)
            {
                String line = String.format("%2d) %d", i + 1, scores.get(i));
                g2.drawString(line, W / 2 - 60, startY + i * lineH);
            }
        }

        // back gomb
        drawButton(g2, btnBack, "BACK");
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text)
    {
        // gomb hattere
        g2.setColor(new Color(78, 82, 88, 245));
        g2.fillRect(r.x, r.y, r.width, r.height);

        // keret
        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawRect(r.x, r.y, r.width - 1, r.height - 1);

        // felso highlight
        g2.setColor(new Color(255, 255, 255, 35));
        g2.drawLine(r.x + 1, r.y + 1, r.x + r.width - 3, r.y + 1);

        // szoveg kozepen
        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontUIMid);

        int tw = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, r.x + r.width / 2 - tw / 2, r.y + r.height / 2 + 7);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // fokusz vissza a panelre, majd vissza menu-be
        requestFocusInWindow();
        if (btnBack.contains(e.getPoint())) frame.showMenu();
    }

    // a tobbi eger esemenyt itt nem hasznaljuk
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
