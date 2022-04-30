package Controlador;

import Modelo.EmisorTCP;
import Vista.IVista;
import Vista.VentanaEmisor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener {

    private IVista vista = null;

    private EmisorTCP emisor;

    public Controlador()
    {
        this.vista = new VentanaEmisor();
        this.vista.addActionListener(this);

        emisor = new EmisorTCP();

        emisor.EnviarEmergencia(vista.getTipoSolicitud(), vista.getHora(), vista.getUbicacion());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        emisor.EnviarEmergencia(vista.getTipoSolicitud(), vista.getHora(), vista.getUbicacion());
    }
}
