package Modelo;

import java.util.ArrayList;
import java.util.List;

public class RegistroEvento {
    String tipo;
    String ip;
    String puerto;
    String tiposDeSolicitud;
    String fechaYHora;

    public RegistroEvento(String tipo, String ip, String puerto, String tiposDeSolicitud,String fechaYHora) {
        this.tipo = tipo;
        this.ip = ip;
        this.puerto = puerto;
        this.fechaYHora = fechaYHora;
        this.tiposDeSolicitud = tiposDeSolicitud;
    }
    public RegistroEvento(String mensaje)
    {
        String[] m = mensaje.split("#");
        this.tipo = m[0];
        this.ip = m[1];
        this.puerto = m[2];
        this.tiposDeSolicitud = m[3];
        this.fechaYHora = m[4];
    }

    public String getTipo() {return tipo;}
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
    @Override
    public String toString()
    {
        return tipo+"#"+ip+"#"+puerto+"#"+tiposDeSolicitud+"#"+fechaYHora;
    }
}
