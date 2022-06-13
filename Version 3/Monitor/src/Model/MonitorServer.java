package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorServer {
    private List<Socket> backupServers = new ArrayList<>();
    private List<Server> connectedServers;
    private ReadConfig config;
    private Server primario;
    private Socket primary = null;

    public MonitorServer() throws IOException {
        config = ReadConfig.getInstance();
        monitoringServers();
    }

///avisa al primario su rol y a los secundarios la ip del primario
    public void enviarAvisoATodos() throws IOException {
        PrintWriter salida = new PrintWriter(primary.getOutputStream(), true);
        salida.println("primario");
        System.out.println("Se eligio un primario");
        for (Socket s : backupServers) {
                salida = new PrintWriter(s.getOutputStream(), true);
                salida.println("secundario#"+primary.getInetAddress()+"#"+primary.getPort());
        }
    }


    public void elegirPrimary() throws IOException {
        int i = 0;
        while (primario == null && i < backupServers.size()) {
           // System.out.println(backupServers.size());
            try {
                if (backupServers.get(i).isConnected()) {
                    primary = backupServers.get(i);
                    primario = new Server(primary.getPort(),primary.getInetAddress().toString());
                    backupServers.remove(primary);
                    enviarAvisoATodos(); ///avisa al server que cambie el rol
                }
            } catch (Exception e) {
                System.out.println(e.getClass());

            }
            i++;

        }
    }

    public void filtrarServers() throws IOException {

        Socket s;
        int i = 0;
        ///filtro desde el config y abro cada uno de los sockets
        do {
            backupServers = new ArrayList<>();
            connectedServers = new ArrayList<>();
            for (Server sv : config.getServers()) {
                try {
                    SocketAddress address = new InetSocketAddress(sv.getAddress(), sv.getPort());
                    s = new Socket();
                    s.setSoTimeout(500);
                    s.connect(address);
                    if (s.isConnected()) {
                        if (primary == null)
                        {
                         primary = s;
                         primario = sv;
                         enviarAvisoATodos();
                        }
                        else {
                            backupServers.add(s);
                        }
                        connectedServers.add(sv);
                    }
                } catch (Exception e) {
                    System.out.println("connection timed out..");
                }
            }
        }while(backupServers.size() == 0);
        while (primary == null) {
            elegirPrimary();
        }

    }

    public void refrescarServers()
    {
        Socket s;
        PrintWriter salida;
        for (Server sv : config.getServers()) {

            SocketAddress address = new InetSocketAddress(sv.getAddress(), sv.getPort());
            if (!connectedServers.contains(sv)) {
                try {
                s = new Socket();
                s.setSoTimeout(500);
                    s.connect(address);
                    if (s.isConnected()) {
                    /*     if (primario == null)
                         {
                             primary = s;
                             primario = sv;
                             enviarAvisoATodos();
                         }*/
                        /*                        else {*/
                        backupServers.add(s);
                        salida = new PrintWriter(s.getOutputStream(), true);
                        salida.println("secundario#" + primary.getInetAddress() + "#" + primary.getPort());
                        /*                         }*/
                        connectedServers.add(sv);
                    }
                } catch(Exception e){
                System.out.println("connection timed out..");
            }
        }
        }
    }


    public void monitoringServers() throws IOException {
        PrintWriter salida;
        BufferedReader entrada;
        filtrarServers();
        String msg;

        while (true) {

            ///ping primary
            try {
                salida = new PrintWriter(primary.getOutputStream(), true);
                salida.println("ping");
                //System.out.println("ping");
                entrada = new BufferedReader(new InputStreamReader(primary.getInputStream()));
                System.out.println(entrada.readLine());
            } catch (Exception e) {
                System.out.println("Fallo primario, cambiando a secundario..");
                connectedServers.remove(primario);
                primario = null;
                elegirPrimary();
                refrescarServers();
            }
            ///ping secundarios
            refrescarServers();
            for (Socket sv : backupServers) {
                try {
                    entrada = new BufferedReader(new InputStreamReader(sv.getInputStream()));
                    salida = new PrintWriter(sv.getOutputStream(), true);
                    salida.println("ping");
                   // System.out.println(entrada.readLine());
                } catch (Exception e) {
                    connectedServers.remove(connectedServers.stream().filter((Server s) -> s.getPort() == sv.getPort()).findFirst());
                     refrescarServers();
                }

            }

        }
    }


}
