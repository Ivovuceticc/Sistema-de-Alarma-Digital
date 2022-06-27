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
    private String MensajeEmisor = "recibido";
    private String MensajeEmisorF = "Solicitud invalida";
    private List<Receptor> receptores;
    private List<ServidorSecundario> secundarios;
    private State servidorState;
    private ArrayList<RegistroEvento> registroLog;

    private boolean conectado = false;
    private Observer observador;

    public ServidorTCP(Observer observador){
        //addObserver(observador);
        this.observador = observador;
        receptores = new ArrayList<>();

        hiloMonitor = new Thread(new HiloMonitor());
        hiloMonitor.start();

        registroLog = new ArrayList<RegistroEvento>();

        notificarRol("Ninguno");
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
                String mensaje = servidor.LeerString();
                if (mensaje.equals("0"))
                {
                    ServidorSecundario secundario = new ServidorSecundario(socket);
                    for (Receptor r : receptores)
                    {
                        secundario.EnviarReceptor(r);
                    }
                    for (RegistroEvento log : registroLog)
                    {
                        secundario.EnviarLog(log);
                    }
                    if (!SecundariosContenido(socket))
                    {
                        secundarios.add(secundario);
                    }
                }
            }
            while (ejecutando);
        }
    }
    //-------------------------------------------------------------------------------------------
    //synchronized metodos
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
    private synchronized void EnviarLogNuevo(RegistroEvento evento)
    {
        for(ServidorSecundario s : secundarios)
        {
            s.EnviarLog(evento);
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
                primario.EsperarClientes();

                String rta = primario.LeerString();
                String[] mensaje =  rta.split("#");

                switch (mensaje[0])
                {
                    case "0":  ///recibo emergencia de emisor
                    {
                        MensajeEmisor mensajeEmisor = new MensajeEmisor(mensaje);
                        if (receptorTipoEmergencia(mensajeEmisor.getTipoEmergencia()))
                        {
                            enviarEmergencia(mensajeEmisor.getTipoEmergencia(),getFecha(), mensajeEmisor.getUbicacion()); //envia Emergencia a receptor
                            primario.EnviarString(MensajeEmisor);
                        }
                        else
                        {
                            primario.EnviarString(MensajeEmisorF);
                        }
                        RegistroEvento evento = new RegistroEvento("Mensaje de Emisor", primario.getIP().substring(1),Integer.toString(InformacionConfig.getInstance().getPuertoAlarma()),mensajeEmisor.getTipoEmergencia(),getFecha());
                        notificarEvento(evento);
                        EnviarLogNuevo(evento);
                        break;
                    }
                    case "1": ///registro un receptor
                    {
                        MensajeReceptor mensajeReceptor = new MensajeReceptor(mensaje);
                        AgregarReceptor(primario.getIP().substring(1), Integer.parseInt(mensajeReceptor.getPuerto()), mensajeReceptor.getTipoEmergencias());

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
    void AgregarReceptor(String ip, int puerto, String solicitudes)
    {
        String tipoSolicitudes = "| ";
        RegistroEvento evento;
        Receptor receptor = new Receptor(ip, puerto, solicitudes);

        receptores.add(receptor);

        for(String s : receptor.getTipoSolicitudes())
        {
            tipoSolicitudes += s + " | ";
        }
        evento = new RegistroEvento("Nuevo receptor registrado", receptor.getIP(), receptor.getPuerto().toString(), tipoSolicitudes, getFecha());
        notificarEvento(evento);

        EnviarReceptorNuevo(receptor);
        EnviarLogNuevo(evento);
    }

    @Override
    public void runSecundario(String ip, int puerto) {
        boolean ejecucion = true;
        CSocket cliente = new CSocket();

        cliente.IniciarClient(ip, puerto);
        if (cliente.ConectarseServidor() != null)
        {
            cliente.EnviarString(conectado?"-":"0");
            conectado=true;
            do {
                String mensaje = cliente.LeerString();
                if (mensaje == null || mensaje.equals("fin")) {
                    ejecucion = false;
                }
                else
                {
                    String[] receptorMensaje = mensaje.split("#");
                    switch (receptorMensaje[0])
                    {
                        case "1":
                        {
                            receptores.add(new Receptor(receptorMensaje[1],Integer.parseInt(receptorMensaje[2]),receptorMensaje[3]));
                        }
                        break;
                        case "2":
                        {
                            RegistroEvento evento = new RegistroEvento(mensaje.substring(2));
                            registroLog.add(evento);
                            notificarEvento(evento);
                        }
                        break;
                    }
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
        servidorState = new SecundarioState(this, ip.substring(1), puerto+2);
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

    public void enviarEmergencia(String tipoSolicitud, String fecha, String ubicacion)
    {
        CSocket receptor = new CSocket();
        //RegistroEvento evento;

        for(Receptor r : receptores) {
            if(r.getTipoSolicitudes().stream().filter(s -> s.equalsIgnoreCase(tipoSolicitud)).count() > 0) {
                receptor.IniciarClient(r.getIP(), r.getPuerto());
                if (receptor.ConectarseServidor()!= null)
                {
                    receptor.ConectarseServidor();
                    //evento = new RegistroEvento(r.getIP(), Integer.toString(r.getPuerto()) ,tipoSolicitud,fecha);

                    //Manda la emergencia
                    receptor.EnviarString(tipoSolicitud + "#" + fecha + "#" + ubicacion);

                    //notificarEvento(evento);

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
        registroLog.add(evento);
        setChanged();
        observador.update(this, evento);
        //notifyObservers(evento);
    }
}
