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

    public static List<ServerInfo> listaServers(){
        return List.of(
                new ServerInfo("12.34.564.1", 60, "asf", "afs"),
                new ServerInfo("12.34.564.2", 60, "asf", "afs"),
                new ServerInfo("12.34.564.3", 60, "asf", "afs")
                );
    }

    private void handleClient(Socket clientSocket) {
        try{
            // obtener la entrada de stream de la conexion de socket
            InputStream inputStream = clientSocket.getInputStream();
            // crea un DataInputStream para leer la lista de objectos
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            //Obtiene que tipo es si cliente o servidor, en caso de ser cliente le manda las lista estatica
            //En caso de ser servidor agrega mas data
            String tipo = (String)objectInputStream.readObject();
            System.out.println(tipo);
            if (tipo.equalsIgnoreCase("Cliente")){
                // obtiene los output(salidas) del stream de el socket
                OutputStream outputStream = clientSocket.getOutputStream();
                // crea un objeto salida stream para mandar los objectos
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                System.out.println("Mandando la lista de servidores al cliente");
                objectOutputStream.writeObject(obtenerListaServidores());
            }
            if (tipo.equalsIgnoreCase("Servidor")){
                ServerInfo data =(ServerInfo) objectInputStream.readObject();
                agregarServidorInfo(data);
                System.out.println("Imprimiendo lista de servidores....");
                obtenerListaServidores().forEach((l)-> System.out.println("ip: " + l.getIp() + " puerto: " + l.getPuerto() + " path: " + l.getPath() +  " nombre del archivo: " + l.getNombreArchivo()));
            }
            // Cierre de conexiones
            clientSocket.close();
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        //Se inicializa la lista de servidores
        this.listaServidores = new ArrayList<>();

        try {
            //Cantidad de solicitudes que soporta el servidor central
            int max = 1000;
            //Declaracion de la red ip, se le pasara al socket
            InetAddress ipAdd = InetAddress.getByName(ip);
            //Se inicaliza y se hace instancia de un servidor socket, se le pasa el puerto, max solicitudes, y la direccion ip
            ServerSocket serverSocket = new ServerSocket(port, max, ipAdd);
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto " + port);

            //Bucle infinito de las solicitudes aceptadas
            while (true) {
                //Se espera hasta que inicie un cliente
                Socket clientSocket = serverSocket.accept();
                //Muestra informacion detallada
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
}
