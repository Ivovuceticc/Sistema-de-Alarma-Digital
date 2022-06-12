package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorServer {
    private List<Socket> backupServers;
    private ReadConfig config;
    private Socket primary = null;

    public MonitorServer() throws IOException {
        config = ReadConfig.getInstance();
        monitoringServers();
    }

    public void enviarAviso() throws IOException {
        PrintWriter salida = new PrintWriter(primary.getOutputStream(), true);
        salida.println("Primario");
    }


    public void elegirPrimary() throws IOException {
        int i = 0;
        Socket monitorConn;
        while (primary == null && i < backupServers.size()) {
            try {
//                monitorConn = new Socket();
//                SocketAddress address = new InetSocketAddress(servers.get(i).getAddress(), servers.get(i).getPort());
//                monitorConn.connect(address);
                if (backupServers.get(i).isConnected()) {
                    primary = backupServers.get(i);
                    backupServers.remove(primary);
                    enviarAviso(); ///avisa al server que cambie el rol
                }
            } catch (java.net.SocketTimeoutException e) {
                System.out.println("Server timeout.. buscando siguiente");

            }
            i++;

        }
    }

    public void filtrarServers() throws IOException {

        int i = 0;
        for (Server sv : config.getServers()) {
            SocketAddress address = new InetSocketAddress(sv.getAddress(), sv.getPort());
            backupServers.add(new Socket());
            backupServers.get(i).connect(address);
            backupServers.get(i).setSoTimeout(1000);
        }
        while (primary == null) {
            elegirPrimary();
        }

    }


    public void monitoringServers() throws IOException {
        Socket socketPrimary = new Socket();
        PrintWriter salida;
        BufferedReader entrada;
        filtrarServers();
        String msg;

        while (true) {

            ///ping primary
            try {
                salida = new PrintWriter(primary.getOutputStream(), true);
                salida.println("ping");
                entrada = new BufferedReader(new InputStreamReader(primary.getInputStream()));
                msg = entrada.readLine();
            } catch (java.net.SocketTimeoutException e) {
                primary.close();
                primary = null;
                filtrarServers();
            }
            for (Socket sv : backupServers) {
                try {
                    entrada = new BufferedReader(new InputStreamReader(sv.getInputStream()));
                    salida = new PrintWriter(sv.getOutputStream(), true);
                    salida.println("ping");
                    System.out.println(entrada.readLine());
                } catch (java.net.SocketTimeoutException e) {
                    backupServers.remove(sv);
                }

            }

        }
    }


}
