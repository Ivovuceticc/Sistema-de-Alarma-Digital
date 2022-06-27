package Model;

import java.net.Socket;

public class Server {
    private Integer port;
    private String address;
    private Socket socket;

    public Server(Integer port, String address) {
        this.port = port;
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket s) {
        this.socket = s;
    }
}
