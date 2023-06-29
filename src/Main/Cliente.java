package Main;

import Implementacion.ClienteImp;

//C:\tmp

public class Cliente {
    public static void main(String[] args) {
        if(args.length > 0 ){
            String ip = args[0];
            int puerto = Integer.parseInt(args[1]);
            String path = args[2];
            ClienteImp s = new ClienteImp(ip, puerto, path);
            s.peticionListaServidores();
            s.conectarConServidores();
            s.conectarConServidor();
            //Thread thread = new Thread(s);
            //thread.start();
        }
    }
}
