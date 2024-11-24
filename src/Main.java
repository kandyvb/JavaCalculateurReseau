public class Main {
    public static void main(String[] args) {
        // Lancer l'application Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculateurReseau app = new CalculateurReseau();
            app.setVisible(true);
        } );
    }
}
