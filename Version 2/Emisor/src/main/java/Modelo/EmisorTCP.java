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
public class EmisorTCP extends Observable {
    Ubicacion ubicacion;
    Servidor servidor;

    private String MensajeCorrecto = "recibido";
    private String MensajeNegativo = "No se pudo recibir";

    public String getUbicacion()
    {
        return ubicacion.getDireccion();
    }

    public EmisorTCP(Observer observador)
    {
        ubicacion = InformacionConfig.getInstance().getUbicacion();
        servidor = InformacionConfig.getInstance().getServidor();
        addObserver(observador);
    }

    public void EnviarEmergencia(String tipoSolicitud, String fecha)
    {
        Socket socketCliente = null;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos


        try {
                socketCliente = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(servidor.getIP(), servidor.getPuerto());
                socketCliente.setSoTimeout(10000);
                socketCliente.connect(socketAddress, 1000);
                entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                salida = new PrintWriter(socketCliente.getOutputStream(), true);


                //Manda la emergencia
                salida.println("0#" + tipoSolicitud + "#" + ubicacion.getDireccion());

                //Recibe la confirmacion
                String mensaje = entrada.readLine();

                if (MensajeCorrecto.equals(mensaje))
                {
                    NotificarEmergencia(MensajeCorrecto);
                }
                else
                {
                    NotificarEmergencia(MensajeNegativo);
                }

                salida.close();
                entrada.close();
                socketCliente.close();
            } catch (Exception e) {
                System.out.println("Tiempo de espera agotado para conectar al host");
                NotificarEmergencia("No es posible conectarse con el servidor");
        }
    }
    private void NotificarEmergencia(String mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
    }
}