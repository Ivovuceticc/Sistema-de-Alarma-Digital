package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Receptor {
    String ip;
    Integer puerto;
    List<String> tipoSolicitudes = new ArrayList<>();

    public Receptor(String ip, Integer puerto, String solicitudes) {
        this.ip = ip;
        this.puerto = puerto;
        if (solicitudes.charAt(0) == '1') {
            this.tipoSolicitudes.add("Medica");
        }
        if (solicitudes.charAt(1) == '1')
        {
            this.tipoSolicitudes.add("Incendio");
        }
        if (solicitudes.charAt(2) == '1')
        {
            this.tipoSolicitudes.add("Policial");
        }
    }

    public String getIP() {
        return ip;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public List<String> getTipoSolicitudes() {
        return tipoSolicitudes;
    }
}
