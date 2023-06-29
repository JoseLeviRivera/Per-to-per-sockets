package Implementacion;

import Model.ServerInfo;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ServidorImp implements Runnable{
    private String ipServidorCentral;
    private int puertoServidorCentral;

    private String ipServidor;
    private int puertoServidor;
    private String path;
    private String nombre;


    public ServidorImp(){}

    public ServidorImp(String servidorIp, int puerto){
        super();
        this.ipServidorCentral = servidorIp;
        this.puertoServidorCentral = puerto;
    }

    public ServidorImp(String servidorIp, int puerto, String ip, int port, String path, String nombre){
        this.ipServidorCentral = servidorIp;
        this.puertoServidorCentral = puerto;
        this.ipServidor = ip;
        this.puertoServidor = port;
        this.path = path;
        this.nombre = nombre;
    }

    public void obtenerInforServidor(){
        System.out.println(this);
    }

    public void conectarServidorCentral(){
        Socket socket;
        try {
            //Creamos nuestro socket
            socket = new Socket(ipServidorCentral, puertoServidorCentral);

            // obtiene los output(salidas) del stream de el socket
            OutputStream outputStream = socket.getOutputStream();
            // crea un objeto salida stream para mandar los objectos
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject("Servidor");
            ServerInfo data = new ServerInfo(ipServidor, puertoServidor, path, nombre);
            System.out.println("Mandado la data a la lista....");
            System.out.println(data);
            objectOutputStream.writeObject(data);
            // obtener la entrada de stream de la conexion de socket
            //InputStream inputStream = socket.getInputStream();
            // crea un DataInputStream para leer la lista de objectos
            //ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // lee la lista de servidores del socket
            //List<ServerInfo> list = (List<ServerInfo>) objectInputStream.readObject();
            //System.out.println("Recibido [" + list.size() + "] servidores servidores de: " + socket);
            // Imprime todos los servidores
            //System.out.println("Todos los servidores en linea(*):");
            //list.forEach((l)-> System.out.println("ip: " + l.getIp() + " puerto: " + l.getPuerto() + " path" + l.getPath() +  "nombre del archivo: " + l.getNombreArchivo()));
            //socket.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
            iniciarServidorCompartirArchivo();
        }
    }
    public void run() {
        Socket socket;
        try {
            //Creamos nuestro socket
            socket = new Socket(ipServidorCentral, puertoServidorCentral);

            // obtiene los output(salidas) del stream de el socket
            OutputStream outputStream = socket.getOutputStream();
            // crea un objeto salida stream para mandar los objectos
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject("Servidor");
            //System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            //String ipCliente = socket.getInetAddress().getHostAddress();
            //int puertoCliente = socket.getPort();
            ServerInfo data = new ServerInfo(ipServidor, puertoServidor, path, nombre);
            System.out.println("Mandado la data a la lista....");
            System.out.println(data);
            objectOutputStream.writeObject(data);
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
            iniciarServidorCompartirArchivo();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
            iniciarServidorCompartirArchivo();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void iniciarServidorCompartirArchivo(){
        try {
            //Cantidad de solicitudes que soporta el servidor central
            int max = 1000;
            //Declaracion de la red ip, se le pasara al socket
            InetAddress ipAdd = InetAddress.getByName(ipServidor);
            //Se inicaliza y se hace instancia de un servidor socket, se le pasa el puerto, max solicitudes, y la direccion ip
            ServerSocket serverSocket = new ServerSocket(puertoServidor, max, ipAdd);
            System.out.println("Servidor socket corriendo en " + getIpServidor() + ":" + getPuertoServidor());

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

    private void handleClient(Socket clientSocket) {
        try {
            enviarArchivo(clientSocket, path.concat(nombre)); // Ruta del archivo que deseas enviar
            System.out.println("El archivo que se envio esta en: " + path.contains(nombre));
            System.out.println("Archivo enviado al cliente...");
            // Cierre de conexiones
            clientSocket.close();
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void enviarArchivo(Socket socket, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        FileInputStream fileInputStream = new FileInputStream(archivo);
        OutputStream outputStream = socket.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        outputStream.close();
    }

    //getter y setters


    public String getIpServidorCentral() {
        return ipServidorCentral;
    }

    public void setIpServidorCentral(String ipServidorCentral) {
        this.ipServidorCentral = ipServidorCentral;
    }

    public int getPuertoServidorCentral() {
        return puertoServidorCentral;
    }

    public void setPuertoServidorCentral(int puertoServidorCentral) {
        this.puertoServidorCentral = puertoServidorCentral;
    }

    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public int getPuertoServidor() {
        return puertoServidor;
    }

    public void setPuertoServidor(int puertoServidor) {
        this.puertoServidor = puertoServidor;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "ServidorImp{" +
                "ipServidorCentral='" + ipServidorCentral + '\'' +
                ", puertoServidorCentral=" + puertoServidorCentral +
                ", ipServidor='" + ipServidor + '\'' +
                ", puertoServidor=" + puertoServidor +
                ", path='" + path + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
