package Modelo;

public class Servidor {
    String ip;
    Integer puerto;

    public Servidor(String ip, Integer puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getIP() {
        return ip;
    }

    public Integer getPuerto() {
        return puerto;
    }
}
