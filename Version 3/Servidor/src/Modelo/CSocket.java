package Modelo;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class CSocket implements ICSocket {

    private ServerSocket socketServidor = null;
    private Socket socketCliente = null;
    SocketAddress socketAddress = null;
    private BufferedReader entrada = null;
    private PrintWriter salida = null;

    public void IniciarServer(int puerto)
    {
        try {
            socketServidor = new ServerSocket(puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ICSocket EsperarClientes()
    {
        socketCliente = null;
        try {
            socketCliente = socketServidor.accept();
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
        } catch (IOException e) {
            return null;
        }
        return this;
    }
    public void IniciarClient(String ip, int puerto)
    {
        try {
            socketCliente = new Socket();
            socketAddress = new InetSocketAddress(ip, puerto);
            socketCliente.setSoTimeout(10000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ICSocket ConectarseServidor()
    {
        try {
            socketCliente.connect(socketAddress, 1000);
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(socketCliente.getOutputStream(), true);
        } catch (IOException e) {
            return null;
        }
        return this;
    }

    @Override
    public String LeerString()
    {
        String mensaje = null;
        try {
            mensaje = entrada.readLine();
        } catch (IOException e) {
        }
        return mensaje;
    }
    @Override
    public void EnviarString(String mensaje)
    {
        salida.println(mensaje);
    }

    public String getIP()
    {
        return socketCliente.getInetAddress().toString();
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
