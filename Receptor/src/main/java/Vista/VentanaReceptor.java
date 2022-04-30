package Vista;

import Vista.Botones.ButtonEditor;
import Vista.Botones.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class VentanaReceptor extends JDialog implements IVista {
    private JPanel contentPane;
    private JButton buttonSalir;
    private JTable table1;
    private JScrollPane scrollPane;

    public VentanaReceptor() {
        this.pack();
        this.setVisible(true);
        //System.exit(0);

        IniciarTabla();

        setTitle("Receptor");
        setSize(800,600);

        buttonSalir.setActionCommand("Salir");

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSalir);

        buttonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
                //dispose();
            }
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
                //dispose();
            }
        });
    }

    private void IniciarTabla()
    {
        String[] columns = new String[] {"Tipo", "Ubicación", "Hora"};
        String[][] data = new String[][]{};

        DefaultTableModel model = new DefaultTableModel(data,columns);
        table1.setModel(model);
    }

    @Override
    public void MostrarEmergencia(String tipoEmergencia, String hora, String ubicacion)
    {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addRow(new Object[]{tipoEmergencia, ubicacion, hora});

        //table1.getColumn("Accion").setCellRenderer(new ButtonRenderer());
        //table1.getColumn("Accion").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    @Override
    public void addActionListener(ActionListener listenner) {
    }
}
