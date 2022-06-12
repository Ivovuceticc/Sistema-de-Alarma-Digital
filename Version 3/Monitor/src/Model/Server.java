package Model;

public class Server {
    private Integer port;
    private String address;

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
}
