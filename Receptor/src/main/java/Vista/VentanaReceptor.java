package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class VentanaReceptor extends JFrame implements IVista {
    private JPanel contentPane;
    private JButton buttonSalir;
    private JTable table1;
    private JScrollPane scrollPane;

    public VentanaReceptor() {
        setTitle("Solicitud de emergencia");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        IniciarTabla();

        setTitle("Receptor");
        setSize(800,600);

        buttonSalir.setActionCommand("Salir");

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonSalir);

        buttonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
                //dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
                //dispose();
            }
        });
        this.setVisible(true);
    }

    private void IniciarTabla()
    {
        String[] columns = new String[] {"Tipo", "Ubicación", "fecha"};
        String[][] data = new String[][]{};

        DefaultTableModel model = new DefaultTableModel(data,columns);
        table1.setModel(model);
    }

    @Override
    public void MostrarEmergencia(String tipoEmergencia, String fecha, String ubicacion)
    {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addRow(new Object[]{tipoEmergencia, ubicacion, fecha});

        //table1.getColumn("Accion").setCellRenderer(new ButtonRenderer());
        //table1.getColumn("Accion").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    @Override
    public void addActionListener(ActionListener listenner) {
    }
}
