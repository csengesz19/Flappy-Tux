import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class SettingsStore {
    // ide mentjuk a beallitasokat (user home-ban, hogy irhato legyen)
    private final Path file;

    // jelenleg egy beallitas van: legyen-e zene
    private boolean musicOn = true;

    public SettingsStore() {
        // beallitas fajl helye
        file = Paths.get(System.getProperty("user.home"), "tuxflappy_settings.properties");

        // indulaskor betoltjuk, ha mar letezik
        load();
    }

    public boolean isMusicOn() {
        // lekerdezes: be van-e kapcsolva a zene
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        // beallitjuk az uj erteket
        this.musicOn = musicOn;

        // valtozas utan azonnal mentjuk fajlba
        save();
    }

    public void load() {
        // ha nincs fajl, akkor marad a default (true)
        if (!Files.exists(file)) return;

        // properties = egyszeru kulcs-ertek tarolas fajlban
        Properties p = new Properties();

        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            // beolvassa a fajl tartalmat a properties-be
            p.load(fis);

            // kiolvassuk a musicOn kulcsot (ha nincs, default true)
            musicOn = Boolean.parseBoolean(p.getProperty("musicOn", "true"));
        } catch (Exception ignored) {
        }
    }

    public void save() {
        // uj properties objektum, amibe beleirjuk a jelenlegi beallitasokat
        Properties p = new Properties();
        p.setProperty("musicOn", Boolean.toString(musicOn));

        try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
            // fajlba iras kommenttel
            p.store(fos, "TuxFlappy settings");
        } catch (Exception ignored) {
        }
    }
}
