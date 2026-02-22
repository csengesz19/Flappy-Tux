import java.awt.*;
import java.util.Random;

public class PipePair {
    // a csopar vizszintes pozicioja (tort lehet a dt-s mozgas miatt)
    private double x;

    // a cso szelessege es a kepernyo magassaga
    private final int width;
    private final int screenH;

    // a res tetejenek y-ja es a res magassaga
    private int gapY;
    private int gapH;

    // pontszamhoz: mar atment-e rajta a tux
    private boolean passed = false;

    // veletlen res poziciohoz
    private final Random rnd;

    // disz feliratok a csoben
    private static final String[] LABELS =
            {
                    "/home", "/etc", "/var", "/usr", "/bin", "/tmp", "/opt", "/dev", "/proc"
            };

    PipePair(int x, int width, int screenH, int gapH, Random rnd) {
        // kezdo ertekek beallitasa
        this.x = x;
        this.width = width;
        this.screenH = screenH;
        this.gapH = gapH;
        this.rnd = rnd;

        // indulaskor is kell egy veletlen res
        rerollGap();
    }

    double getX() {
        return x;
    }

    int getWidth() {
        return width;
    }

    boolean isPassed() {
        return passed;
    }

    void setPassed(boolean passed) {
        this.passed = passed;
    }

    void setGapH(int gapH) {
        // res magassag allitasa es ujradobas
        this.gapH = gapH;
        rerollGap();
    }

    void update(double speedPxPerSec, double dt) {
        // csovek balra mennek: x csokken
        x -= speedPxPerSec * dt;
    }

    void reset(double newX) {
        // ha kiment balra, elorerakjuk jobbra es uj rest adunk neki
        x = newX;
        passed = false;
        rerollGap();
    }

    private void rerollGap() {
        // a res ne legyen tul kozel a kepernyo szeleihez
        int margin = 80;
        int minY = margin;
        int maxY = screenH - margin - gapH;
        if (maxY < minY) maxY = minY;

        // veletlen gapY a megengedett tartomanyban
        gapY = minY + rnd.nextInt(Math.max(1, maxY - minY + 1));
    }

    Rectangle topRect() {
        // felso cso: kepernyo tetejetol a resig
        return new Rectangle((int) x, 0, width, gapY);
    }

    Rectangle bottomRect() {
        // also cso: res aljatol a kepernyo aljaig
        int y = gapY + gapH;
        int h = screenH - y;
        if (h < 0) h = 0;
        return new Rectangle((int) x, y, width, h);
    }

    boolean collides(Rectangle tuxRect) {
        // utkozes: a tux hitbox metszi barmelyik csovet
        return tuxRect.intersects(topRect()) || tuxRect.intersects(bottomRect());
    }

    void draw(Graphics2D g2) {
        // ket "torony": felso es also cso
        drawTechTower(g2, topRect(), true);
        drawTechTower(g2, bottomRect(), false);
    }

    private void drawTechTower(Graphics2D g2, Rectangle r, boolean top) {
        // ha nincs magassag, nincs mit rajzolni
        if (r.height <= 0) return;

        // clip: csak a teglalapon belul rajzolunk
        Shape oldClip = g2.getClip();
        g2.setClip(r);

        // alap hatter
        g2.setColor(new Color(56, 60, 66));
        g2.fillRect(r.x, r.y, r.width, r.height);

        // fenyes csik
        g2.setColor(new Color(255, 255, 255, 18));
        g2.fillRect(r.x + 8, r.y, 10, r.height);

        // keret
        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawRect(r.x, r.y, r.width - 1, r.height - 1);

        // enyhe highlight vonalak
        g2.setColor(new Color(255, 255, 255, 26));
        g2.drawLine(r.x + 1, r.y + 1, r.x + r.width - 3, r.y + 1);
        g2.drawLine(r.x + 1, r.y + 1, r.x + 1, r.y + r.height - 3);

        // "csavarok" disznek
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(r.x + 10, r.y + 10, 4, 4);
        g2.fillRect(r.x + r.width - 14, r.y + 10, 4, 4);
        g2.fillRect(r.x + 10, r.y + r.height - 14, 4, 4);
        g2.fillRect(r.x + r.width - 14, r.y + r.height - 14, 4, 4);

        // belso sorok parameterei
        int pad = 12;
        int rowH = 30;
        int gap = 10;

        int startY = r.y + pad;
        int endY = r.y + r.height - pad;

        // felso csoben felulrol tolti, alsoban alulrol/felulrol maskepp
        if (top) {
            for (int y = endY - rowH; y >= startY; y -= (rowH + gap)) {
                drawFolderRow(g2, r.x + pad, y, r.width - 2 * pad, rowH, (y / (rowH + gap)));
            }
        } else {
            for (int y = startY; y + rowH <= endY; y += (rowH + gap)) {
                drawFolderRow(g2, r.x + pad, y, r.width - 2 * pad, rowH, (y / (rowH + gap)));
            }
        }

        // clip visszaallitasa
        g2.setClip(oldClip);
    }

    private void drawFolderRow(Graphics2D g2, int x, int y, int w, int h, int idx) {
        // sor hattere
        g2.setColor(new Color(88, 92, 100));
        g2.fillRect(x, y, w, h);

        // sor kerete
        g2.setColor(new Color(0, 0, 0, 130));
        g2.drawRect(x, y, w - 1, h - 1);

        // mappa ikon parameterei
        int fx = x + 8;
        int fy = y + 8;
        int fw = 28;
        int fh = 16;
        int tabW = 12;
        int tabH = 5;

        // mappa ikon rajzolasa
        g2.setColor(new Color(210, 214, 220));
        g2.fillRect(fx, fy, fw, fh);

        g2.setColor(new Color(230, 233, 238));
        g2.fillRect(fx + 3, fy - tabH + 2, tabW, tabH);

        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawRect(fx, fy, fw - 1, fh - 1);
        g2.drawRect(fx + 3, fy - tabH + 2, tabW - 1, tabH - 1);

        // felirat valasztasa es kiirasa
        String label = LABELS[Math.floorMod(idx, LABELS.length)];
        g2.setColor(new Color(238, 241, 245));
        g2.drawString(label, fx + fw + 10, y + h - 10);

        // kis fenyes csik a sor jobb oldalan
        g2.setColor(new Color(255, 255, 255, 20));
        g2.fillRect(x + w - 18, y + 6, 10, h - 12);
    }
}
