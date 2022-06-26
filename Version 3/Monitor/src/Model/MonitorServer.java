package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MonitorServer {
    private List<Server> connectedServers =new ArrayList<>();;
    private ReadConfig config;
    private Server primario;

    public MonitorServer() throws IOException {
        config = ReadConfig.getInstance();
        monitoringServers();
    }

///avisa al primario su rol y a los secundarios la ip del primario
    public void enviarAvisoATodos() throws IOException {
        PrintWriter salida = new PrintWriter(primario.getSocket().getOutputStream(), true);
        salida.println("primario");
        System.out.println("Se eligio un primario");
        for (Server s: connectedServers) {
               avisoSecundario(s.getSocket());
        }
    }
public void avisoSecundario(Socket s) throws IOException {
    PrintWriter salida;
    salida = new PrintWriter(s.getOutputStream(), true);
    salida.println("secundario#"+primario.getSocket().getInetAddress()+"#"+primario.getSocket().getPort());
}

    public void elegirPrimary() throws IOException {
        int i = 0;
           // System.out.println(backupServers.size());
            try {
                while (primario == null && i < connectedServers.size()) {
                        primario = connectedServers.get(i) ;
                        connectedServers.remove(primario);
                        enviarAvisoATodos();   ///avisa al server que cambie el rol
                    i++;
                }

                }
              catch (Exception e) {
                System.out.println(e.getClass());
            }
    }

    public void filtrarServers() throws IOException {

        Socket s;
        int i = 0;
        ///filtro desde el config y abro cada uno de los sockets
        do {
            for (Server sv : config.getServers()) {
                 if (!connectedServers.stream().anyMatch((Server serv) -> sv.getPort() == serv.getPort())) {
                     try {
                         if (sv != primario) {
                             SocketAddress address = new InetSocketAddress(sv.getAddress(), sv.getPort());
                             s = new Socket();
                             s.setSoTimeout(500);
                             s.connect(address);
                             sv.setSocket(s);
                             connectedServers.add(sv);
                             if (primario == null) {
                                 elegirPrimary();
                             } else {
                                 avisoSecundario(s);
                             }
                         }
                     }catch (Exception e) {
                         System.out.println("connection timed out..");
                     }
                 }
            }
        }while(connectedServers.size() == 0);

    }



    public void monitoringServers() throws IOException {
        PrintWriter salida;
        BufferedReader entrada;
        filtrarServers();
        String msg;

        while (true) {

            ///ping primary
            try {
                salida = new PrintWriter(primario.getSocket().getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(primario.getSocket().getInputStream()));
                salida.println("ping");
                //System.out.println("ping");
                entrada.readLine();
            } catch (Exception e) {
                System.out.println("Fallo primario, cambiando a secundario..");
                primario = null;
                filtrarServers();


            }
            ///ping secundarios
            for (int i = 0; i < connectedServers.size(); i++) {
                try {
                    System.out.println(connectedServers.size());
                    entrada = new BufferedReader(new InputStreamReader(connectedServers.get(i).getSocket().getInputStream()));
                    salida = new PrintWriter(connectedServers.get(i).getSocket().getOutputStream(), true);
                    salida.println("ping");
                    entrada.readLine();
                    // System.out.println(entrada.readLine());
                } catch (Exception e) {
                    connectedServers.remove(connectedServers.get(i));
                    filtrarServers();
                    System.out.println("Se perdio conexion");
                }

            }

        }
    }

}


