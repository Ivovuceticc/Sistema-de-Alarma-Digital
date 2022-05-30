package Controlador;

import Modelo.MensajeEmergencia;
import Modelo.ReceptorTCP;
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
    private ReceptorTCP receptor;

    private boolean receptorIniciado = false;
    public Controlador() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.vistaReceptor = new VentanaReceptor();
        this.vistaReceptor.addActionListener(this);
        this.vistaReceptor.addWindowListener(this);

        this.vistaAjustes = new VentanaAjustes();
        this.vistaAjustes.addActionListener(this);
        this.vistaAjustes.Mostrar(true);

        receptor = new ReceptorTCP(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Ajustes")){
            this.vistaAjustes.Mostrar(true);
        }
        else if(e.getActionCommand().equals("Conectar")){
            IniciarReceptor();
            this.vistaAjustes.Mostrar(false);
        }
    }
    private void IniciarReceptor()
    {
        if (!receptorIniciado)
        {
            receptorIniciado = true;
            receptor.setAceptaEM(vistaAjustes.getEmergenciaM());
            receptor.setAceptaI(vistaAjustes.getIncendio());
            receptor.setAceptaP(vistaAjustes.getPolicia());
            this.vistaAjustes.BloquearTipEmergencia();
            receptor.Iniciar();
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try {
            if (o == null)
            {
                String mensaje = (String)arg;
                if (mensaje.equals("enviandoRegistro"))
                {
                    this.vistaReceptor.enviandoRegistro();
                } else {
                    if (mensaje.equals("registrado"))
                    {
                        this.vistaReceptor.registroCompletado();
                    } else {
                        if (mensaje.equals("fallo"))
                        {
                            this.vistaReceptor.registroFallado();
                        }
                        else {
                            vistaReceptor.MostrarCartel((String)arg);
                            this.vistaAjustes.DesbloquearTipEmergencia();
                            receptorIniciado = false;
                            this.vistaAjustes.Mostrar(true);
                        }
                    }
                }
            }
            else
            {
                MensajeEmergencia mensaje = (MensajeEmergencia)arg;
                vistaReceptor.MostrarEmergencia(mensaje.getTipoEmergencia(),mensaje.getFecha(), mensaje.getUbicacion());
            }
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
