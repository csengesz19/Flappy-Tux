import javax.sound.sampled.*;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class Audio {
    // hatterzene clip
    private Clip musicClip;

    // pause utan innen folytatja (mikroszekundumban)
    private long pausedMusicPosUs = 0;

    public Audio(String musicFileName) {
        // hatterzene betoltese
        musicClip = loadClip(musicFileName);
    }

    public void startMusicLoop() {
        if (musicClip == null) return;

        // ha volt pause, akkor folytassa onnan
        if (pausedMusicPosUs > 0) {
            resumeMusic();
            return;
        }

        // ha mar szol, nem inditjuk ujra
        if (musicClip.isRunning()) return;

        // induljon elolrol, es menjen vegtelen ciklusban
        musicClip.setFramePosition(0);
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopMusic() {
        if (musicClip == null) return;

        // stop eseten elfelejtjuk a poziciot
        pausedMusicPosUs = 0;
        musicClip.stop();
    }

    public void pauseMusic() {
        if (musicClip == null) return;
        if (!musicClip.isRunning()) return;

        // elmentjuk hol tartott, aztan megallitjuk
        pausedMusicPosUs = musicClip.getMicrosecondPosition();
        musicClip.stop();
    }

    public void resumeMusic() {
        if (musicClip == null) return;

        // ha nincs mentett pozicio, induljon elolrol
        if (pausedMusicPosUs <= 0) {
            musicClip.setFramePosition(0);
        } else {
            // folytatas a mentett poziciobol
            musicClip.setMicrosecondPosition(pausedMusicPosUs);
        }

        // tovabbra is loopoljon
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public boolean isMusicPlaying() {
        // gyors ellenorzes: van-e clip es fut-e
        return musicClip != null && musicClip.isRunning();
    }

    public void setMusicEnabled(boolean on) {
        // settingsbol hivjuk: on -> megy, off -> pause
        if (on) startMusicLoop();
        else pauseMusic();
    }

    public void playFlap() {
        // rovid effekt a csapkodashoz
        Sound.FLAP.play();
    }

    public void playDie() {
        // rovid effekt halalkor
        Sound.DIE.play();
    }

    public void playDieWithMusicPause() {
        // megjegyezzuk, ment-e a zene
        boolean wasMusicRunning = (musicClip != null && musicClip.isRunning());

        // ha ment, akkor pause, hogy a die hang jol hallatszodjon
        if (wasMusicRunning) pauseMusic();

        // die hang lejatszasa
        Sound.DIE.play();

        // varunk a die hang kb. hosszaig, aztan visszainditjuk a zenet
        long len = Sound.DIE.lengthMs();
        int delayMs = (int) Math.max(80, len);

        Timer t = new Timer(delayMs, e ->
        {
            if (wasMusicRunning) resumeMusic();
        });
        t.setRepeats(false);
        t.start();
    }

    private static Clip loadClip(String fileName) {
        try {
            // megprobaljuk eloszor resources-bol (classpath), aztan fajlbol
            AudioInputStream ais = null;

            String res = fileName.startsWith("/") ? fileName : ("/" + fileName);
            InputStream is = Audio.class.getResourceAsStream(res);

            if (is != null) {
                // resources-os betoltes
                ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            } else {
                // fajlbol betoltes (working directory)
                File f = new File(fileName);
                if (!f.exists()) return null;
                ais = AudioSystem.getAudioInputStream(f);
            }

            // clip letrehozasa es betoltese
            Clip c = AudioSystem.getClip();
            c.open(ais);
            return c;
        } catch (Exception e) {
            // ha nem sikerul, a zene egyszeruen nem fog szolni
            System.out.println("Music load failed: " + fileName + " (" + e.getMessage() + ")");
            return null;
        }
    }
}
