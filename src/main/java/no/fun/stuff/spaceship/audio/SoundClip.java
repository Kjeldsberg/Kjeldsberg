package no.fun.stuff.spaceship.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip {
	private Clip clip;
	private FloatControl gainControll;
	
	
 public SoundClip(String path) {
	 try (InputStream audioSource = SoundClip.class.getResourceAsStream(path);
			 InputStream buffered = new BufferedInputStream(audioSource);
			 AudioInputStream ais = AudioSystem.getAudioInputStream(buffered)) {
		 	final AudioFormat  baseFormat = ais.getFormat();
		 	AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
		 			baseFormat.getSampleRate(),
		 			16,
		 			baseFormat.getChannels(),
		 			baseFormat.getChannels() * 2,
		 			baseFormat.getFrameRate(),
		 			false);
		 	AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
		 	
		 	clip = AudioSystem.getClip();
		 	clip.open(dais);
		 	gainControll = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		 
	 } catch (final IOException e) {
		e.printStackTrace();
	} catch (final UnsupportedAudioFileException e) {
		e.printStackTrace();
	} catch (LineUnavailableException e) {
		e.printStackTrace();
	}
	}
	 	 
	 
	 public void play() {
		 if(Optional.ofNullable(clip).isEmpty()) {
			 return;
		 }
		 clip.stop();
		 clip.setFramePosition(0);
		 while(!clip.isRunning()) {
			 clip.start();
		 }
 }
	 public void stop() {
		 if(clip.isRunning()) {
			 clip.stop();
		 }
	 }
	 public void close() {
		 stop();
		 clip.drain();
		 clip.close();
	 }
	 public void loop() {
		 clip.loop(Clip.LOOP_CONTINUOUSLY);
		 play();
		 
	 }
	 public void volume(float volume) {
		 gainControll.setValue(volume);
	 }
	 public boolean isRunning() {
		 return clip.isRunning();
				
	 }
}
