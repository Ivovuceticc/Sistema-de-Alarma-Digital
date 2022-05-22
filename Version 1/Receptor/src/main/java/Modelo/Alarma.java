package Modelo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Alarma {
    Clip sirena;
    AudioInputStream audioInputStream;

    public Alarma(){

    }

    void abrirAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File("alarma.wav").getAbsoluteFile());
        sirena = AudioSystem.getClip();
    }

    public void dispararAlarma() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        abrirAudio();
        sirena.open(audioInputStream);
        sirena.loop(Clip.LOOP_CONTINUOUSLY);
        sirena.start();
    }

    public void frenarAlarma()
    {
        sirena.stop();
        sirena.close();
    }
}
