package Main;

import Implementacion.Cliente;
import Implementacion.Servidor;

public class MainCliente {
    public static void main(String[] args) {
        if(args.length > 0 ){
            String ip = args[0];
            int puerto = Integer.parseInt(args[1]);
            Cliente s = new Cliente(ip, puerto);
            Thread thread = new Thread(s);
            thread.start();
        }
    }
}
