package Proxy;

import Model.ServerInfo;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClienteNormal {
    private static final String IP_SERVIDOR_CENTRAL = "127.0.0.1";
    private static final int PUERTO_SERVIDOR_CENTRAL = 1234;

    public static void main(String[] args) {
        try {
            // Conectar al servidor central
            Socket socket = new Socket(IP_SERVIDOR_CENTRAL, PUERTO_SERVIDOR_CENTRAL);
            System.out.println("Conectado al servidor central.");

            // Obtener la lista de servidores
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            List<ServerInfo> servidores = (List<ServerInfo>) entrada.readObject();

            // Desconectarse del servidor central
            socket.close();

            // Conectarse a cada servidor y descargar el archivo
            for (ServerInfo servidor : servidores) {
                Socket servidorSocket = new Socket(servidor.getIp(), servidor.getPuerto());
                System.out.println("Conectado al servidor: " + servidor.getIp());

                // Descargar el archivo por bytes
                InputStream inputStream = servidorSocket.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream("archivo_descargado.dat");
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                servidorSocket.close();
                System.out.println("Archivo descargado desde el servidor: " + servidor.getIp());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

