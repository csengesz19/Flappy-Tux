import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class Sound {
    static final SoundEffect FLAP = new SoundEffect("flap.wav");
    static final SoundEffect DIE = new SoundEffect("die.wav");

    static class SoundEffect {
        private Clip clip;

        SoundEffect(String fileName) {
            this.clip = loadClip(fileName);
        }

        void play() {
            if (clip == null) return;

            // ha épp megy, indítsuk újra az elejéről (így gyorsan ismételhető)
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }

        long lengthMs() {
            if (clip == null) return 0;
            return clip.getMicrosecondLength() / 1000;
        }

        private static Clip loadClip(String fileName) {
            try {
                AudioInputStream ais = null;


                InputStream is = Sound.class.getResourceAsStream(fileName);
                if (is != null) {
                    ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
                } else {
                    //
                    File f = new File(fileName);
                    if (!f.exists()) return null;
                    ais = AudioSystem.getAudioInputStream(f);
                }

                Clip c = AudioSystem.getClip();
                c.open(ais);
                return c;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
