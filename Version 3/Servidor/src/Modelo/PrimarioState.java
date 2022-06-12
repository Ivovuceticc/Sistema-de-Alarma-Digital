package Modelo;

public class PrimarioState extends State{

    public PrimarioState(ServidorTCP servidor) {
        super(servidor);
    }
    @Override
    public void Run() {
        servidor.runPrimario();
    }
    @Override
    public String getRol()
    {
        return "Primario";
    }
}
