package Modelo;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Observable;
import java.util.Observer;

public class ReceptorTCP extends Observable implements Runnable {
    private ServerSocket socketServidor = null;
    private Socket socketCliente = null;
    private BufferedReader entrada = null;
    private PrintWriter salida = null;

    private Thread hilo = null;
    private boolean ejecutarHilo;

    private boolean aceptaEM = false;
    private boolean aceptaI = false;
    private boolean aceptaP = false;
    private String MensajeEmisor = "3";

    private Observer observador;

    ReintentoSocket reintentoSocket;
    private String getEmergencias()
    {
        return (aceptaEM? "1":"0") + (aceptaI?"1":"0")+ (aceptaP?"1":"0");
    }

    public ReceptorTCP(Observer observador){
        this.observador = observador;
        reintentoSocket = new ReintentoSocket();
        //addObserver(observador);
    }

    public void Iniciar()
    {
        hilo = new Thread(this);
        ejecutarHilo = true;
        hilo.start();
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

    public boolean NotificarServidor() {
        boolean servidorConectado = false;
        NotificarEstadoConexion("enviandoRegistro");
        reintentoSocket.Reiniciar();
        while (!servidorConectado)
        {
            System.out.println("Intento numero " + reintentoSocket.getIntentos());
            try {
                Socket socketCliente = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(reintentoSocket.getIP(), reintentoSocket.getPuerto());
                socketCliente.setSoTimeout(2000);
                socketCliente.connect(socketAddress, 1000);
                PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);

                salida.println("1#" + InformacionConfig.getInstance().getPuertoReceptor() + "#" + getEmergencias());

                salida.close();
                socketCliente.close();
                servidorConectado = true;
            } catch (IOException e) {
                reintentoSocket.Reintentar();
            }
        }
        return servidorConectado;
    }
    @Override
    public void run() {
        if (NotificarServidor())
        {
            try
            {
                NotificarEstadoConexion("registrado");
                socketServidor = new ServerSocket(InformacionConfig.getInstance().getPuertoReceptor());
                do
                {
                    socketCliente = socketServidor.accept();
                    entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                    salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())),true);

                    String rta = entrada.readLine();
                    MensajeEmergencia mensaje = new MensajeEmergencia(rta);

                    NotificarEmergencia(mensaje);
                    //salida.println(MensajeEmisor);

                    entrada.close();
                    salida.close();
                    socketCliente.close();
                }
                while(ejecutarHilo);
            }
            catch(Exception e)
            {
            }
        } else {
            //NotificarEstadoConexion("fallo");
            //NotificarErrorServidor();
        }
    }

    private void NotificarEmergencia(MensajeEmergencia mensaje)
    {
        setChanged();
        observador.update(this, mensaje);
    }
    private void NotificarErrorServidor()
    {
        setChanged();
        observador.update(null, "No es posible conectarse con el servidor");
    }
    private void NotificarEstadoConexion(String estado)
    {
        setChanged();
        observador.update(null, estado);
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
