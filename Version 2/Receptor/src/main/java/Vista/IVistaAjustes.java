package Vista;

import java.awt.event.ActionListener;

public interface IVistaAjustes {
    void setSolicitudes(boolean emrgencica,boolean incendio, boolean policia);
    boolean getEmergenciaM();
    boolean getIncendio();
    boolean getPolicia();

    void Mostrar(boolean visible);
    void BloquearTipEmergencia();
    void DesbloquearTipEmergencia();
    void addActionListener(ActionListener listenner);
}
