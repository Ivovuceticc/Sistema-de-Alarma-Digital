package Vista;

import Modelo.RegistroEvento;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface IVistaServer {
    void addActionListener(ActionListener listenner);
    void agregaLogCentral(RegistroEvento e);
    void addWindowListener(WindowListener windowListener);
    void setRol(String rol);
}

