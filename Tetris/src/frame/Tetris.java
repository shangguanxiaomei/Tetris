package frame;

import javax.swing.*;

public class Tetris {
	
	//***************************************************************************************
	//make a frame to hold a mainpanel which is the root-panel of this program.
	//***************************************************************************************	
	
	static JFrame frame = new JFrame("Tetris");
	
	public static void main (String[] args) {
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(300, 100);
	
		MainPanel main = new MainPanel();
		frame.getContentPane().add(main);

		frame.pack();
		frame.setVisible(true);
		
	}
}