package Implementacion;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Servidor implements Runnable{
    private String servidorIp;
    private int puerto;

    public Servidor(){}

    public Servidor(String servidorIp, int puerto){
        this.servidorIp = servidorIp;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        Socket socket;
        try {
            //Creamos nuestro socket
            socket = new Socket(servidorIp, puerto);
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        }
    }
}
