package Vista;

import Modelo.RegistroEvento;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaServidor extends JFrame implements IVistaServer{
    private JScrollPane scrollPane_Server;
    private JTextArea textArea_Server;

    public VentanaServidor() {
        setTitle("Servidor");
        this.setSize(600,600);

        this.scrollPane_Server = new JScrollPane();
        this.scrollPane_Server.setBorder(new TitledBorder(null, "Eventos ", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        getContentPane().add(this.scrollPane_Server, BorderLayout.CENTER);

        this.textArea_Server = new JTextArea();
        this.textArea_Server.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        this.scrollPane_Server.setViewportView(this.textArea_Server);

        this.setVisible(true);
        this.setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
                //dispose();
            }
        });
    }

    public void agregaLogCentral(RegistroEvento e)
    {
        this.textArea_Server.append("IP:"+e.getIp()+"\t Puerto: "+e.getPuerto()+"\t Fecha:"+e.getFechaYHora()+"\t tipoSolicitud: "+e.getTiposDeSolicitud()+ "\n");
    }

    public void addActionListener(ActionListener listenner){
        this.addActionListener(listenner);
    }
    @Override
    public void setRol(String rol)
    {
        setTitle("Servidor - " + rol);
    }
}
