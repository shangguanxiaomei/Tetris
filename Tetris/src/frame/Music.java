package frame;

import java.net.URL;
import javax.sound.sampled.*;
import java.io.IOException;





public class Music {
	
	public static Clip backmusic;
	public static void playBackMusic(int index) {
		if (backmusic != null)backmusic.stop();
		if(index == 1) {
		      try {
		    	AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/pal.mid"));
		    	backmusic = AudioSystem.getClip();
		    	backmusic.open(audioIn);
		    	backmusic.start();
		    	backmusic.loop(Clip.LOOP_CONTINUOUSLY);
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		}else if(index == 2) {
		      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/palbutterfly.mid"));
		    	backmusic = AudioSystem.getClip();
		    	backmusic.open(audioIn);
		    	backmusic.start();
		    	backmusic.loop(Clip.LOOP_CONTINUOUSLY);
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		}else if(index == 3) {
		      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/ferrylove.mid"));
		    	backmusic = AudioSystem.getClip();
		    	backmusic.open(audioIn);
		    	backmusic.start();
		    	backmusic.loop(Clip.LOOP_CONTINUOUSLY);
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		}else if(index == 4) {
		      try {
		        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/eyes on me.mid"));
		    	backmusic = AudioSystem.getClip();
		    	backmusic.open(audioIn);
		    	backmusic.start();
		    	backmusic.loop(Clip.LOOP_CONTINUOUSLY);
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		}
	 }
	public static void stopBackMusic() {
		if (backmusic != null) {
			if (backmusic.isRunning())backmusic.stop();
		}
	}	

	public static void keyboardMusic() {		   
	      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/keyboard.wav"));
	    	    Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	 }
	   
	public static void settleMusic() {		   
	      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/settle.wav"));
	    	    Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	 }
	   
	public static void cleanrowMusic() {		   
	      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/cleanrow.wav"));
	    	    Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	 }
	
	public static void winMusic() {		   
	      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/win.wav"));
	    	    Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	 }
	
	public static void dieMusic() {		   
	      try {
	    	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file", "localhost","sound/die.wav"));
	    	    Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	 }
	   

}


