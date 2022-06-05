package Controlador;

import Modelo.RegistroEvento;
import Modelo.ServidorTCP;
import Vista.IVistaServer;
import Vista.VentanaServidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

public class Controlador  implements ActionListener, Observer, WindowListener {

    private IVistaServer vistaServer = null;
    private ServidorTCP servidorTCP;

    public Controlador(){
        this.vistaServer = new VentanaServidor();
        //this.vistaServer.addActionListener(this);
        this.servidorTCP = new ServidorTCP(this);
        servidorTCP.iniciar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        servidorTCP.Detener();
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

    //Le manda el String al log del server.
    @Override
    public void update(Observable o, Object arg) {
        this.vistaServer.agregaLogCentral((RegistroEvento) arg);
    }
}
