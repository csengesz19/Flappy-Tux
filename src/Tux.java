import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class Tux {
    private static final String TUX_IMAGE_FILE = "tux.png";

    // jatekos x koordinataja fix
    private final int x;

    //jatekos y koordinataja valtozik
    private double y;

    // kirajzolt karakter merete pixelekben
    private final int w = 60;
    private final int h = 76;

    // vy = fuggoleges sebesseg pixel / masodperc
    private double vy = 0.0;

    // gravitacio: minden update-ben noveli a lefele eso sebesseget
    private final double gravity = 1650.0;

    // flap sebesseg: amikor space/click van, ezzel "felpattan" a tux (negativ = felfele)
    private final double flapV = -560.0;

    // maximum esesi sebesseg: hogy ne gyorsuljon vegtelenre lefele
    private final double maxFall = 920.0;

    // a betoltott kep
    private BufferedImage img;

    // jelzi, hogy a kep betoltese sikerult-e
    private boolean imgOk = false;

    Tux(int x, int y) {
        // kezdo pozicio beallitasa
        this.x = x;
        this.y = y;


        //  probaljuk a futtatas mappajabol
        try {
            img = ImageIO.read(new File(TUX_IMAGE_FILE));
            imgOk = (img != null);
        } catch (Exception ignored) {
            imgOk = false;
        }
    }

    int getX() {
        return x;
    }

    double getY() {
        return y;
    }

    int getW() {
        return w;
    }

    int getH() {
        return h;
    }

    // fizika frissitese dt ido alatt (dt masodpercben van)
    void update(double dt) {
        // gravitacio gyorsit: vy no
        vy += gravity * dt;

        // lefele eso sebesseg korlatozasa
        if (vy > maxFall) vy = maxFall;

        // pozicio frissitese: y valtozik a sebesseg alapjan
        y += vy * dt;
    }

    // flap: egy pillanatnyi "felrugas", beallitjuk a felfele iranyu sebesseget
    void flap() {
        vy = flapV;
    }

    // utkozeshez egy kisebb teglalapot adunk vissza
    Rectangle hitbox() {
        return new Rectangle(x + 12, (int) y + 14, w - 24, h - 30);
    }

    // kirajzolas: ha van kep, azt rajzoljuk, kulonben egy egyszeru oval fallbacket
    void draw(Graphics2D g2) {
        // egesz y kell a rajzolashoz (pixelek)
        int iy = (int) y;

        // dontes a sebesseg alapjan: gyors esesnel jobban "lefele billen"
        // gyors felugrasnal "felfele billen"
        double angle = (vy / 900.0) * 0.55;

        // szog korlatozasa, hogy ne forogjon tul sokat
        if (angle > 0.55) angle = 0.55;
        if (angle < -0.55) angle = -0.55;

        if (imgOk) {
            // elmentjuk az eredeti transformot, hogy a forgatas utan vissza tudjuk allitani
            AffineTransform old = g2.getTransform();

            // a forgatashoz a kep kozepere visszuk az origot
            g2.translate(x + w / 2.0, iy + h / 2.0);

            // elforgatjuk a rajzolasi koordinatarendszert
            g2.rotate(angle);

            // visszatoljuk ugy, hogy a kep (0,0)-tol legyen rajzolva
            g2.translate(-w / 2.0, -h / 2.0);

            // kep kirajzolasa fix meretre skalazva (w x h)
            g2.drawImage(img, 0, 0, w, h, null);

            // visszaallitjuk az eredeti transformot, hogy mas rajzolas ne legyen elforgatva
            g2.setTransform(old);
        }

    }
}
