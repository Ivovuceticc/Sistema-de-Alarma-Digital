package Vista;

import java.awt.event.ActionListener;

public interface IVista {
    String getTipoSolicitud();
    String getFecha();
    String getUbicacion();
    void Confirmacion();
    void addActionListener(ActionListener listenner);
}
