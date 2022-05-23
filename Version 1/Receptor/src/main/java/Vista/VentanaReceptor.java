package Vista;

import Modelo.Alarma;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;

public class VentanaReceptor extends JFrame implements IVista {
    private JPanel contentPane;
    private JButton buttonSalir;
    private JTable table1;
    private JButton buttonAjustes;
    private JScrollPane scrollPane;
    private Alarma alarma;

    public VentanaReceptor() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setTitle("Solicitud de emergencia");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(true);

        alarma = new Alarma();

        IniciarTabla();

        setTitle("Receptor");
        setSize(800,600);

        buttonSalir.setActionCommand("Salir");
        buttonAjustes.setActionCommand("Ajustes");

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
    public void MostrarEmergencia(String tipoEmergencia, String fecha, String ubicacion) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addRow(new Object[]{tipoEmergencia, ubicacion, fecha});
        nuevaAlerta(ubicacion);


        //table1.getColumn("Accion").setCellRenderer(new ButtonRenderer());
        //table1.getColumn("Accion").setCellEditor(new ButtonEditor(new JCheckBox()));
    }


    public void nuevaAlerta(String mensaje) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        alarma.dispararAlarma();
        int optionType = JOptionPane.OK_OPTION;
        JOptionPane.showMessageDialog(null,"Alerta de nueva emergencia en "+mensaje,"Nueva Emeergencia",JOptionPane.WARNING_MESSAGE);
            alarma.frenarAlarma();


    }

    @Override
    public void addActionListener(ActionListener listenner) {
        this.buttonAjustes.addActionListener(listenner);
    }
}
