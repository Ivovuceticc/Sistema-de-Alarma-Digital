package Modelo;

public class Receptor {
    String ip;
    Integer puerto;

    public Receptor(String ip, Integer puerto) {
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
