package fr.esisar.exercice4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.plaf.nimbus.State;

public class Machines {

    private boolean firstState = true;
    private boolean needRead = true;
    private int i = 0;

    public static void main(String[] args) throws Exception {
        Machines m = new Machines();
        m.execute();
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
        
        // On travaille avec une machine à état, le premier état est la
        // lecture du premier nombre et puis le deuxième
        byte[] bufR = new byte[2048];
        byte[] bufE = new byte[2048];
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        String message;
        int lenBufR = -1;
        int x = 0; // This is the first number
        int y = 0; // This is the second number
        while (b) {
            // Si besoin, on récupère le message du serveur
            if (needRead) {
                lenBufR = is.read(bufR);
                needRead = false;
                message = new String(bufR, 0, lenBufR);
                System.out.println("Message du serveur = " + message);
            }

            if (lenBufR != -1) {
                if (firstState) {
                    // Si on n'a pas encore lu le premier nombre
                    x = readFirst(x, bufR, lenBufR);
                    System.out.println("x = " + x);
                } else {
                    y = readSecond(y, bufR, lenBufR);
                    System.out.println("y = " + y);

                    // Si on a fini de tout lire, il faut envoyer le message
                    if (firstState) {
                        bufE = new String(x + y + ";").getBytes();
                        os.write(bufE);
                        System.out.println("Message envoyé = " + (x + y) + ";");
                        x = 0;
                        y = 0;
                    }
                }
            }
        }
        socket.close();
    }

    private int readFirst(int start, byte[] buf, int len) {
        int result = start;
        while (i < len && buf[i] != '+') {
            int temp = Integer.parseInt(new String(buf, i, 1));
            result = 10 * result + temp;
            i++;
        }

        if (i != len) {
            // Si on a atteint le signe plus, c'est qu'on a lu tout le premier
            // nombre
            firstState = false;
            // On ne veut pas lire le signe plus
            i++;
        } else {
            needRead = true;
            i = 0;
        }

        return result;
    }

    private int readSecond(int start, byte[] buf, int len) {
        int result = start;
        // Ici on regarde si on a un signe = ou un ?
        while (i < len && buf[i] != '=' && buf[i] != '?') {
            int temp = Integer.parseInt(new String(buf, i, 1));
            result = 10 * result + temp;
            i++;
        }

        if (i != len && buf[i] == '?') {
            // Si on a atteint la fin de l'expression, alors on a tout ce
            // qu'il faut pour faire le calcul
            firstState = true;
            i++;
        } else if (i + 1 < len && buf[i] == '=' && buf[i + 1] == '?') {
            firstState = true;
            i += 2;
        } else {
            needRead = true;
            i = 0;
        }

        return result;
    }
}
