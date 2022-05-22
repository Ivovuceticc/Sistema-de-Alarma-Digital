package Controlador;

import Modelo.MensajeEmergencia;
import Modelo.ServidorTCP;
import Vista.IVista;
import Vista.IVistaAjustes;
import Vista.VentanaAjustes;
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

    private IVista vistaReceptor = null;
    private IVistaAjustes vistaAjustes = null;
    private ServidorTCP receptor;

    public Controlador() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.vistaReceptor = new VentanaReceptor();
        this.vistaReceptor.addActionListener(this);
        this.vistaReceptor.addWindowListener(this);

        this.vistaAjustes = new VentanaAjustes();
        this.vistaAjustes.addActionListener(this);
        this.vistaAjustes.Mostrar(false);

        receptor = new ServidorTCP(this);
        receptor.Iniciar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Ajustes")){
            this.vistaAjustes.Mostrar(true);
        }
        else if(e.getActionCommand().equals("Aplicar")){
            receptor.setAceptaEM(vistaAjustes.getEmergenciaM());
            receptor.setAceptaI(vistaAjustes.getIncendio());
            receptor.setAceptaP(vistaAjustes.getPolicia());
            this.vistaAjustes.Mostrar(false);
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        MensajeEmergencia mensaje = (MensajeEmergencia)arg;
        try {
            vistaReceptor.MostrarEmergencia(mensaje.getTipoEmergencia(),mensaje.getFecha(), mensaje.getUbicacion());
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
