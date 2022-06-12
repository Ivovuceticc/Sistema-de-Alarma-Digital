package Modelo;

import java.io.PrintWriter;
import java.net.Socket;

public class ServidorSecundario {
    private Socket cliente;
    private PrintWriter salida;

    public ServidorSecundario(Socket cliente, PrintWriter salida)
    {
        this.cliente = cliente;
        this.salida = salida;
    }
    public void EnviarReceptor(Receptor receptor)
    {
        salida.println(receptor);
    }
}
