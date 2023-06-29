package Proxy;

import Model.ServerInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorCentral {
    private static final int PUERTO = 1234;
    private List<ServerInfo> servidores;

    public ServidorCentral() {
        servidores = new ArrayList<>();
    }

    public void iniciar() {
        try {
            ServerSocket servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor central iniciado. Esperando conexiones...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());

                // Procesar la información del cliente servidor
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                String ip = entrada.readLine();
                int puerto = Integer.parseInt(entrada.readLine());
                String path = entrada.readLine();
                String nombreArchivo = entrada.readLine();
                servidores.add(new ServerInfo(ip, puerto, path, nombreArchivo));

                // Enviar la lista de servidores al cliente normal
                ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream());
                salida.writeObject(servidores);
                salida.flush();

                clienteSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServidorCentral servidorCentral = new ServidorCentral();
        servidorCentral.iniciar();
    }
}
