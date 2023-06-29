package Main;

import Implementacion.ServidorCentral;

//ip: 192.168.1.82
//puerto:  1234
public class MainServidorCentral {
    public static void main(String[] args)  {
        if(args.length > 0 ){
            int puerto = Integer.parseInt(args[1]);
            String ip = args[0];
            ServidorCentral s = new ServidorCentral(ip, puerto);
            Thread r = new Thread(s);
            r.start();
        }
    }
}
