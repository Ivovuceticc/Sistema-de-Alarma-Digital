package Modelo;

public interface IServidorState {
    void runPrimario();
    void runSecundario(String ip, int puerto);
    void serPrimario();
    void serSecundario(String ip, int puerto);
}
