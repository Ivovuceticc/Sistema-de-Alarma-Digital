package Modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author user
 */
public class EmisorTCP extends Observable implements Runnable {

    private Thread hiloEmisor = null;
    private Ubicacion ubicacion;

    private String MensajeCorrecto = "recibido";
    private String MensajeNegativo = "No se pudo recibir";

    private String tipoSolicitud;
    private String fecha;

    private ReintentoSocket reintentoSocket;

    public String getUbicacion()
    {
        return ubicacion.getDireccion();
    }

    public EmisorTCP(Observer observador)
    {
        reintentoSocket = new ReintentoSocket();
        ubicacion = InformacionConfig.getInstance().getUbicacion();
        addObserver(observador);
    }

    //TESTING
    @Override
    public void run() {
        String tipoSolicitud = this.tipoSolicitud;
        Socket socketCliente = null;
        Boolean exitoEmergencia = false;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos
        while (!exitoEmergencia) {
            try {
                if (tipoSolicitud.equals("emergencia"))
                {
                    socketCliente = new Socket();
                    SocketAddress socketAddress = new InetSocketAddress("192.168.0.14", 1212);
                    socketCliente.setSoTimeout(10000);
                    socketCliente.connect(socketAddress, 1000);
                    salida = new PrintWriter(socketCliente.getOutputStream(), true);


                    //Manda la emergencia
                    salida.println("primario");

                    exitoEmergencia = true;
                    salida.close();
                    socketCliente.close();
                }
                else {
                    if (tipoSolicitud.equals("incendio"))
                    {
                        socketCliente = new Socket();
                        SocketAddress socketAddress = new InetSocketAddress("192.168.0.14", 1112);
                        socketCliente.setSoTimeout(10000);
                        socketCliente.connect(socketAddress, 1000);
                        entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                        salida = new PrintWriter(socketCliente.getOutputStream(), true);


                        //Manda la emergencia
                        salida.println("secundario"+"#192.168.0.14"+"#1113");

                        exitoEmergencia = true;
                        salida.close();
                        entrada.close();
                        socketCliente.close();
                    }
                    else {
                        socketCliente = new Socket();
                        SocketAddress socketAddress = new InetSocketAddress("192.168.0.14", 1113);
                        socketCliente.setSoTimeout(10000);
                        socketCliente.connect(socketAddress, 1000);
                        entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                        salida = new PrintWriter(socketCliente.getOutputStream(), true);


                        //Manda la emergencia
                        salida.println("000"+"#192.168.0.14"+"#1113");
                        salida.println("010"+"#192.168.0.15"+"#1115");
                        salida.println("011"+"#192.168.0.18"+"#1116");

                        exitoEmergencia = true;
                        salida.close();
                        entrada.close();
                        socketCliente.close();
                    }
                }
            } catch (Exception e) {
            }
        }
        hiloEmisor = null;
        NotificarEmergencia("emergenciaDisponible");
    }

    /*@Override
    public void run() {
        String tipoSolicitud = this.tipoSolicitud;
        Socket socketCliente = null;
        Boolean exitoEmergencia = false;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos
        reintentoSocket.Reiniciar();
        while (!exitoEmergencia) {
            System.out.println("Intento numero " + reintentoSocket.getIntentos());
            try {
                socketCliente = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(reintentoSocket.getIP(), reintentoSocket.getPuerto());
                socketCliente.setSoTimeout(10000);
                socketCliente.connect(socketAddress, 1000);
                entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                salida = new PrintWriter(socketCliente.getOutputStream(), true);


                //Manda la emergencia
                salida.println("0#" + tipoSolicitud + "#" + ubicacion.getDireccion());

                //Recibe la confirmacion
                String mensaje = entrada.readLine();

                if (MensajeCorrecto.equals(mensaje)) {
                    NotificarEmergencia(MensajeCorrecto);
                } else {
                    NotificarEmergencia(MensajeNegativo);
                }

                exitoEmergencia = true;
                salida.close();
                entrada.close();
                socketCliente.close();
            } catch (Exception e) {
                reintentoSocket.Reintentar();
            }
        }
        if (!exitoEmergencia)
        {
            //NotificarEmergencia("No es posible conectarse con el servidor");
        }
        hiloEmisor = null;
        NotificarEmergencia("emergenciaDisponible");
    }*/
    public void EnviarEmergencia(String tipoSolicitud, String fecha)
    {
        if (hiloEmisor == null)
        {
            this.tipoSolicitud = tipoSolicitud;
            this.fecha = fecha;

            hiloEmisor = new Thread(this);
            hiloEmisor.start();
            NotificarEmergencia("enviandoEmergencia");
        }
    }
    private void NotificarEmergencia(String mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
    }
}