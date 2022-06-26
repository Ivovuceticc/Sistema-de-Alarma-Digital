package View;



import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface IViewMonitor {
    void addActionListener(ActionListener listenner);
    void agregaNuevaConexion(String conexion);
    void addWindowListener(WindowListener windowListener);

}

