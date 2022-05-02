package Modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author user
 */
public class EmisorTCP extends Observable {
    Ubicacion ubicacion;

    private String MensajeCorrecto = "recibido";
    private String MensajeNegativo = "No se pudo recibir";

    public EmisorTCP(Observer observador)
    {
        ubicacion = InformacionConfig.getInstance().getUbicacion();
        addObserver(observador);

    }

    private void lecturaUbicacion()
    {

    }

    public void EnviarEmergencia(String tipoSolicitud, String fecha)
    {
        Socket socketCliente = null;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos

        BufferedReader sc = new BufferedReader( new InputStreamReader(System.in));

        try{
            socketCliente = new Socket(ubicacion.getIP(), ubicacion.getPuerto());
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(socketCliente.getOutputStream(), true);

            //Manda la emergencia
            salida.println(tipoSolicitud+"#"+fecha+"#"+ubicacion.getDireccion());

            //Recibe la confirmacion
            String mensaje = entrada.readLine();
            NotificarEmergencia(MensajeCorrecto.equals(mensaje)? MensajeCorrecto:MensajeNegativo);

            salida.close();
            entrada.close();
            sc.close();
            socketCliente.close();

        }
        catch(Exception e){
        }
    }
    private void NotificarEmergencia(String mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
    }
}