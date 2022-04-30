package Vista;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface IVista {
    void MostrarEmergencia(String tipoEmergencia, String fecha, String ubicacion);
    void addActionListener(ActionListener listenner);
    void addWindowListener(WindowListener windowListener);
}
