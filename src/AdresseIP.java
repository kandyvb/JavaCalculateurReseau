import java.util.regex.Pattern;

public class AdresseIP {

    private int[] octets;
    private String classe;

    public AdresseIP(String adresse) throws InvalidIPException {
        this.octets = validerAdresse(adresse);
        this.classe = getClasse();
    }

    private int[] validerAdresse(String adresse) throws InvalidIPException {
        String regex = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
        if (!Pattern.matches(regex, adresse)) {
            throw new InvalidIPException("Format d'adresse IP invalide !");
        }

        String[] parties = adresse.split("\\.");
        int[] octets = new int[4];
        for (int i = 0; i < parties.length; i++) {
            int valeur = Integer.parseInt(parties[i]);
            if (valeur < 0 || valeur > 255) {
                throw new InvalidIPException("Un octet est hors de la plage (0-255).");
            }
            octets[i] = valeur;
        }
        return octets;
    }

    public String getClasse() {
        int premierOctet = octets[0];
        if (premierOctet >= 1 && premierOctet <= 126) {
            return "A";
        } else if (premierOctet >= 128 && premierOctet <= 191) {
            return "B";
        } else if (premierOctet >= 192 && premierOctet <= 223) {
            return "C";
        } else if (premierOctet >= 224 && premierOctet <= 239) {
            return "D (Multicast)";
        } else if (premierOctet >= 240 && premierOctet <= 255) {
            return "E (Experimental)";
        } else {
            return "Non définie";
        }
    }

    // Méthode pour calculer les plages d'adresses
    public String[] calculerPlages(String masque) throws InvalidIPException {
        int[] masqueOctets = validerAdresse(masque);
        int[] adresseReseau = new int[4];
        int[] adresseDiffusion = new int[4];

        // Calcul de l'adresse réseau et de diffusion
        for (int i = 0; i < 4; i++) {
            adresseReseau[i] = octets[i] & masqueOctets[i];
            adresseDiffusion[i] = octets[i] | ~masqueOctets[i] & 0xFF;
        }

        String adresseDebut = String.format("%d.%d.%d.%d",
                adresseReseau[0], adresseReseau[1], adresseReseau[2], adresseReseau[3]);
        String adresseFin = String.format("%d.%d.%d.%d",
                adresseDiffusion[0], adresseDiffusion[1], adresseDiffusion[2], adresseDiffusion[3]);

        return new String[]{adresseDebut, adresseFin};
    }

    @Override
    public String toString() {
        return String.format("Adresse IP : %d.%d.%d.%d, Classe : %s",
                octets[0], octets[1], octets[2], octets[3], classe);
    }

    public static class InvalidIPException extends Exception {
        public InvalidIPException(String message) {
            super(message);
        }
    }
}
