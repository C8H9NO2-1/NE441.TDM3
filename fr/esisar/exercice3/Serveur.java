package fr.esisar.exercice3;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    public static void main(String[] args) throws Exception {
        Serveur serveur = new Serveur();
        serveur.execute();
    }

    private void execute() throws IOException {
        System.out.println("Démarrage du serveur ...");

        // On déclare le serveur
        ServerSocket socketEcoute = new ServerSocket();
        socketEcoute.bind(new InetSocketAddress(2000));

        // Attente de la connexion d'un client
        System.out.println("Attente de la connexion du client ...");
        Socket socketConnexion = socketEcoute.accept();

        // Affichage des données servant à identifier le client
        System.out.println("Un client est connecté");
        System.out.println("IP: " + socketConnexion.getInetAddress());
        System.out.println("Port: " + socketConnexion.getPort());

        // On lit tous les messages envoyés par le client
        byte[] bufR = new byte[2048];
        InputStream is = socketConnexion.getInputStream();

        boolean b = true;
        while (b) {
            int lenBufR = is.read(bufR);
            if (lenBufR != -1) {
                String message = new String(bufR, 0, lenBufR);
                System.out.println("Message reçu = " + message);
            } else {
                b = false;
            }
        }

        socketConnexion.close();

        socketEcoute.close();
        System.out.println("Arrêt du serveur");
    }
}
