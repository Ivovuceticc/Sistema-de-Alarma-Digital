package Controlador;

import Modelo.RegistroEvento;
import Modelo.ServidorTCP;
import Vista.IVistaServer;
import Vista.VentanaServidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class Controlador  implements ActionListener, Observer {

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

    //Le manda el String al log del server.
    @Override
    public void update(Observable o, Object arg) {
        this.vistaServer.agregaLogCentral((RegistroEvento) arg);
    }
}
