package Modelo;

import java.util.ArrayList;
import java.util.List;

public class RegistroEvento {
    String ip;
    String puerto;
    String tiposDeSolicitud;
    String fechaYHora;

    public RegistroEvento(String ip, String puerto, String tiposDeSolicitud,String fechaYHora) {
        this.ip = ip;
        this.puerto = puerto;
        this.fechaYHora = fechaYHora;
        this.tiposDeSolicitud = tiposDeSolicitud;
    }

    public String getIp() {
        return ip;
    }

    public String getPuerto() {
        return puerto;
    }

    public String getTiposDeSolicitud() {
        return tiposDeSolicitud;
    }

    public String getFechaYHora() {
        return fechaYHora;
    }
}
