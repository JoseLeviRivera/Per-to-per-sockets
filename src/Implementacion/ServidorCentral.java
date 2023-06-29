package Implementacion;

import Model.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorCentral implements Runnable{

    private List<ServerInfo> listaServidores;

    public ServidorCentral(){
        this.listaServidores = new ArrayList<>();
    }

    public void agregarServidorInfo(ServerInfo serverInfo){
        listaServidores.add(serverInfo);
    }

    public List<ServerInfo> obtenerListaServidores(){
        return listaServidores;
    }

    public void iniciarServidor(int puerto, String ip){
        try {
            int backlog = 1000;
            InetAddress ipAdd = InetAddress.getByName(ip);

            ServerSocket serverSocket = new ServerSocket(puerto, backlog, ipAdd);
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + puerto);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                // Crear hilos para manejar múltiples clientes de forma concurrente
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter salida = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                // Responder al cliente
                salida.println("Hola desde Servidor!");
            }
            // Cierre de conexiones
            clientSocket.close();
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
