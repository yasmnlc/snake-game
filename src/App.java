import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            Telas telas = new Telas();
            telas.setVisible(true);
        });
    }
}