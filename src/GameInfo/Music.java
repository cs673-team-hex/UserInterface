/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import sun.audio.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 *
 * @author Wei
 */
public class Music {
    
    private int Song_Num;
    
    private static Music music = null; 
    
    public static void initial(int song_number){
        music = new Music(song_number);
    }

    private Music(int song_number){
        this.Song_Num = song_number;
        startMusic(this.Song_Num);
    }
    
    private void startMusic(int song_number){
        
        try {
            /*AudioPlayer myBackgroundPlayer = AudioPlayer.player;
            AudioStream myBackgroundMusic;
            AudioData myData;
            ContinuousAudioDataStream myLoop = null;
            try{
            myBackgroundMusic = new AudioStream(new FileInputStream("src/UI/BackGroundMusic/TheDawn.wav"));
            myData = myBackgroundMusic.getData();
            myLoop = new ContinuousAudioDataStream(myData);
            } catch (IOException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
            }
            myBackgroundPlayer.start(myLoop);*/
            File soundfile = new File("src/UI/BackGroundMusic/Normal.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile);
            clip.open(inputStream);
            clip.start();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Music GetMusic(){
        return music;
    }
    
    public void SetSong_Num(int song_number){
        this.Song_Num = song_number;
        startMusic(this.Song_Num);
    }
    
    public int GetSong_Num(){
        return this.Song_Num;
    }
    
}
