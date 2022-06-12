package Modelo;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServidorTCP extends Observable implements Runnable, IServidorState {
    private ServerSocket socketServidor = null;
    private Socket socketCliente = null;
    private BufferedReader entrada = null;
    private PrintWriter salida = null;

    private Thread hilo = null;
    private Thread hiloMonitor = null;
    private Thread hiloPrimario = null;
    private boolean ejecutarHilo;

    private boolean aceptaEM = false;
    private boolean aceptaI = false;
    private boolean aceptaP = false;
    private String MensajeEmisor = "recibido";
    private String MensajeEmisorF = "Solicitud invalida";

    private MensajeEmisor mensajeEmisor;
    private MensajeReceptor mensajeReceptor;
    private List<Receptor> receptores;
    private List<ServidorSecundario> secundarios;
    private State servidorState;

    private Observer observador;

    public ServidorTCP(Observer observador){
        //addObserver(observador);
        this.observador = observador;
        receptores = new ArrayList<>();

        hiloMonitor = new Thread(new HiloMonitor());
        hiloMonitor.start();
        notificarRol("Ninguno");
        //serSecundario("192.168.0.14",1211);
        serPrimario();
        receptores.add(new Receptor("192.111.11.1", 1234, "001"));
        receptores.add(new Receptor("192.111.11.2", 1234, "001"));
        receptores.add(new Receptor("192.111.11.2", 1234, "001"));
        receptores.add(new Receptor("192.111.11.2", 1234, "001"));
    }
    //-------------------------------------------------------------------------------------------
    //HILOS
    private class HiloMonitor implements Runnable {
        private boolean ejecutando = true;
        public void run()
        {
            try
            {
                do {
                    ServerSocket socketServidor = new ServerSocket(Integer.parseInt(InformacionConfig.getInstance().getPuertoMonitor()));
                    Socket socketCliente = socketServidor.accept();
                    PrintWriter salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                    String rta = entrada.readLine();
                    if (rta != null)
                    {
                        if (rta.equals("ping"))
                        {
                            salida.println("echo");
                        }
                        else
                        {
                            if (rta.equals("primario"))
                            {
                                serPrimario();
                            }
                            else
                            {
                                String[] info =  rta.split("#");
                                if (info[0].equals("secundario"))
                                {
                                    serSecundario(info[1], Integer.parseInt(info[2]));
                                }
                            }
                        }
                    }
                    entrada.close();
                    salida.close();
                    socketCliente.close();
                    socketServidor.close();
                }
                while (ejecutando);
            }
            catch (IOException e)
            {
            }
        }
    }
    private class HiloPrimario implements Runnable {
        private boolean ejecutando = true;
        public void run()
        {
            try
            {
                ServerSocket socketServidor = new ServerSocket(Integer.parseInt(InformacionConfig.getInstance().getPuertoServidor()));
                Socket socketCliente = socketServidor.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                PrintWriter salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
                do
                {
                    String rta = entrada.readLine();
                    for (Receptor r : receptores)
                    {
                        salida.println(r.toString());
                    }
                    salida.println("fin");
                    AgregarSecundario(new ServidorSecundario(socketCliente, salida));
                }
                while (ejecutando);
                entrada.close();
                //salida.close();
                //socketCliente.close();
            }
            catch (IOException e)
            {
            }
        }
    }
    //-------------------------------------------------------------------------------------------
    //synchronized metodos
    private synchronized void AgregarSecundario(ServidorSecundario secundario){
        secundarios.add(secundario);
    }
    private synchronized void EnviarReceptorNuevo(Receptor receptor)
    {
        for(ServidorSecundario s : secundarios)
        {
            s.EnviarReceptor(receptor);
        }
    }
    //-------------------------------------------------------------------------------------------
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
            CerrarServidor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------------
    //Estados del Servidor
    @Override
    public void runPrimario()
    {
        try
        {
            IniciarServidor();
            secundarios = new ArrayList<>();
            hiloPrimario = new Thread(new HiloPrimario());
            hiloPrimario.start();
            do
            {
                IniciarComponentesServidor();
                RegistroEvento evento;
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

                    evento = new RegistroEvento(socketCliente.getInetAddress().toString(),InformacionConfig.getInstance().getPuertoAlarma(),mensajeEmisor.getTipoEmergencia(),fecha);
                    notificarEvento(evento);
                }
                else if(mensaje[0].equals("1")) ///registro un receptor
                {
                    Receptor receptor;
                    String tipoSolicitudes = "";

                    mensajeReceptor = new MensajeReceptor(mensaje);
                    receptor = new Receptor(socketCliente.getInetAddress().toString().substring(1),Integer.parseInt(mensajeReceptor.getPuerto()),mensajeReceptor.getTipoEmergencias());
                    receptores.add(receptor);
                    EnviarReceptorNuevo(receptor);
                    StringBuilder sb = new StringBuilder();
                    for(String s : receptor.getTipoSolicitudes())
                    {
                        tipoSolicitudes = sb.append(s).toString();
                    }
                    evento = new RegistroEvento(socketCliente.getInetAddress().toString(),InformacionConfig.getInstance().getPuertoAlarma(),tipoSolicitudes,fecha);
                    notificarEvento(evento);
                }

                //CerrarComponentesServidor();
            }
            while(ejecutarHilo);
            CerrarServidor();
        }
        catch(Exception e)
        {
        }
    }
    @Override
    public void runSecundario(String ip, int puerto) {

        Socket socketCliente = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, puerto);
        boolean ejecucion = true;
        do {
            try {
                socketCliente.setSoTimeout(10000);
                socketCliente.connect(socketAddress, 1000);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);

                //Registrarse
                salida.println("0");
                //
                do {
                    String mensaje = entrada.readLine();
                    if (mensaje.equals("fin")) {
                        ejecucion = false;
                    }
                    else
                    {
                        String[] receptorMensaje = mensaje.split("#");
                        Receptor receptor = new Receptor(receptorMensaje[0],Integer.parseInt(receptorMensaje[1]),receptorMensaje[2]);
                        receptores.add(receptor);
                        notificarEvento(new RegistroEvento(receptor.getIP(),receptor.getPuerto().toString(),
                                receptor.getTipoSolicitudes().toString(),getFecha()));
                    }
                }
                while (ejecucion);

                salida.close();
                entrada.close();
                socketCliente.close();
            }
            catch (Exception e) {
                ejecucion = false;
            }
        }
        while (ejecucion);
    }
    @Override
    public void serPrimario() {
        servidorState = new PrimarioState(this);
        notificarRol(servidorState.getRol());
        Iniciar();
    }
    @Override
    public void serSecundario(String ip, int puerto) {
        servidorState = new SecundarioState(this, ip, puerto);
        notificarRol(servidorState.getRol());
        Iniciar();
    }
    //-------------------------------------------------------------------------------------------
    public void run() {
        do {
            servidorState.Run();
        }
        while (1 != 0);
    }

    private void IniciarServidor() throws IOException
    {
        socketServidor = new ServerSocket(Integer.parseInt(InformacionConfig.getInstance().getPuertoAlarma()));
    }
    private void IniciarComponentesServidor() throws IOException
    {
        socketCliente = socketServidor.accept();
        entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
    }
    private void CerrarServidor() throws IOException
    {
        socketServidor.close();
    }
    private void CerrarComponentesServidor() throws IOException
    {
        entrada.close();
        salida.close();
        socketCliente.close();
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

    private void notificarRol(String rol)
    {
        setChanged();
        observador.update(null, rol);
    }
    private void notificarEvento(RegistroEvento evento)
    {
        setChanged();
        observador.update(this, evento);
        //notifyObservers(evento);
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
