package Modelo;

public class MensajeEmergencia
{
    private String tipoEmergencia;
    private String fecha;
    private String ubicacion;

    public MensajeEmergencia(String mensaje)
    {
        String[] partes = mensaje.split("#");

        tipoEmergencia = partes[0];
        fecha = partes[1];
        ubicacion = partes[2];
    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }
    public String getFecha()
    {
        return fecha;
    }
    public String getUbicacion()
    {
        return ubicacion;
    }
}
