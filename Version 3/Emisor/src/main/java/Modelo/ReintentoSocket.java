package Modelo;


public class ReintentoSocket {
    private Servidor[] servidor;
    private int indice = 0;
    private  int intentos = 0;
    public ReintentoSocket()
    {
        servidor = new Servidor[2];

        servidor[0] = InformacionConfig.getInstance().getServidor1();
        servidor[1] = InformacionConfig.getInstance().getServidor2();
    }

    public String getIP()
    {
        return servidor[indice].getIP();
    }
    public int getPuerto()
    {
        return servidor[indice].getPuerto();
    }
    public int getIntentos()
    {
        return intentos;
    }
    public void Reintentar()
    {
        indice++;
        if (indice >= servidor.length)
        {
            indice = 0;
            intentos++;
        }
    }
    public void Reiniciar()
    {
        indice = 0;
        intentos = 0;
    }
}
