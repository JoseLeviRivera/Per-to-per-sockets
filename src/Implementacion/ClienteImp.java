package Implementacion;

import Model.ServerInfo;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ClienteImp{
    private static final Logger logger = Logger.getLogger(ClienteImp.class.getName());

    private String servidorIp;
    private int puerto;
    private List<ServerInfo> serverInfos;

    private String path;

    public ClienteImp() {
        serverInfos = new ArrayList<>();
    }

    public ClienteImp(String servidorIp, int puerto) {
        this.servidorIp = servidorIp;
        this.puerto = puerto;
        serverInfos = new ArrayList<>();
    }

    public ClienteImp(String servidorIp, int puerto, String path) {
        this.servidorIp = servidorIp;
        this.puerto = puerto;
        this.path = path;
        serverInfos = new ArrayList<>();
    }

    public void peticionListaServidores() {
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
            list = filtrarServidoresRepetidos(list);
            serverInfos.addAll(list);
            serverInfos.forEach((l) -> System.out.println("ip: " + l.getIp() + " puerto: " + l.getPuerto() + " path: " + l.getPath() + " nombre del archivo: " + l.getNombreArchivo()));
            socket.close();
        } catch (UnknownHostException e) {
            logger.warning("El host no existe o no está activo.");
        } catch (IOException e) {
            logger.warning("Error de entrada/salida.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void conectarConServidor() {
        System.out.println("conectando con servidores...");
        System.out.println(serverInfos);
        ServerInfo server = serverInfos.get(0);
        int puerto = server.getPuerto();
        String ip = server.getIp();
        //Creamos nuestro socket
        try {
            Socket socket = new Socket(ip, puerto);
            String ruta = path;
            //ruta = ruta.replace("\\", "\\\\");
            recibirArchivo(socket, ruta);
            System.out.println("Conectado al servidor: " + ip + ":" + puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void recibirArchivo(Socket socket, String rutaArchivoDestino) throws IOException {
        System.out.println("Procesando el archivo....");
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(rutaArchivoDestino);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        fileOutputStream.close();
        System.out.println("Finalizo el procesando del archivo....");

    }

    public void conectarConServidores(){
        System.out.println("Conectando con servidores de forma conccurrente. Tamaño de la lista: " + serverInfos.size() );
        for (ServerInfo servidor : serverInfos) {
            Thread hilo = new Thread(new ClienteRunnable(servidor));
            hilo.start();
        }
    }

    public List<ServerInfo> filtrarServidoresRepetidos(List<ServerInfo> serverInfos) {
        return serverInfos.stream()
                .collect(Collectors.groupingBy(server -> server.getIp() + server.getPuerto()))
                .values()
                .stream()
                .map(list -> list.get(0))
                .collect(Collectors.toList());
    }


    public class  ClienteRunnable implements Runnable {

        private ServerInfo serverInfo;
        public ClienteRunnable(){}
        public ClienteRunnable(ServerInfo serverInfo){
            this.serverInfo = serverInfo;
        }

        @Override
        public void run() {
            try {
                int puerto = serverInfo.getPuerto();
                String ip = serverInfo.getIp();

                Socket socket = new Socket(ip, puerto);
                System.out.println("Conectado al servidor: " + ip);

                // Realiza las operaciones de envío y recepción de datos con el servidor aquí

                // Cierra la conexión cuando hayas terminado
                socket.close();
                System.out.println("Desconectado del servidor: " + ip);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getServidorIp() {
        return servidorIp;
    }

    public void setServidorIp(String servidorIp) {
        this.servidorIp = servidorIp;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public List<ServerInfo> getServerInfos() {
        return serverInfos;
    }

    public void setServerInfos(List<ServerInfo> serverInfos) {
        this.serverInfos = serverInfos;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
