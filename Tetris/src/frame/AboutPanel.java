package frame;
//***************************************************************************************
//AboutPanel.java                                   Author:  Caleb Zhang
//
//print the version information of this game.    
//added in the information panel
//***************************************************************************************

import java.awt.*;
import javax.swing.*;

public class AboutPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	//***************************************************************************************
	//print out title, version, author, blablabla
	//***************************************************************************************
	public AboutPanel ()
	{
		JLabel l1 = new JLabel("         Tetris");
		JLabel l2 = new JLabel("        Version: 3.1.5");
		JLabel l3 = new JLabel("Copyright: Caleb Zhang");
		//JLabel l4 = new JLabel("All rights reserved.");
		l1.setFont(new Font("Italic",Font.BOLD,30));
		l2.setFont(new Font("Italic",Font.BOLD,20));
		l3.setFont(new Font("Italic",Font.BOLD,20));
		//l4.setFont(new Font("Italic",Font.BOLD,20));
		l1.setForeground(Color.red);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(l1);
		add(l2);
		add(Box.createRigidArea(new Dimension(0,20)));
		add(l3);
		//add(l4);
		add(Box.createVerticalGlue());
		setBackground(MainPanel.backgroundcolor);
	}     

}