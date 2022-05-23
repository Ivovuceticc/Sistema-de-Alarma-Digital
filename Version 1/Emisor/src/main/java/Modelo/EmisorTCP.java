package Modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author user
 */
public class EmisorTCP extends Observable {
    Ubicacion ubicacion;
    List<Receptor> receptores;

    private String MensajeCorrecto = "recibido";
    private String MensajeNegativo = "No se pudo recibir";

    public String getUbicacion()
    {
        return ubicacion.getDireccion();
    }

    public EmisorTCP(Observer observador)
    {
        ubicacion = InformacionConfig.getInstance().getUbicacion();
        receptores = InformacionConfig.getInstance().getReceptores();
        addObserver(observador);
    }

    public void EnviarEmergencia(String tipoSolicitud, String fecha)
    {
        boolean encontrado = false;
        Socket socketCliente = null;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos

        BufferedReader sc = new BufferedReader( new InputStreamReader(System.in));
        for (Receptor receptor:
                receptores) {
            try {
                socketCliente = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(receptor.getIP(), receptor.getPuerto());
                socketCliente.setSoTimeout(10000);
                socketCliente.connect(socketAddress, 1000);
                entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                salida = new PrintWriter(socketCliente.getOutputStream(), true);


                //Manda la emergencia
                salida.println(tipoSolicitud + "#" + fecha + "#" + ubicacion.getDireccion());

                //Recibe la confirmacion
                String mensaje = entrada.readLine();
                //NotificarEmergencia(MensajeCorrecto.equals(mensaje) ? MensajeCorrecto : MensajeNegativo);
                if (!encontrado && MensajeCorrecto.equals(mensaje))
                {
                    encontrado = true;
                }

                salida.close();
                entrada.close();
                sc.close();
                socketCliente.close();
            } catch (Exception e) {
                System.out.println("Tiempo de espera agotado para conectar al host");
            }
        }
        if (encontrado)
        {
            NotificarEmergencia(encontrado ? MensajeCorrecto : MensajeNegativo);
        }
    }
    private void NotificarEmergencia(String mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
    }
}