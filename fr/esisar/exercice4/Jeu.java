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
        while (b) {
            // On reçoit le message du serveur
            byte[] bufR = new byte[2048];
            InputStream is = socket.getInputStream();
            int lenBufR = is.read(bufR);
            // TODO Il faudra penser à vérifier si le message contient une
            // ou plusieurs questions ou alors une portion de question
            if (lenBufR != -1) {
                String message = new String(bufR, 0, lenBufR);
                System.out.println("Message du serveur = " + message);

                // On veut récupérer les deux nombres envoyés par le serveur
                int x;
                int y;
                // On récupère l'indice du + et du = et du ? :
                int plusIndice = message.indexOf('+'); 
                int egalIndice = message.indexOf('=');
                int interrogationIndice = message.indexOf('?');

                if (plusIndice != -1) {
                    x = Integer.parseInt(message.substring(0, plusIndice));
                }

                if (egalIndice != -1) {
                    y = Integer.parseInt(message.substring(0, egalIndice));
                }

                    System.out.println(x + y);

                    // On envoie la réponse
                    byte[] bufE = new String(x + y + ";").getBytes();
                    OutputStream os = socket.getOutputStream();
                    os.write(bufE);
                    System.out.println("Message envoyé");
            }
        }
    }
}
