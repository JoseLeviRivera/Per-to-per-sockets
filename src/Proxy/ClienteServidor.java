package Proxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteServidor {
    private static final String IP_SERVIDOR_CENTRAL = "127.0.0.1";
    private static final int PUERTO_SERVIDOR_CENTRAL = 1234;

    public static void main(String[] args) {
        try {
            // Conectar al servidor central
            Socket socket = new Socket(IP_SERVIDOR_CENTRAL, PUERTO_SERVIDOR_CENTRAL);
            System.out.println("Conectado al servidor central.");

            // Enviar información al servidor central
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            salida.println("192.168.1.2");  // IP del cliente servidor
            salida.println(5000);           // Puerto del cliente servidor
            salida.println("/path/al/archivo");  // Ruta del archivo en el cliente servidor
            salida.println("archivo.dat");  // Nombre del archivo

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

