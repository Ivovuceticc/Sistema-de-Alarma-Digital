package Vista;

import sun.java2d.Disposer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.*;

public class VentanaEmisor extends JDialog implements IVista {
    @Override
    public String getTipoSolicitud() {
        return null;
    }

    @Override
    public String getHora() {
        return null;
    }

    @Override
    public String getUbicacion() {
        return null;
    }

    @Override
    public void addActionListener(ActionListener listenner) {

    }

    public VentanaEmisor()
    {
        this.pack();
        this.setVisible(true);
        //System.exit(0);

        setTitle("Emisor");
        setSize(800,600);

        //setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
