import javax.swing.*;

public class TuxFlappy {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
        {
            MainFrame f = new MainFrame(900, 520);
            f.setVisible(true);
        });
    }
}
