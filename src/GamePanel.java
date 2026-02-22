import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final int W;
    private final int H;
    private final MainFrame frame;
    private final Audio audio;

    private final Timer timer;
    private final Random rnd = new Random();

    private static final double STEP = 1.0 / 120.0; // 120 Hz
    private double accumulator = 0.0;
    private long lastNs = 0;

    private double pipeSpeedPxPerSec = 260.0;
    private int pipeSpacing = 270;
    private int gapH = 200;
    private int pipeWidth = 110;

    private final ArrayList<PipePair> pipes = new ArrayList<>();
    private Tux tux;

    private int score = 0;
    private boolean running = false;

    // fontok
    private final Font fontUIBig = techFont(22, true);
    private final Font fontUISmall = techFont(14, false);
    private final Font fontMono = techFont(13, false);

    GamePanel(int w, int h, MainFrame frame, Audio audio) {
        this.W = w;
        this.H = h;
        this.frame = frame;
        this.audio = audio;

        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        resetWorld();

        timer = new Timer(16, this);
        timer.start();
    }

    public void startNewRun() {
        resetWorld();
        running = true;
        requestFocusInWindow();
    }

    private static Font techFont(int size, boolean bold) {
        String[] prefs = {"JetBrains Mono", "Cascadia Mono", "Consolas", "Menlo", "Monospaced"};
        int style = bold ? Font.BOLD : Font.PLAIN;
        for (String name : prefs) {
            Font f = new Font(name, style, size);
            if (f != null) return f;
        }
        return new Font("Monospaced", style, size);
    }

    private void resetWorld() {
        tux = new Tux(W / 4, H / 2);

        pipes.clear();
        int n = 4;
        int startX = W + 220;

        for (int i = 0; i < n; i++) {
            int x = startX + i * pipeSpacing;
            pipes.add(new PipePair(x, pipeWidth, H, gapH, rnd));
        }

        score = 0;
        accumulator = 0.0;
        lastNs = System.nanoTime();
    }

    private void gameOver() {
        if (!running) return;
        running = false;

        audio.playDieWithMusicPause();
        frame.showGameOver(score);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long now = System.nanoTime();
        if (lastNs == 0) lastNs = now;

        double frameDt = (now - lastNs) / 1_000_000_000.0;
        lastNs = now;

        if (frameDt > 0.25) frameDt = 0.25;

        if (running) {
            accumulator += frameDt;

            int steps = 0;
            int maxSteps = 12;

            while (accumulator >= STEP && steps < maxSteps) {
                updateGame(STEP);
                accumulator -= STEP;
                steps++;
            }
        }

        repaint();
    }

    private void updateGame(double dt) {
        tux.update(dt);

        // hatarok
        if (tux.getY() + tux.getH() >= H || tux.getY() < 0) {
            gameOver();
            return;
        }

        Rectangle tuxHit = tux.hitbox();

        double rightMost = 0;
        for (PipePair p : pipes) {
            if (p.getX() > rightMost) rightMost = p.getX();
        }

        for (PipePair p : pipes) {
            p.update(pipeSpeedPxPerSec, dt);

            if (!p.isPassed() && p.getX() + p.getWidth() < tux.getX()) {
                p.setPassed(true);
                score++;
            }

            if (p.collides(tuxHit)) {
                gameOver();
                return;
            }

            if (p.getX() + p.getWidth() < 0) {
                p.reset(rightMost + pipeSpacing);
                rightMost = p.getX();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBackground(g2);

        // csovek
        g2.setFont(fontMono);
        for (PipePair p : pipes) p.draw(g2);

        // tux
        tux.draw(g2);

        g2.setColor(new Color(235, 238, 242));
        g2.setFont(fontUIBig);
        g2.drawString("SCORE: " + score, 18, 32);
        g2.setFont(fontUISmall);
        g2.drawString("ESC = MENU", 18, 52);

        if (!running) {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRect(0, 0, W, H);

            g2.setColor(new Color(235, 238, 242));
            g2.setFont(fontUIBig);
            g2.drawString("Click / SPACE to start", W / 2 - 120, H / 2);
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(new Color(108, 112, 118));
        g2.fillRect(0, 0, W, H);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (k == KeyEvent.VK_ESCAPE) {
            running = false;
            frame.showMenu();
            return;
        }

        if (k == KeyEvent.VK_SPACE) {
            if (!running) {
                startNewRun();
                audio.playFlap();
            } else {
                tux.flap();
                audio.playFlap();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();

        if (!running) {
            startNewRun();
            audio.playFlap();
            return;
        }

        tux.flap();
        audio.playFlap();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
