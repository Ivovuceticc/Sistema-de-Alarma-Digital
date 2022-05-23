package Modelo;

public class MensajeEmisor
{
    private String tipoEmergencia;
    private String ubicacion;

    public MensajeEmisor(String[] mensaje)
    {
        ubicacion = mensaje[2];
        tipoEmergencia = mensaje[1];

    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }
    public String getUbicacion()
    {
        return ubicacion;
    }
}
