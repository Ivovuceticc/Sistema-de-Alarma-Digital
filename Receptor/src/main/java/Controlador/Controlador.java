package Controlador;

import Modelo.MensajeEmergencia;
import Modelo.ServidorTCP;
import Vista.IVista;
import Vista.VentanaReceptor;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Controlador implements ActionListener, WindowListener, Observer {

    private IVista vista = null;
    private ServidorTCP receptor;

    public Controlador() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.vista = new VentanaReceptor();
        this.vista.addActionListener(this);
        this.vista.addWindowListener(this);

        receptor = new ServidorTCP(this);
        receptor.Iniciar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void update(Observable o, Object arg)
    {
        MensajeEmergencia mensaje = (MensajeEmergencia)arg;
        try {
            vista.MostrarEmergencia(mensaje.getTipoEmergencia(),mensaje.getFecha(), mensaje.getUbicacion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        receptor.Detener();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
