package Model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * La classe AudioManager gestisce la riproduzione di file audio nel gioco Blackjack.
 * Supporta la riproduzione di suoni singoli, suoni in loop e la regolazione del volume.
 */
public class AudioManager
{
    private Clip clip; // Oggetto per la riproduzione del suono

    /**
     * Riproduce un suono dal file specificato.
     *
     * @param filePath Il percorso del file audio da riprodurre.
     */
    public void playSound(String filePath)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Ferma la riproduzione del suono attuale.
     */
    public void stopSound()
    {
        if (clip != null && clip.isRunning())
        {
            clip.stop();
        }
    }

    /**
     * Riproduce un suono in loop continuo dal file specificato.
     *
     * @param filePath Il percorso del file audio da riprodurre in loop.
     */
    public void loopSound(String filePath)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Imposta il volume dell'audio.
     *
     * @param volume Il livello del volume (es. -10.0 per abbassare, 0.0 per normale, +6.0 per aumentare).
     */
    public void setVolume(float volume)
    {
        if (clip != null)
        {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
        }
    }

    /**
     * Verifica se il suono è attualmente in riproduzione.
     *
     * @return {@code true} se il suono è in riproduzione, {@code false} altrimenti.
     */
    public boolean isPlaying()
    {
        return clip != null && clip.isRunning();
    }
}
