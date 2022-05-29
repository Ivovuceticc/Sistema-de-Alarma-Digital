package Vista;

import java.awt.event.ActionListener;

public interface IVista {
    String getTipoSolicitud();
    String getFecha();
    //String getUbicacion();
    void Confirmacion(String mensaje);
    void addActionListener(ActionListener listenner);
    void setDireccion(String direccion);
}
