package Modelo;

public abstract class State {
    protected IServidorState servidor;

    public State(IServidorState servidor)
    {
        this.servidor = servidor;
    }
    public abstract void Run();
    public abstract String getRol();
}