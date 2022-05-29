package Modelo;

public class MensajeReceptor {
    private String puerto;
    private String tipoEmergencias;

    public MensajeReceptor(String[] mensaje) {
        this.puerto = mensaje[1];
        this.tipoEmergencias = mensaje[2];
    }

    public String getPuerto() {
        return puerto;
    }

    public String getTipoEmergencias() {
        return tipoEmergencias;
    }
}
