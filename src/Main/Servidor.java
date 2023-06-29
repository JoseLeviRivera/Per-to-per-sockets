package Main;

import Implementacion.ServidorImp;

//ip: 192.168.1.82
//puerto:  1234
public class Servidor {
    public static void main(String[] args)  {
        if(args.length > 0 ){
            String ipServidorCentral = args[0];
            int puertoServidorCentral = Integer.parseInt(args[1]);
            String ipServidor = args[2];
            int puertoServidor = Integer.parseInt(args[3]);
            String path = args[4];
            String nombre = args[5];
            ServidorImp s = new ServidorImp(ipServidorCentral, puertoServidorCentral,
                    ipServidor, puertoServidor, path, nombre);
            Thread hilo = new Thread(s);
            hilo.start();
        }
    }
}
