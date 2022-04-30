package Vista;

import java.awt.event.ActionListener;

public interface IVista {
    String getTipoSolicitud();
    String getHora();
    String getUbicacion();
    void addActionListener(ActionListener listenner);
}
