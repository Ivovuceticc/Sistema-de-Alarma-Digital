package Modelo;

public class SecundarioState extends State{
    private String ip;
    private int puerto;
    public SecundarioState(ServidorTCP servidor, String ip, int puerto) {
        super(servidor);
        this.ip = ip;
        this.puerto = puerto;
    }
    @Override
    public void Run() {
        servidor.runSecundario(this.ip, this.puerto);
    }

    @Override
    public String getRol() {
        return "Secundario";
    }
}
