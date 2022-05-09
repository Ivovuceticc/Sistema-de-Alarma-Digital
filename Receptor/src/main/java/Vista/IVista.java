package Vista;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.IOException;

public interface IVista {
    void MostrarEmergencia(String tipoEmergencia, String fecha, String ubicacion) throws IOException, LineUnavailableException, UnsupportedAudioFileException;
    void addActionListener(ActionListener listenner);
    void addWindowListener(WindowListener windowListener);
}
