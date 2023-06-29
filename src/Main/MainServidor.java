package Main;

import Implementacion.Servidor;

//ip: 192.168.1.82
//puerto:  1234
public class MainServidor {
    public static void main(String[] args)  {
        if(args.length > 0 ){
            String ip = args[0];
            int puerto = Integer.parseInt(args[1]);
            Servidor s = new Servidor(ip, puerto);
            Thread hilo = new Thread(s);
            hilo.start();
        }
    }
}
