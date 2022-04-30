package Modelo;

public class MensajeEmergencia
{
    private String tipoEmergencia;
    private String horario;
    private String direccion;

    public MensajeEmergencia(String mensaje)
    {
        String[] partes = mensaje.split("#");

        tipoEmergencia = partes[0];
        horario = partes[1];
        direccion = partes[2];
    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }
    public String getHorario()
    {
        return horario;
    }
    public String getDireccion()
    {
        return direccion;
    }
}
