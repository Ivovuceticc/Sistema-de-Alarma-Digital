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
        //serPrimario();
        //serSecundario("192.168.0.14",1211);
        ///serPrimario();
//        receptores.add(new Receptor("192.111.11.1", 1234, "001"));
//        receptores.add(new Receptor("192.111.11.2", 1234, "001"));
//        receptores.add(new Receptor("192.111.11.2", 1234, "001"));
//        receptores.add(new Receptor("192.111.11.2", 1234, "001"));

    }
    //-------------------------------------------------------------------------------------------
    //HILOS
    //los dos
    private class HiloMonitor implements Runnable {
        private boolean ejecutando = true;
        public void run()
        {
            CSocket monitor = new CSocket();
            monitor.IniciarServer(InformacionConfig.getInstance().getPuertoMonitor());

            monitor.EsperarClientes();
            do {
                String rta = monitor.LeerString();
                if (rta != null)
                {
                    if (rta.equals("ping"))
                    {
                        monitor.EnviarString("echo");
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
                else {
                    monitor.EsperarClientes();
                }
            }
            while (ejecutando);
            monitor.CerrarServer();
        }
    }
    //Solo lo ejecuta el servidor primario
    private class HiloPrimario implements Runnable {
        private boolean ejecutando = true;
        public void run()
        {
            CSocket servidor = new CSocket();
            servidor.IniciarServer(InformacionConfig.getInstance().getPuertoServidor());
            do
            {
                ICSocket socket = servidor.EsperarClientes();
                if (!SecundariosContenido(socket))
                {
                    String rta = servidor.LeerString();
                    for (Receptor r : receptores)
                    {
                        servidor.EnviarString(r.toString());
                    }
                    AgregarSecundario(socket);
                }
            }
            while (ejecutando);
        }
    }
    //-------------------------------------------------------------------------------------------
    //synchronized metodos
    private synchronized void AgregarSecundario(ICSocket socket){
        secundarios.add(new ServidorSecundario(socket));
    }
    private synchronized boolean SecundariosContenido(ICSocket socket)
    {
        for (ServidorSecundario s : secundarios)
        {
            if (s.equals(socket))
            {
                return true;
            }
        }
        return false;
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
    }
    //-------------------------------------------------------------------------------------------
    //Estados del Servidor
    @Override
    public void runPrimario()
    {
        try
        {
            CSocket primario = new CSocket();
            primario.IniciarServer(InformacionConfig.getInstance().getPuertoAlarma());

            secundarios = new ArrayList<>();
            hiloPrimario = new Thread(new HiloPrimario());
            hiloPrimario.start();
            do
            {
                RegistroEvento evento;

                primario.EsperarClientes();

                String rta = primario.LeerString();
                String[] mensaje =  rta.split("#");

                switch (mensaje[0])
                {
                    case "0":  ///recibo emergencia de emisor
                    {
                        mensajeEmisor = new MensajeEmisor(mensaje);
                        if (receptorTipoEmergencia(mensajeEmisor.getTipoEmergencia())) {

                            enviarEmergencia(mensajeEmisor.getTipoEmergencia(),getFecha()); //envia Emergencia a receptor
                            primario.EnviarString(MensajeEmisor);
                        }
                        else
                        {
                            primario.EnviarString(MensajeEmisorF);
                        }

                        evento = new RegistroEvento(primario.getIP(),Integer.toString(InformacionConfig.getInstance().getPuertoAlarma()),mensajeEmisor.getTipoEmergencia(),getFecha());
                        notificarEvento(evento);
                        break;
                    }
                    case "1": ///registro un receptor
                    {
                        Receptor receptor;
                        String tipoSolicitudes = "";

                        mensajeReceptor = new MensajeReceptor(mensaje);
                        receptor = new Receptor(primario.getIP().substring(1),Integer.parseInt(mensajeReceptor.getPuerto()),mensajeReceptor.getTipoEmergencias());
                        receptores.add(receptor);
                        EnviarReceptorNuevo(receptor);
                        StringBuilder sb = new StringBuilder();
                        for(String s : receptor.getTipoSolicitudes())
                        {
                            tipoSolicitudes = sb.append(s).toString();
                        }
                        evento = new RegistroEvento(primario.getIP(),Integer.toString(InformacionConfig.getInstance().getPuertoAlarma()),tipoSolicitudes,getFecha());
                        notificarEvento(evento);
                        break;
                    }
                }

                //CerrarComponentesServidor();
                primario.CerrarClient();
            }
            while(ejecutarHilo);
        }
        catch(Exception e)
        {
        }
    }
    @Override
    public void runSecundario(String ip, int puerto) {
        boolean ejecucion = true;
        CSocket cliente = new CSocket();

        cliente.IniciarClient(ip, puerto);
        if (cliente.ConectarseServidor() != null)
        {
            cliente.EnviarString("0");
            do {
                String mensaje = cliente.LeerString();
                if (mensaje == null || mensaje.equals("fin")) {
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
        }
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
        while (ejecutarHilo);
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
        CSocket receptor = new CSocket();
        RegistroEvento evento;

        for(Receptor r : receptores) {
            if(r.getTipoSolicitudes().stream().filter(s -> s.equalsIgnoreCase(tipoSolicitud)).count() > 0) {
                receptor.IniciarClient(r.getIP(), r.getPuerto());
                if (receptor.ConectarseServidor()!= null)
                {
                    receptor.ConectarseServidor();
                    evento = new RegistroEvento(r.getIP(), Integer.toString(r.getPuerto()) ,tipoSolicitud,fecha);

                    //Manda la emergencia
                    receptor.EnviarString(tipoSolicitud + "#" + fecha + "#" + mensajeEmisor.getUbicacion());

                    notificarEvento(evento);

                    receptor.CerrarClient();
                }
                else
                {
                    System.out.println("Tiempo de espera agotado para conectar al host");
                }
            }
        }
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
