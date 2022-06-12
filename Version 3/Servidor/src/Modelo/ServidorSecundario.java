package Modelo;

import java.io.PrintWriter;
import java.net.Socket;

public class ServidorSecundario {
    private ICSocket socket;

    public ServidorSecundario(ICSocket socket)
    {
        this.socket = socket;
    }
    public void EnviarReceptor(Receptor receptor)
    {
        socket.EnviarString(receptor.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return socket.equals(obj);
    }
}
