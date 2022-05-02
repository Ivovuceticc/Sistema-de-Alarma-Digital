package Modelo;

public class Ubicacion {
    private String IP;
    private Integer puerto;
    private String direccion;


    public Ubicacion(String IP, Integer puerto,String direccion) {
        this.IP = IP;
        this.puerto = puerto;
        this.direccion = direccion;
    }

    public String getIP() {
        return IP;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public Ubicacion() {
    }

    public String getDireccion() {
        return direccion;
    }
}