package Vista;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class VentanaAjustes extends JFrame{

    private JPanel contentPane;
    private JPanel PaneInformacion;
    private JButton buttonAplicar;
    private JRadioButton RdbuttonEmergenciaM;
    private JRadioButton RdbuttonIncendio;
    private JRadioButton RdbuttonPolicia;
    private JButton buttonCancelar;

    public VentanaAjustes(){
        this.setResizable(false);
        this.setTitle("Ajustes");
        setSize(500,300);
        this.setContentPane(contentPane);

        this.buttonAplicar.setActionCommand("Aplicar");
        this.buttonCancelar.setActionCommand("Cancelar");
        this.setVisible(true);
    }


    public void addActionListener(ActionListener listenner) {
        buttonAplicar.addActionListener(listenner);
        buttonCancelar.addActionListener(listenner);
    }

    public boolean getEmergenciaM(){
        return RdbuttonEmergenciaM.isSelected();
    }

    public boolean getIncendio(){
        return RdbuttonIncendio.isSelected();
    }

    public boolean getPolicia(){
        return RdbuttonPolicia.isSelected();
    }
}
