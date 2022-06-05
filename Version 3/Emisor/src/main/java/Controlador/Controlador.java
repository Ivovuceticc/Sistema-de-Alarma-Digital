package Controlador;

import Modelo.EmisorTCP;
import Vista.IVista;
import Vista.VentanaEmisor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class Controlador implements ActionListener, Observer {
    private IVista vista = null;
    private EmisorTCP emisor;
    public Controlador()
    {
        this.vista = new VentanaEmisor();
        this.vista.addActionListener(this);

        emisor = new EmisorTCP(this);
        this.vista.setDireccion(emisor.getUbicacion());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Enviar"))
        {
            emisor.EnviarEmergencia(vista.getTipoSolicitud(), vista.getFecha());
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        String mensaje = (String)arg;
        if (mensaje.equals("enviandoEmergencia"))
        {
            this.vista.enviandoEmergencia();
        } else {
            if (mensaje.equals("emergenciaDisponible")) {
                this.vista.emergenciaDisponible();
            } else {
                this.vista.Confirmacion(mensaje);
            }
        }
    }
}
