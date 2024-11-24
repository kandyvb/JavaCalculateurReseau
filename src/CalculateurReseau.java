import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateurReseau extends JFrame {

    private JTextField ipField;
    private JTextField masqueField;
    private JTextArea resultArea;

    public CalculateurReseau() {
        setTitle("Calculateur d'Adresse Réseau");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel pour les entrées
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("Adresse IP :"));
        ipField = new JTextField();
        inputPanel.add(ipField);

        inputPanel.add(new JLabel("Masque (ex: 255.255.255.0) :"));
        masqueField = new JTextField();
        inputPanel.add(masqueField);

        JButton calculerButton = new JButton("Calculer");
        inputPanel.add(calculerButton);

        add(inputPanel, BorderLayout.NORTH);

        // Zone de résultats
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Gestion de l'événement bouton
        calculerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effectuerCalcul();
            }
        });
    }

    private void effectuerCalcul() {
        String ip = ipField.getText().trim();
        String masque = masqueField.getText().trim();

        resultArea.setText("");

        try {
            AdresseIP adresse = new AdresseIP(ip);
            String classe = adresse.getClasse();

            // Calcul des plages d'adresses
            String[] plages = adresse.calculerPlages(masque);
            String adresseReseau = plages[0];
            String adresseDiffusion = plages[1];

            // Affichage des résultats
            String resultats = String.format(
                    "Adresse IP : %s\nMasque : %s\nClasse : %s\n\n" +
                            "Adresse réseau : %s\nAdresse de diffusion : %s\n",
                    ip, masque, classe, adresseReseau, adresseDiffusion
            );

            resultArea.append(resultats);

            // Enregistrement dans un fichier
            enregistrerDansFichier(ip, masque, classe, adresseReseau, adresseDiffusion);

        } catch (AdresseIP.InvalidIPException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Entrée invalide", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enregistrerDansFichier(String ip, String masque, String classe, String adresseReseau, String adresseDiffusion) {
        try (FileWriter writer = new FileWriter("historique.txt", true)) {
            writer.write(String.format(
                    "Adresse IP : %s, Masque : %s, Classe : %s, Adresse réseau : %s, Adresse de diffusion : %s\n",
                    ip, masque, classe, adresseReseau, adresseDiffusion
            ));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Impossible d'enregistrer les données : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void enregistrerDansFichier(String ip, String masque, String classe) {
        try (FileWriter writer = new FileWriter("historique.txt", true)) {
            writer.write(String.format("Adresse IP : %s, Masque : %s, Classe : %s\n", ip, masque, classe));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Impossible d'enregistrer les données : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculateurReseau app = new CalculateurReseau();
            app.setVisible(true);
        });
    }
}
