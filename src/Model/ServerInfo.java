package Model;

public class ServerInfo {
    private String ip;
    private int puerto;
    private String path;
    private String nombreArchivo;

    public ServerInfo(){}

    public ServerInfo(String ip, int puerto, String path, String nombreArchivo) {
        this.ip = ip;
        this.puerto = puerto;
        this.path = path;
        this.nombreArchivo = nombreArchivo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}
