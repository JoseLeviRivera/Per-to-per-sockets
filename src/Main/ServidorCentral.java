package Main;

import Implementacion.ServidorCentraImp;

//ip: 192.168.0.8
//puerto:  1234
public class ServidorCentral {
    public static void main(String[] args)  {
        if(args.length > 0 ){
            int puerto = Integer.parseInt(args[1]);
            String ip = args[0];
            ServidorCentraImp s = new ServidorCentraImp(ip, puerto);
            Thread r = new Thread(s);
            r.start();
        }
    }
}
