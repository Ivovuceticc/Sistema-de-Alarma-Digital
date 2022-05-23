package Modelo;

public class MensajeEmisor
{
    private String tipoEmergencia;
    private String ubicacion;

    public MensajeEmisor(String[] mensaje)
    {
        tipoEmergencia = mensaje[1];
        ubicacion = mensaje[2];
    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }
    public String getUbicacion()
    {
        return ubicacion;
    }
}
