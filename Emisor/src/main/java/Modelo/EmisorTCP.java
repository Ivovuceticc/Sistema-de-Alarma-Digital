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
    private String IP = "192.168.0.177";
    private String Puerto = "1111";
    private String MensajeEmisor = "recibido";

    public EmisorTCP(Observer observador)
    {
        addObserver(observador);
    }

    public void EnviarEmergencia(String tipoSolicitud, String fecha, String ubicacion)
    {
        Socket socketCliente = null;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos

        BufferedReader sc = new BufferedReader( new InputStreamReader(System.in));

        try{
            socketCliente = new Socket(IP, Integer.parseInt(Puerto));
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(socketCliente.getOutputStream(), true);

            //Manda la emergencia
            salida.println(tipoSolicitud+"#"+fecha+"#"+ubicacion);

            //Recibe la confirmacion
            String mensaje = entrada.readLine();
            if (MensajeEmisor.equals(mensaje))
            {
                NotificarEmergencia();
            }

            salida.close();
            entrada.close();
            sc.close();
            socketCliente.close();

        }
        catch(Exception e){
        }
    }
    private void NotificarEmergencia()
    {
        setChanged();
        notifyObservers(MensajeEmisor);
    }
}