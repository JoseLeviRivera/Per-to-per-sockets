package Implementacion;

import Model.ServerInfo;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Servidor implements Runnable{

    private String servidorIp;
    private int puerto;

    private BufferedReader entrada;
    private DataOutputStream salida;

    public Servidor(){}

    public Servidor(String servidorIp, int puerto){
        this.servidorIp = servidorIp;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        Socket socket;
        DataOutputStream mensaje;
        HashMap<String, ServerInfo> data = new HashMap<>();
        try {
            //Creamos nuestro socket
            socket = new Socket(servidorIp, puerto);
            mensaje = new DataOutputStream(socket.getOutputStream());
            //Enviamos un mensaje
            data.put("Servidor", null);
            mensaje.writeUTF("Servidor");
            Thread.sleep(1000);
            // Enviar información al servidor central
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            salida.println("192.168.1.2");  // IP del cliente servidor
            salida.println(5000);           // Puerto del cliente servidor
            salida.println("/path/al/archivo");  // Ruta del archivo en el cliente servidor
            salida.println("archivo.dat");  // Nombre del archivo
            //Cerramos la conexión
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
