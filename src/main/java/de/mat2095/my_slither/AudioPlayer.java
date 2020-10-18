package de.mat2095.my_slither;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Adapted from code from the below site.
 * https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
 */
public class AudioPlayer implements LineListener, Runnable {

    //flag to show whether audio has finished playing or not.
    private boolean playCompleted;
    //instance of Clip class to hold playable audio.
    private Clip audioClip;
    //instance of AudioInputStream to hold audio file.
    private AudioInputStream audioFile;

    public AudioPlayer(String path) {
        try {
            //open audio file - should be .WAV, .AU or .AIFF.
            audioFile = AudioSystem.getAudioInputStream(new File(path));

            //create Clip from file.
            audioClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioFile.getFormat()));
            audioClip.addLineListener(this);
            audioClip.open(audioFile);
            
        //catch exceptions.
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

    
    //Listens to for STOP events of the audio line. 
    @Override
    public void update(LineEvent event) {
        //if audio finished playing.
        if (event.getType() == LineEvent.Type.STOP) {
            //set flag to true.
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
