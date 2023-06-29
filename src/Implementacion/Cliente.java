package Implementacion;


import Model.ServerInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Cliente implements Runnable{

    private String servidorIp;
    private int puerto;

    private BufferedReader entrada;
    private DataOutputStream salida;

    public Cliente(){}

    public Cliente(String servidorIp, int puerto){
        this.servidorIp = servidorIp;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        Socket socket;
        DataOutputStream mensaje;

        try {
            //Creamos nuestro socket
            socket = new Socket(servidorIp, puerto);
            mensaje = new DataOutputStream(socket.getOutputStream());
            //Enviamos un mensaje
            mensaje.writeUTF("Cliente");
            /*
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            List<ServerInfo> listaServers = (List<ServerInfo>) inputStream.readObject();
            System.out.println("Lista de objetos recibida del servidor.");
            System.out.println(listaServers);
             */
            //Cerramos la conexión
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        }
    }
}
