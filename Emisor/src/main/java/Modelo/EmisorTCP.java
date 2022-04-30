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

    private String IP = "192.168.0.16";
    private String Puerto = "1234";
    private String MensajeEmisor = "recibido";

    public EmisorTCP(Observer observador)
    {
        addObserver(observador);
    }

    public void EnviarEmergencia(String tipoSolicitud, String hora, String ubicacion)
    {
        Socket socketCliente = null;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos

        BufferedReader sc = new BufferedReader( new InputStreamReader(System.in));

        try{
            socketCliente = new Socket(IP, Integer.parseInt(Puerto));
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(socketCliente.getOutputStream(), true);
        }
        catch(Exception e){
            System.out.println("La ip ingresada es inválida...");
        }

        try{
            salida.println(tipoSolicitud+"#"+hora+"#"+ubicacion);
        }
        catch(Exception e){
        }

        try{
            while(true){
                String mensaje = entrada.readLine();
                if (MensajeEmisor.equals(mensaje))
                {
                    NotificarEmergencia();
                }
                break;
            }
        }
        catch(Exception e){
        }
        try {
            salida.close();
            entrada.close();
            sc.close();
            socketCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void NotificarEmergencia()
    {
        setChanged();
        notifyObservers(MensajeEmisor);
    }
}