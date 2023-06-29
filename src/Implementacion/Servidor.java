package Implementacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Servidor {
    public void correrServidor(String servidorIp, int puerto){
        try {
            Socket socket = new Socket(servidorIp, puerto);
            System.out.println("Cliente conectado al servidor " + servidorIp + ":" + puerto);
            // Obtener flujos de entrada y salida para la comunicación con el servidor
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            // Envío de mensajes al servidor
            salida.println("Cliente[tipo]: Servidor");
            // Lectura de respuestas del servidor
            String respuesta = entrada.readLine();
            System.out.println("Servidor: " + respuesta);
            socket.close();
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
