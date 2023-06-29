package Implementacion;

import Model.ServerInfo;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorCentral implements Runnable{

    private List<ServerInfo> listaServidores;
    private String ip;
    private int port;

    private BufferedReader entrada;
    private DataOutputStream salida;

    public ServidorCentral(){
        this.listaServidores = new ArrayList<>();
    }

    public ServidorCentral(String ip, int port){
        super();
        this.ip = ip;
        this.port = port;
    }

    public void agregarServidorInfo(ServerInfo serverInfo){
        listaServidores.add(serverInfo);
    }

    public List<ServerInfo> obtenerListaServidores(){
        return listaServidores;
    }

    public List<ServerInfo> listaServers(){
        return List.of(
                new ServerInfo("12.34.564.3", 60, "asf", "afs"),
                new ServerInfo("12.34.564.3", 60, "asf", "afs"),
                new ServerInfo("12.34.564.3", 60, "asf", "afs")
                );
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
        try {
            int backlog = 1000;
            InetAddress ipAdd = InetAddress.getByName(this.ip);
            ServerSocket serverSocket = new ServerSocket(port, backlog, ipAdd);
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                // Para los canales de entrada y salida de datos
                entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                salida = new DataOutputStream(clientSocket.getOutputStream());
                // Para recibir el mensaje
                String mensajeRecibido = entrada.readLine();
                System.out.println(mensajeRecibido);

                if (mensajeRecibido.contains("Cliente")){
                    System.out.println("Va  mandar la lista al Cliente");
                    // Procesar la información del cliente servidor
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String ip = entrada.readLine();
                    int puerto = Integer.parseInt(entrada.readLine());
                    String path = entrada.readLine();
                    String nombreArchivo = entrada.readLine();
                    listaServidores.add(new ServerInfo(ip, puerto, path, nombreArchivo));
                    System.out.println(listaServidores);
                }
                if (mensajeRecibido.contains("Servidor")){
                    System.out.println("Va a agregar a la lista de servidores");
                    /*
                    ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    outputStream.writeObject(listaServers());
                    System.out.println("Lista de objetos enviada al cliente.");
                     */

                }
                clientSocket.close();
            }
            // Crear hilos para manejar múltiples clientes de forma concurrente
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
