package Modelo;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class CSoket {

    private ServerSocket socketServidor = null;
    private Socket socketCliente = null;
    private BufferedReader entrada = null;
    private PrintWriter salida = null;

    public void IniciarServer()
    {
        try {
            socketServidor = new ServerSocket(Integer.parseInt(InformacionConfig.getInstance().getPuertoServidor()));
            socketCliente = socketServidor.accept();
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void IniciarClient(String ip, int puerto)
    {
        try {
            socketCliente = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, puerto);
            socketCliente.setSoTimeout(10000);
            socketCliente.connect(socketAddress, 1000);
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(socketCliente.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String LeerString()
    {
        String mensaje = null;
        try {
            mensaje = entrada.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mensaje;
    }
    public void EnviarString(String mensaje)
    {
        salida.println(mensaje);
    }

    public void CerrarServer()
    {
        try {
            socketServidor.close();
            socketCliente.close();
            entrada.close();
            salida.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void CerrarClient()
    {
        try {
            socketCliente.close();
            entrada.close();
            salida.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
