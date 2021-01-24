package controller;


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Con_BGM {

	//음악 실행을 위해 필요한 변수
	private File bgm = new File("music_wav\\game_BGM.wav");
	private AudioInputStream stream; 
	private AudioFormat format;
	private DataLine.Info info;
	
	
	Clip clip;
	
	public Con_BGM() {
		
	}
	
	public void BGM_On() {
		try {
			stream = AudioSystem.getAudioInputStream(bgm);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
			
		} catch (UnsupportedAudioFileException e) {	System.err.println(e.getMessage());
		} catch (IOException e) {System.err.println(e.getMessage());
		} catch (LineUnavailableException e) {System.err.println(e.getMessage());
		}
	}
	
	public void BGM_Off() {
		clip.close();
		clip.drain();
	}
	
	
}
