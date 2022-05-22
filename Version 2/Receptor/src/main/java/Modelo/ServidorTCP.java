package Modelo;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Observable;
import java.util.Observer;

public class ServidorTCP extends Observable implements Runnable {
    private ServerSocket socketServidor = null;
    private Socket socketCliente = null;
    private BufferedReader entrada = null;
    private PrintWriter salida = null;

    private Thread hilo = null;
    private boolean ejecutarHilo;

    private boolean aceptaEM = false;
    private boolean aceptaI = false;
    private boolean aceptaP = false;
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
            socketServidor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean NotificarServidor()
    {
        boolean servidorConectado = false;
        Socket socketCliente = null;

        BufferedReader entrada = null;
        PrintWriter salida = null;

        BufferedReader sc = new BufferedReader( new InputStreamReader(System.in));

        try {
            socketCliente = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(InformacionConfig.getInstance().getIpServidor(), InformacionConfig.getInstance().getPuertoServidor());
            socketCliente.setSoTimeout(10000);
            socketCliente.connect(socketAddress, 1000);
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(socketCliente.getOutputStream(), true);

            salida.println("1|"+InformacionConfig.getInstance().getPuertoReceptor()+"|001");

            servidorConectado = true;
            salida.close();
            entrada.close();
            sc.close();
            socketCliente.close();
        } catch (Exception e) {
            System.out.println("Tiempo de espera agotado para conectar al host");
        }
        return servidorConectado;
    }
    public void run() {
        try
        {
            if (NotificarServidor())
            {
                socketServidor = new ServerSocket(InformacionConfig.getInstance().getPuertoReceptor());
                do
                {
                    socketCliente = socketServidor.accept();
                    entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                    salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);

                    String rta = entrada.readLine();
                    MensajeEmergencia mensaje = new MensajeEmergencia(rta);
                    String tipoSolicitud = mensaje.getTipoEmergencia();

                    if((tipoSolicitud.equalsIgnoreCase("emergencia") && this.aceptaEM) || (tipoSolicitud.equalsIgnoreCase("Incendio") && this.aceptaI) || (tipoSolicitud.equalsIgnoreCase("Policia") && this.aceptaP)){
                        NotificarEmergencia(mensaje);
                        salida.println(MensajeEmisor);
                    }
                    else
                    {
                        salida.println(MensajeEmisorF);
                    }


                    entrada.close();
                    salida.close();
                    socketCliente.close();
                }
                while(ejecutarHilo);
            }
            else {
                NotificarErrorServidor();
            }
        }
        catch(Exception e)
        {
        }
    }

    private void NotificarEmergencia(MensajeEmergencia mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
    }
    private void NotificarErrorServidor()
    {
        setChanged();
        notifyObservers("No se ha podido conectarse con el servidor");
    }

    public void setAceptaEM(boolean resp){
        this.aceptaEM = resp;
    }
    public void setAceptaI(boolean resp){
        this.aceptaI = resp;
    }
    public void setAceptaP(boolean resp){
        this.aceptaP = resp;
    }
}
