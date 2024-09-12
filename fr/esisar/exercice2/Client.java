package fr.esisar.exercice2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.execute();
    }

    private void execute() throws IOException {
        System.out.println("Démarrage du client ...");

        Socket socket = new Socket();

        InetSocketAddress adrDest = new InetSocketAddress("127.0.0.1", 2000);
        socket.connect(adrDest);

        // Envoi du message
        byte[] bufE = new String("hello").getBytes();
        OutputStream os = socket.getOutputStream();
        os.write(bufE);
        System.out.println("Message envoyé");

        // On ferme la connexion
        socket.close();
        System.out.println("Arrêt du client.");
    }
}
