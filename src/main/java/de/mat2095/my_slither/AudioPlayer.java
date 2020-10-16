package de.mat2095.my_slither;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Adapted from code copied from the author below.
 *
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 *
 */
public class AudioPlayer implements LineListener, Runnable {

    private boolean playCompleted;
    private Clip audioClip;
    private AudioInputStream audioFile;

    public AudioPlayer(String path) {
        try {
            //open audio file - should be .WAV, .AU or .AIFF.
            audioFile = AudioSystem.getAudioInputStream(new File(path));

            //create Clip from file.
            audioClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioFile.getFormat()));
            audioClip.addLineListener(this);
            audioClip.open(audioFile);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            playCompleted = true;
        }
    }

    @Override
    public void run() {
        //play audio.
        audioClip.start();

        //while file is still playing.
        while (!playCompleted) {
            try {
                //pause for a second then check again.
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        //close audio Clip.
        audioClip.close();

        //end thread.
    }
}
