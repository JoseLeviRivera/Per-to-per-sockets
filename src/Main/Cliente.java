package Main;

import Implementacion.ClienteImp;

public class Cliente {
    public static void main(String[] args) {
        if(args.length > 0 ){
            String ip = args[0];
            int puerto = Integer.parseInt(args[1]);
            ClienteImp s = new ClienteImp(ip, puerto);
            s.peticionListaServidores();
            s.conectarConServidores();
            s.conectarConServidor();
            //Thread thread = new Thread(s);
            //thread.start();
        }
    }
}
