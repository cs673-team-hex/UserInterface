/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JFrame;
/**
 *
 * @author Wei
 */
public class Music {

    
    private int Song_Num;    
    private static Music music = null; 
    
    private static void initial(){
        music = new Music();
    }

    private Music(){
        //startMusic(this.Song_Num);
    }
    
    public void startMusic(int song_number){
        
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
            String Add = "src/UI/BackGroundMusic/Normal" + song_number + ".wav";
            //String newAdd = this.getClass().getResource(Add).toString();
            File soundfile = new File(Add);
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundfile);
            clip.open(inputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Music GetMusic(){
        if(music == null)
            initial();
        return music;
    }
    
    /*public static void Create_MusicBtn(JFrame Jframe){
        JButton MusicBtn = new JButton("Play");
        Jframe.add(MusicBtn);
        MusicBtn.setSize(60,20);
        MusicBtn.setLocation(Jframe.getWidth() - 60,Jframe.getHeight() - 20);
        MusicBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (play == 1){
                    startMusic(1);
                    play = 0;
                }
                else if (play == 0){
                    
                }
            }
            
        });
    }*/
    
    public void SetSong_Num(int song_number){
        this.Song_Num = song_number;
        startMusic(this.Song_Num);
    }
    
    public int GetSong_Num(){
        return this.Song_Num;
    }
    
}
