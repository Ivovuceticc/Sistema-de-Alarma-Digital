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
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Enviar"))
        {
            emisor.EnviarEmergencia(vista.getTipoSolicitud(), vista.getFecha(), vista.getUbicacion());
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        String mensaje = (String)arg;
        if (mensaje.equals("recibido"))
        {
            this.vista.Confirmacion();
        }
    }
}
