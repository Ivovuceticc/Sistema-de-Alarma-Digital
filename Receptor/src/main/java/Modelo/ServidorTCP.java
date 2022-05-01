package Modelo;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ServidorTCP extends Observable implements Runnable {
    private ServerSocket socketServidor = null;
    private Socket socketCliente = null;
    private BufferedReader entrada = null;
    private PrintWriter salida = null;

    private Thread hilo = null;
    private boolean ejecutarHilo;
    private String MensajeEmisor = "recibido";
    private String MensajeEmisorF = "Solicitud invalida";

    public ServidorTCP(Observer observador){
        addObserver(observador);
    }

    public void Iniciar()
    {
        hilo = new Thread(this);
        ejecutarHilo = true;
        hilo.run();
    }
    public void Detener()
    {
        ejecutarHilo = false;
        try {
            CerrarServidor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try
        {
            String tipoSolicitud = InformacionConfig.getInstance().getTipoSolicitud();
            IniciarServidor();
            do
            {
                socketCliente = socketServidor.accept();
                entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);

                String rta = entrada.readLine();
                MensajeEmergencia mensaje = new MensajeEmergencia(rta);

                if(mensaje.getTipoEmergencia().equalsIgnoreCase(tipoSolicitud)) {
                    NotificarEmergencia(mensaje);
                    salida.println(MensajeEmisor);
                }
                else
                    salida.println(MensajeEmisorF);

                entrada.close();
                salida.close();
                socketCliente.close();
            }
            while(ejecutarHilo);
        }
        catch(Exception e)
        {
        }
    }

    private void IniciarServidor() throws IOException
    {
        socketServidor = new ServerSocket(Integer.parseInt(InformacionConfig.getInstance().getPuerto()));
    }
    private void CerrarServidor() throws IOException
    {
        socketServidor.close();
    }

    private void NotificarEmergencia(MensajeEmergencia mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
    }
}
