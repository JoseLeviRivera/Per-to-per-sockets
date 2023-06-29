package Implementacion;


import Model.ServerInfo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteImp implements Runnable{

    private String servidorIp;
    private int puerto;

    public ClienteImp(){}

    public ClienteImp(String servidorIp, int puerto){
        this.servidorIp = servidorIp;
        this.puerto = puerto;
    }

    public void comunicarServidorCentral(){
        Socket socket;
        try {
            //Creamos nuestro socket
            socket = new Socket(servidorIp, puerto);

            // obtiene los output(salidas) del stream de el socket
            OutputStream outputStream = socket.getOutputStream();
            // crea un objeto salida stream para mandar los objectos
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject("Cliente");

            // obtener la entrada de stream de la conexion de socket
            InputStream inputStream = socket.getInputStream();
            // crea un DataInputStream para leer la lista de objectos
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // lee la lista de servidores del socket
            List<ServerInfo> list = (List<ServerInfo>) objectInputStream.readObject();
            System.out.println("Recibido [" + list.size() + "] servidores servidores de: " + socket);
            // Imprime todos los servidores
            System.out.println("Todos los servidores en linea(*):");
            list.forEach((l)-> System.out.println("ip: " + l.getIp() + " puerto: " + l.getPuerto() + " path: " + l.getPath() +  " nombre del archivo: " + l.getNombreArchivo()));
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket socket;
        try {
            //Creamos nuestro socket
            socket = new Socket(servidorIp, puerto);

            // obtiene los output(salidas) del stream de el socket
            OutputStream outputStream = socket.getOutputStream();
            // crea un objeto salida stream para mandar los objectos
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject("Cliente");

            // obtener la entrada de stream de la conexion de socket
            InputStream inputStream = socket.getInputStream();
            // crea un DataInputStream para leer la lista de objectos
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // lee la lista de servidores del socket
            List<ServerInfo> list = (List<ServerInfo>) objectInputStream.readObject();
            System.out.println("Recibido [" + list.size() + "] servidores servidores de: " + socket);
            // Imprime todos los servidores
            System.out.println("Todos los servidores en linea(*):");
            System.out.println("Filtrando: ");
            list = filtrarServidoresRepetidos(list);
            list.forEach((l)-> System.out.println("ip: " + l.getIp() + " puerto: " + l.getPuerto() + " path: " + l.getPath() +  " nombre del archivo: " + l.getNombreArchivo()));
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ServerInfo> filtrarServidoresRepetidos(List<ServerInfo> serverInfos){
       return serverInfos.stream()
                .collect(Collectors.groupingBy(server -> server.getIp() + server.getPuerto()))
                .values()
                .stream()
                .map(list -> list.get(0))
                .collect(Collectors.toList());
    }
}
