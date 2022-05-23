package Modelo;

import java.util.List;

public class RegistroEvento {
    String ip;
    String puerto;
    List<String> tipoSolicitud;
    String fechaYHora;

    public RegistroEvento(String ip, String puerto, List<String> tipoSolicitud, String fechaYHora) {
        this.ip = ip;
        this.puerto = puerto;
        this.tipoSolicitud = tipoSolicitud;
        this.fechaYHora = fechaYHora;
    }
}
