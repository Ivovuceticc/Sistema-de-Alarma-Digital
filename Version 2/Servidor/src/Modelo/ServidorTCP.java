package Modelo;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    private MensajeEmisor mensajeEmisor;
    private MensajeReceptor mensajeReceptor;
    private List<Receptor> receptores;

    public ServidorTCP(Observer observador){
        addObserver(observador);
        receptores = new ArrayList<>();
    }

    public void iniciar()
    {
        hilo = new Thread(this);
        ejecutarHilo = true;
        hilo.start();
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
            IniciarServidor();
            do
            {
                RegistroEvento evento;
                socketCliente = socketServidor.accept();
                entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
                String rta = entrada.readLine();
                String[] mensaje =  rta.split("#");
                /*
                if(mensaje.getTipoEmergencia().equalsIgnoreCase(tipoSolicitud)) {
                    NotificarEmergencia(mensaje);
                    salida.println(MensajeEmisor);
                }
                else
                    salida.println(MensajeEmisorF);
                */

                String fecha = getFecha();
                if (mensaje[0].equals("0")) ///recibo emergencia de emisor
                {
                    mensajeEmisor = new MensajeEmisor(mensaje);
                    if (receptorTipoEmergencia(mensajeEmisor.getTipoEmergencia())) {

                        enviarEmergencia(mensajeEmisor.getTipoEmergencia(),fecha); ///envia Emergencia a receptor
                        salida.println(MensajeEmisor);
                    }
                    else
                    {
                        salida.println(MensajeEmisorF);
                    }

                    evento = new RegistroEvento(socketCliente.getInetAddress().toString(),InformacionConfig.getInstance().getPuerto(),mensajeEmisor.getTipoEmergencia(),fecha);
                    notificarEvento(evento);
                }
                else if(mensaje[0].equals("1")) ///registro un receptor
                {
                    Receptor receptor;
                    mensajeReceptor = new MensajeReceptor(mensaje);
                    receptor = new Receptor(socketCliente.getInetAddress().toString().substring(1),Integer.parseInt(mensajeReceptor.getPuerto()),mensajeReceptor.getTipoEmergencias());
                    receptores.add(receptor);
                    String tipoSolicitudes = "";
                    StringBuilder sb = new StringBuilder();
                    for(String s : receptor.getTipoSolicitudes())
                    {
                        tipoSolicitudes = sb.append(s).toString();
                    }
                    evento = new RegistroEvento(socketCliente.getInetAddress().toString(),InformacionConfig.getInstance().getPuerto(),tipoSolicitudes,fecha);
                    notificarEvento(evento);
                }

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

    public Boolean receptorTipoEmergencia (String tipoSolicitud) {
        int i = 0;
        while (i < receptores.size() && receptores.get(i).getTipoSolicitudes().stream().filter(s -> s.equalsIgnoreCase(tipoSolicitud)).count() == 0) {
          i++;
        }
        return (i<receptores.size());
    }


    public String getFecha() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


    public void enviarEmergencia(String tipoSolicitud, String fecha)
    {
        Socket socketCliente = null;

        BufferedReader entrada = null; //leer texto de secuencia de entrada
        PrintWriter salida = null; //crear y escribir archivos
        RegistroEvento evento;
        BufferedReader sc = new BufferedReader( new InputStreamReader(System.in));
        for(Receptor receptor : receptores) {
             if(receptor.getTipoSolicitudes().stream().filter(s -> s.equalsIgnoreCase(tipoSolicitud)).count() > 0) {

                 try {
                     socketCliente = new Socket();
                     SocketAddress socketAddress = new InetSocketAddress(receptor.getIP(), receptor.getPuerto());
                     socketCliente.setSoTimeout(10000);
                     socketCliente.connect(socketAddress, 1000);
                     entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                     salida = new PrintWriter(socketCliente.getOutputStream(), true);
                     evento = new RegistroEvento(socketAddress.toString(),receptor.getPuerto().toString(),tipoSolicitud,fecha);
                     //Manda la emergencia
                     salida.println(tipoSolicitud + "#" + fecha + "#" + mensajeEmisor.getUbicacion());

                     notificarEvento(evento);
                     //Recibe la confirmacion
                    /// String mensaje = entrada.readLine();
                     ///envia mensaje a emisor

                     salida.close();
                     entrada.close();
                     sc.close();
                     socketCliente.close();
                 } catch (Exception e) {
                     System.out.println("Tiempo de espera agotado para conectar al host");
                 }
             }
        };
    }

    private void notificarEvento(RegistroEvento evento)
    {
        setChanged();
        notifyObservers(evento);
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
