package Vista;

import Modelo.RegistroEvento;

import java.awt.event.ActionListener;

public interface IVistaServer {
    void addActionListener(ActionListener listenner);
    void agregaLogCentral(RegistroEvento e);
}

