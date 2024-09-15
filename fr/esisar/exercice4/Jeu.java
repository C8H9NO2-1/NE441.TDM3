package fr.esisar.exercice4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Jeu {
    public static void main(String[] args) throws Exception {
        Jeu j = new Jeu();
        j.execute();
    }

    private void execute() throws IOException {
        System.out.println("Démarrage du client");

        // Création de la socket
        Socket socket = new Socket();

        // Connexion au serveur
        InetSocketAddress adrDest = new InetSocketAddress("127.0.0.1", 7500);
        socket.connect(adrDest);

        boolean b = true;
        String messageCourant = new String();
        byte[] bufR = new byte[2048];
        InputStream is = socket.getInputStream();


        byte[] bufE;
        OutputStream os = socket.getOutputStream();
        while (b) {
            // On reçoit le message du serveur
            int lenBufR = is.read(bufR);

            if (lenBufR != -1) {

                // Ici on a plusieurs cas
                // Le premier est qu'on a reçu tout le message
                // Le deuxième est qu'on a reçu une partie du message
                // Il faut ensuite vérifier quelle partie du message c'est

                String message = new String(bufR, 0, lenBufR);
                System.out.println("Message du serveur = " + message);

                if (message.contains("Erreur")) {
                    b = false;
                } else if (message.contains("?")) {
                    // On a reçu tout le message ou au moins la fin du message
                    // On ajoute la fin du message
                    messageCourant = messageCourant.concat(message);
                    int pointIndice = message.indexOf('?');

                    // On travaille jusqu'au dernier point d'interrogation
                    boolean continuer = true;
                    while (continuer && messageCourant.contains("?")) {
                        System.out.println("Message traité par le client = " + messageCourant);

                        int plusIndice = messageCourant.indexOf('+'); 
                        int egalIndice = messageCourant.indexOf('=');

                        int x = Integer.parseInt(messageCourant.substring(0, plusIndice));
                        int y = Integer.parseInt(messageCourant.substring(plusIndice, egalIndice));

                        // On envoie ensuite la réponse
                        bufE = new String(x + y + ";").getBytes();
                        os.write(bufE);
                        System.out.println("Message envoyé = " + (x + y) + ";");

                        pointIndice = messageCourant.indexOf('?');
                        if (pointIndice < messageCourant.length()) {
                            messageCourant = messageCourant.substring(pointIndice + 1);
                        } else {
                            continuer = false;
                        }
                    }
                } else {
                    // Si ce qu'on a reçu n'est pas la totalité du message
                    // Il faut enregistrer ce qu'on a reçu
                    
                    System.out.println("Message bien reçu mais non traité");
                    messageCourant = messageCourant.concat(message);
                }

            }
        }
        socket.close();
    }
}
