package Vista;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.awt.Point;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaEmisor extends JFrame implements IVista
{

    private PanelConImagen contentPane;
    private JPanel panel_ConexionInternet;
    private JPanel panel_Enviar;
    private JPanel panel_TipoSolicitud;
    private JPanel panel_Izq_conexion;
    private JPanel panel_Izquierdo;
    private JPanel panel_Derecho;
    private JPanel panel_Centro;
    private JCheckBox chckbxNewCheckBox;
    private JPanel panel_LabelSolicitud;
    private JPanel panel_EmergenciaM;
    private JPanel panel;
    private JPanel panel_4;
    private JPanel panelC;
    private JPanel panel_1;
    private JPanel panel_5;
    private JPanel panel_CE;
    private JPanel panel_7;
    private JPanel panel_8;
    private JRadioButton rdbtnEmergenciaM;
    private JPanel panel_Incendio;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_CI;
    private JPanel panel_6;
    private JPanel panel_9;
    private JRadioButton rdbtnIncendio;
    private JPanel panel_policia;
    private JPanel panel_10;
    private JPanel panel_11;
    private JPanel panel_CP;
    private JPanel panel_12;
    private JPanel panel_13;
    private JRadioButton rdbtnPolicia;
    private JPanel panel_14;
    private JPanel panel_DerechoEnviar;
    private JPanel panel_IzqEnviar;
    private JPanel panel_DerEnviar;
    private JPanel panel_17;
    private JPanel panel_18;
    private JButton btn_Enviar;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_Direccion;
    private final ButtonGroup buttonGroup = new ButtonGroup();

    /**
     * Create the frame.
     */
    public VentanaEmisor()
    {
        setTitle("Solicitud de emergencia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 693, 284);
        this.contentPane = new PanelConImagen();
        this.contentPane.setOpaque(false);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setFondo(new ImageIcon("../Imagenes/Fondo2.png"));
        //this.contentPane.setFondo(new ImageIcon("Fondo2.png"));//"..//Imagenes//Fondo2.png"));
        setContentPane(this.contentPane);
        this.contentPane.setLayout(new GridLayout(3, 0, 0, 0));

        //CONEXION INTERNET
        this.panel_ConexionInternet = new JPanel();
        this.panel_ConexionInternet.setOpaque(false);
        this.panel_ConexionInternet.setBackground(Color.WHITE);
        this.panel_ConexionInternet.setFont(new Font("Tahoma", Font.PLAIN, 11));
        this.panel_ConexionInternet.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        this.contentPane.add(this.panel_ConexionInternet);
        this.panel_ConexionInternet.setLayout(new GridLayout(0, 2, 0, 0));
        this.panel_ConexionInternet.setVisible(true);

        this.lblNewLabel_Direccion = new JLabel("Direccion:");
        this.lblNewLabel_Direccion.setFont(new Font("Tahoma", Font.PLAIN, 18));
        this.panel_ConexionInternet.add(this.lblNewLabel_Direccion, BorderLayout.CENTER);

        this.panel_Izq_conexion = new JPanel();
        this.panel_Izq_conexion.setOpaque(false);
        this.panel_Izq_conexion.setBackground(Color.WHITE);
        this.panel_ConexionInternet.add(this.panel_Izq_conexion);
        this.panel_Izq_conexion.setLayout(new BorderLayout(0, 0));

        this.panel_Izquierdo = new JPanel();
        this.panel_Izquierdo.setOpaque(false);
        this.panel_Izquierdo.setBackground(Color.WHITE);
        this.panel_Izq_conexion.add(this.panel_Izquierdo, BorderLayout.WEST);

        this.panel_Derecho = new JPanel();
        this.panel_Derecho.setOpaque(false);
        this.panel_Derecho.setBackground(Color.WHITE);
        this.panel_Izq_conexion.add(this.panel_Derecho, BorderLayout.EAST);
        this.panel_Derecho.setLayout(new BorderLayout(0, 0));

        this.panel_Centro = new JPanel();
        this.panel_Centro.setOpaque(false);
        this.panel_Centro.setBackground(Color.WHITE);
        this.panel_Izq_conexion.add(this.panel_Centro, BorderLayout.CENTER);
        this.panel_Centro.setLayout(new BorderLayout(0, 0));

        this.panel_TipoSolicitud = new JPanel();
        this.panel_TipoSolicitud.setOpaque(false);
        this.panel_TipoSolicitud.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        this.contentPane.add(this.panel_TipoSolicitud);
        this.panel_TipoSolicitud.setLayout(new GridLayout(0, 4, 0, 0));

        this.panel_LabelSolicitud = new JPanel();
        this.panel_LabelSolicitud.setOpaque(false);
        this.panel_LabelSolicitud.setBackground(Color.WHITE);
        this.panel_TipoSolicitud.add(this.panel_LabelSolicitud);
        this.panel_LabelSolicitud.setLayout(new BorderLayout(0, 0));

        this.panel = new JPanel();
        this.panel.setOpaque(false);
        this.panel.setBackground(Color.WHITE);
        this.panel_LabelSolicitud.add(this.panel, BorderLayout.WEST);

        this.panel_4 = new JPanel();
        this.panel_4.setOpaque(false);
        this.panel_4.setBackground(Color.WHITE);
        this.panel_LabelSolicitud.add(this.panel_4, BorderLayout.EAST);

        this.panelC = new JPanel();
        this.panelC.setOpaque(false);
        this.panelC.setBackground(Color.WHITE);
        this.panel_LabelSolicitud.add(this.panelC, BorderLayout.CENTER);
        this.panelC.setLayout(new BorderLayout(0, 0));

        this.lblNewLabel_1 = new JLabel("Seleccione tipo de solicitud:");
        this.panelC.add(this.lblNewLabel_1, BorderLayout.CENTER);

        this.panel_EmergenciaM = new JPanel();
        this.panel_EmergenciaM.setOpaque(false);
        this.panel_EmergenciaM.setBackground(Color.WHITE);
        this.panel_TipoSolicitud.add(this.panel_EmergenciaM);
        this.panel_EmergenciaM.setLayout(new BorderLayout(0, 0));

        this.panel_1 = new JPanel();
        this.panel_1.setOpaque(false);
        this.panel_1.setBackground(Color.WHITE);
        this.panel_EmergenciaM.add(this.panel_1, BorderLayout.WEST);

        this.panel_5 = new JPanel();
        this.panel_5.setOpaque(false);
        this.panel_5.setBackground(Color.WHITE);
        this.panel_EmergenciaM.add(this.panel_5, BorderLayout.EAST);

        this.panel_CE = new JPanel();
        this.panel_CE.setOpaque(false);
        this.panel_EmergenciaM.add(this.panel_CE, BorderLayout.CENTER);
        this.panel_CE.setLayout(new BorderLayout(0, 0));

        this.panel_7 = new JPanel();
        this.panel_7.setOpaque(false);
        this.panel_7.setBackground(Color.WHITE);
        this.panel_CE.add(this.panel_7, BorderLayout.NORTH);

        this.panel_8 = new JPanel();
        this.panel_8.setOpaque(false);
        this.panel_8.setBackground(Color.WHITE);
        this.panel_CE.add(this.panel_8, BorderLayout.SOUTH);

        this.rdbtnEmergenciaM = new JRadioButton("Emergencia Medica");
        buttonGroup.add(this.rdbtnEmergenciaM);
        this.rdbtnEmergenciaM.setOpaque(false);
        this.rdbtnEmergenciaM.setBackground(Color.WHITE);
        this.panel_CE.add(this.rdbtnEmergenciaM, BorderLayout.CENTER);

        this.panel_Incendio = new JPanel();
        this.panel_Incendio.setOpaque(false);
        this.panel_TipoSolicitud.add(this.panel_Incendio);
        this.panel_Incendio.setLayout(new BorderLayout(0, 0));

        this.panel_2 = new JPanel();
        this.panel_2.setOpaque(false);
        this.panel_2.setBackground(Color.WHITE);
        this.panel_Incendio.add(this.panel_2, BorderLayout.WEST);

        this.panel_3 = new JPanel();
        this.panel_3.setOpaque(false);
        this.panel_3.setBackground(Color.WHITE);
        this.panel_Incendio.add(this.panel_3, BorderLayout.EAST);

        this.panel_CI = new JPanel();
        this.panel_CI.setOpaque(false);
        this.panel_CI.setBackground(Color.WHITE);
        this.panel_Incendio.add(this.panel_CI, BorderLayout.CENTER);
        this.panel_CI.setLayout(new BorderLayout(0, 0));

        this.panel_6 = new JPanel();
        this.panel_6.setOpaque(false);
        this.panel_6.setBackground(Color.WHITE);
        this.panel_CI.add(this.panel_6, BorderLayout.NORTH);

        this.panel_9 = new JPanel();
        this.panel_9.setOpaque(false);
        this.panel_9.setBackground(Color.WHITE);
        this.panel_CI.add(this.panel_9, BorderLayout.SOUTH);

        this.rdbtnIncendio = new JRadioButton("Incendio");
        buttonGroup.add(this.rdbtnIncendio);
        this.rdbtnIncendio.setOpaque(false);
        this.rdbtnIncendio.setBackground(Color.WHITE);
        this.panel_CI.add(this.rdbtnIncendio, BorderLayout.CENTER);

        this.panel_policia = new JPanel();
        this.panel_policia.setOpaque(false);
        this.panel_policia.setBackground(Color.WHITE);
        this.panel_TipoSolicitud.add(this.panel_policia);
        this.panel_policia.setLayout(new BorderLayout(0, 0));

        this.panel_10 = new JPanel();
        this.panel_10.setOpaque(false);
        this.panel_10.setBackground(Color.WHITE);
        this.panel_policia.add(this.panel_10, BorderLayout.WEST);

        this.panel_11 = new JPanel();
        this.panel_11.setOpaque(false);
        this.panel_11.setBackground(Color.WHITE);
        this.panel_policia.add(this.panel_11, BorderLayout.EAST);

        this.panel_CP = new JPanel();
        this.panel_CP.setOpaque(false);
        this.panel_policia.add(this.panel_CP, BorderLayout.CENTER);
        this.panel_CP.setLayout(new BorderLayout(0, 0));

        this.panel_12 = new JPanel();
        this.panel_12.setOpaque(false);
        this.panel_12.setBackground(Color.WHITE);
        this.panel_CP.add(this.panel_12, BorderLayout.SOUTH);

        this.panel_13 = new JPanel();
        this.panel_13.setOpaque(false);
        this.panel_13.setBackground(Color.WHITE);
        this.panel_CP.add(this.panel_13, BorderLayout.NORTH);

        this.rdbtnPolicia = new JRadioButton("Policia");
        buttonGroup.add(this.rdbtnPolicia);
        this.rdbtnPolicia.setOpaque(false);
        this.rdbtnPolicia.setBackground(Color.WHITE);
        this.panel_CP.add(this.rdbtnPolicia, BorderLayout.CENTER);

        this.panel_Enviar = new JPanel();
        this.panel_Enviar.setOpaque(false);
        this.panel_Enviar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        this.contentPane.add(this.panel_Enviar);
        this.panel_Enviar.setLayout(new GridLayout(0, 2, 0, 0));

        this.panel_14 = new JPanel();
        this.panel_14.setOpaque(false);
        this.panel_14.setBackground(Color.WHITE);
        this.panel_Enviar.add(this.panel_14);

        this.panel_DerechoEnviar = new JPanel();
        this.panel_DerechoEnviar.setOpaque(false);
        this.panel_DerechoEnviar.setBackground(Color.WHITE);
        this.panel_Enviar.add(this.panel_DerechoEnviar);
        this.panel_DerechoEnviar.setLayout(new BorderLayout(0, 0));

        this.panel_IzqEnviar = new JPanel();
        this.panel_IzqEnviar.setOpaque(false);
        this.panel_IzqEnviar.setBackground(Color.WHITE);
        this.panel_IzqEnviar.setPreferredSize(new Dimension(100, 10));
        this.panel_DerechoEnviar.add(this.panel_IzqEnviar, BorderLayout.WEST);

        this.panel_DerEnviar = new JPanel();
        this.panel_DerEnviar.setOpaque(false);
        this.panel_DerEnviar.setBackground(Color.WHITE);
        this.panel_DerEnviar.setPreferredSize(new Dimension(100, 10));
        this.panel_DerechoEnviar.add(this.panel_DerEnviar, BorderLayout.EAST);

        this.panel_17 = new JPanel();
        this.panel_17.setOpaque(false);
        this.panel_17.setBackground(Color.WHITE);
        this.panel_DerechoEnviar.add(this.panel_17, BorderLayout.SOUTH);

        this.panel_18 = new JPanel();
        this.panel_18.setOpaque(false);
        this.panel_18.setBackground(Color.WHITE);
        this.panel_DerechoEnviar.add(this.panel_18, BorderLayout.NORTH);

        this.btn_Enviar = new JButton("Enviar");
        this.btn_Enviar.setOpaque(false);
        this.btn_Enviar.setForeground(Color.BLACK);
        this.btn_Enviar.setBackground(new Color(23,12,32));
        this.btn_Enviar.setActionCommand("Enviar");
        this.panel_DerechoEnviar.add(this.btn_Enviar, BorderLayout.CENTER);

        rdbtnEmergenciaM.setSelected(true);

        this.setVisible(true);
    }
    @Override
    public String getTipoSolicitud() {
        String solicitud;
        if (rdbtnPolicia.isSelected())
        {
            solicitud = "policia";
        }
        else {
            if (rdbtnEmergenciaM.isSelected())
            {
                solicitud = "emergencia";
            }
            else
            {
                solicitud = "incendio";
            }
        }
        return solicitud;
    }
    @Override
    public String getFecha() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
//    @Override
//    public String getUbicacion() {
//        return "independencia";
//    }
    @Override
    public void addActionListener(ActionListener listenner) {
        btn_Enviar.addActionListener(listenner);
    }
    @Override
    public void Confirmacion(String mensaje)
    {
        JOptionPane.showMessageDialog(null,mensaje);
    }

    @Override
    public void setDireccion(String direccion)
    {
        lblNewLabel_Direccion.setText("Direccion: "+direccion);
    }
}
