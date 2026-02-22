import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class HighScoreStore {
    // ide mentjuk a pontszamokat (fajl eleresi utvonala)
    private final Path file;

    // memoriaban tarolt pontszam lista
    private final ArrayList<Integer> scores = new ArrayList<>();

    public HighScoreStore() {
        // user home-ba ment, hogy biztosan irhato legyen (nem resources mappa, nem jar belseje)
        this.file = Paths.get(System.getProperty("user.home"), "tuxflappy_scores.txt");

        // indulaskor betoltjuk, ha mar van fajl
        load();
    }

    public void addScore(int s) {
        // negativ pontszamot nem veszunk fel
        if (s < 0) return;

        // uj pont hozzaadasa
        scores.add(s);

        // csokkeno sorrend (legnagyobb elol)
        scores.sort(Comparator.reverseOrder());

        // csak a top 10-et tartjuk meg
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }

        // valtozas utan mentunk fajlba
        save();
    }

    public List<Integer> getScores() {
        // kivulrol ne lehessen modositani a listat (csak olvasni)
        return Collections.unmodifiableList(scores);
    }

    public int getBest() {
        // ha nincs pont, akkor 0 a best, kulonben az elso elem (mert rendezve van)
        return scores.isEmpty() ? 0 : scores.get(0);
    }

    public void reload() {
        // ujra betoltes fajlbol (pl. highscore panel megnyitasakor)
        load();
    }

    private void load() {
        // eloszor mindent torlunk a memoriabol
        scores.clear();

        // ha nincs fajl, nincs mit betolteni
        if (!Files.exists(file)) return;

        // soronkent beolvassuk a pontokat
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // ha szam, akkor felvesszuk, ha nem, akkor atugorjuk
                try {
                    scores.add(Integer.parseInt(line));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException ignored) {
        }

        // betoltes utan is rendezes + top10
        scores.sort(Comparator.reverseOrder());
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
    }

    private void save() {
        // kiirjuk a pontokat a fajlba (minden sor egy pontszam)
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int s : scores) {
                bw.write(Integer.toString(s));
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }
}
