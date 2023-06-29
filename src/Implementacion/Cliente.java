package Implementacion;


import Model.ServerInfo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Cliente implements Runnable{

    private String servidorIp;
    private int puerto;

    public Cliente(){}

    public Cliente(String servidorIp, int puerto){
        this.servidorIp = servidorIp;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        Socket socket;


        try {
            //Creamos nuestro socket
            socket = new Socket(servidorIp, puerto);

            // obtener la entrada de stream de la conexion de socket
            InputStream inputStream = socket.getInputStream();
            // crea un DataInputStream para leer la lista de objectos
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            // lee la lista de servidores del socket
            List<ServerInfo> list = (List<ServerInfo>) objectInputStream.readObject();
            System.out.println("Recibido [" + list.size() + "] servidores servidores de: " + socket);
            // Imprime todos los servidores
            System.out.println("Todos los servidores en linea(*):");
            list.forEach((l)-> System.out.println("ip: " + l.getIp() + " puerto: " + l.getPuerto() + " path" + l.getPath() +  "nombre del archivo: " + l.getNombreArchivo()));
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
