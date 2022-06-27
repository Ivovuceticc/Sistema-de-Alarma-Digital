package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class MonitorServer extends Observable {
    private List<Server> connectedServers =new ArrayList<>();;
    private ReadConfig config;
    private Server primario;
    private Observer observer;

    public MonitorServer(Observer observer) throws IOException {
        config = ReadConfig.getInstance();
        this.observer = observer;
        monitoringServers();
    }

    private void nuevaConexion(String conexion)
    {
        setChanged();
        observer.update(null,conexion);
    }

///avisa al primario su rol y a los secundarios la ip del primario
    public void enviarAvisoATodos() throws IOException {
        PrintWriter salida = new PrintWriter(primario.getSocket().getOutputStream(), true);
        salida.println("primario");
        nuevaConexion("Se eligio un nuevo primario: "+"Direccion: "+ primario.getAddress()+" Puerto: "+primario.getPort());
        for (Server s: connectedServers) {
               avisoSecundario(s.getSocket());
        }
    }
public void avisoSecundario(Socket s) throws IOException {
    PrintWriter salida;
    salida = new PrintWriter(s.getOutputStream(), true);
    nuevaConexion("Rol Secundario para: "+s.getInetAddress()+"Puerto: "+s.getPort());
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
            for (Server sv : config.getServers()) {
                if (connectedServers.stream().noneMatch((Server serv) -> sv.getPort() == serv.getPort())) {

                    try {
                        if (sv != primario) {
                            SocketAddress address = new InetSocketAddress(sv.getAddress(), sv.getPort());
                            s = new Socket();
                            s.setSoTimeout(50);
                            s.connect(address);
                            sv.setSocket(s);
                            System.out.println("Se agreego un server");
                            connectedServers.add(sv);
                            if (primario == null) {
                                elegirPrimary();
                            } else {
                                avisoSecundario(s);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("connection timed out..");
                    }
                }
            }

    }



    public void monitoringServers() throws IOException {
        PrintWriter salida;
        BufferedReader entrada;
        filtrarServers();
        Server s=null;
        String msg;

        while (true) {

            ///ping primary
            filtrarServers();
            try {
                salida = new PrintWriter(primario.getSocket().getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(primario.getSocket().getInputStream()));
                salida.println("ping");
                //System.out.println("ping");
                entrada.readLine();
            } catch (Exception e) {
                nuevaConexion("Fallo conexion con el primario, buscando...");
                System.out.println("Fallo primario, cambiando a secundario..");
                primario = null;
                elegirPrimary();
                filtrarServers();


            }
            ///ping secundarios
            try {

            for (int i = 0; i < connectedServers.size(); i++) {
                    s = connectedServers.get(i);
                    entrada = new BufferedReader(new InputStreamReader(connectedServers.get(i).getSocket().getInputStream()));
                    salida = new PrintWriter(connectedServers.get(i).getSocket().getOutputStream(), true);
                    salida.println("ping");
                    entrada.readLine();
                    // System.out.println(entrada.readLine());
                }

            }
            catch (Exception e) {
                connectedServers.remove(s);
                nuevaConexion("Se perdio conexion con server secundario, buscando..");
                System.out.println("Se perdio conexion con server secundario, buscando..");
                filtrarServers();

            }

        }
    }

}


