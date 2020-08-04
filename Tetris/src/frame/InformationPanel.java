package frame;
//***************************************************************************************
//InformationPanel.java                                   Author:  Beibei Zhang
//
//show the information of current round. 
//added in the mainpanel
//
//arrange them vertically, from top to bottom, the order is:
//forecastbrick of player.
//backgroud music select combox
//version and author information of this game
//brief statistics information of player
//***************************************************************************************

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import shape.Brick;

public class InformationPanel extends JPanel {
	

	private static final long serialVersionUID = 1L;
	private Brick forecastbrick;
	public  static int backmusicindex;
	private JComboBox<String> musicCombo;
	private JLabel pointLabel, levelLabel, apmLabel, bpmLabel;//to show brief statistics information of player
	private final int BLOCK_SIZE = 31;//size of each block
	private final int SIZE = 33;//area each block occupied
	private final Font COMBO_FONT = new Font("Italic",Font.PLAIN,20);
	private final Font LABEL_FONT = new Font("Italic",Font.BOLD,25);
	private final int FORECASTBRICK_XOFFSIDE = -30;//adjust forecastbrick xposition
	private final int FORECASTBRICK_YOFFSIDE = 110;//adjust forecastbrick yposition
	
	public InformationPanel ()
	{
		JPanel next = new JPanel();
		next.setBackground(MainPanel.backgroundcolor);
		JLabel text = new JLabel("  NEXT ");
		text.setFont(new Font("ITALIC",Font.BOLD,40));
		text.setForeground(new Color(0,152,255));
		next.add(text);
		//initialize forecastbrick, with as same color as backgroud, user won't see this
		forecastbrick = new Brick(8);
				
		backmusicindex = 1;
		String musicNames[] = {"Choose Music","Pal","Butterfly","Ferrylove","Eyes On Me","Mute"};
        musicCombo = new JComboBox<String>(musicNames);
        musicCombo.setFont(COMBO_FONT);
        musicCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicCombo.addActionListener(new ComboListener());
		
        
        //AboutPanel hold all the version and author information
        //aboutholder is an outer panel containing aboutpanel to ajust the position of aboutpanel
        AboutPanel about = new AboutPanel();
        JPanel aboutholder = new JPanel();        
		aboutholder.setLayout(new BoxLayout(aboutholder, BoxLayout.X_AXIS));
		aboutholder.add(about);
        
		//show brief statistics information of player
		//*L label show the title, *Label label show th number
		//then put them in 4*2 grid.
        JLabel pointL = new JLabel("    Score:");
		pointLabel = new JLabel("");
		JLabel levelL = new JLabel("    Level:");
		levelLabel = new JLabel("");
		JLabel apmL = new JLabel("    Apm:");
		apmLabel = new JLabel("");
		JLabel bpmL = new JLabel("    Bpm:");
		bpmLabel = new JLabel("");
		
		pointL.setFont(LABEL_FONT);
		pointLabel.setFont(LABEL_FONT);
		levelL.setFont(LABEL_FONT);
		levelLabel.setFont(LABEL_FONT);
		apmL.setFont(LABEL_FONT);
		apmLabel.setFont(LABEL_FONT);
		bpmL.setFont(LABEL_FONT);
		bpmLabel.setFont(LABEL_FONT);
        

        JPanel report = new JPanel();        
        report.setBackground(new Color(0,0,0,0));
		report.setLayout(new GridLayout(4,2));
		report.add(pointL);
		report.add(pointLabel);
		report.add(levelL);
		report.add(levelLabel);
		report.add(apmL);
		report.add(apmLabel);
		report.add(bpmL);
		report.add(bpmLabel);
		
        //arrange all the element vertically, adjust the position
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(0,30)));
		add(next);
        add(Box.createRigidArea(new Dimension(0,130)));
        add(musicCombo);
        add(Box.createRigidArea(new Dimension(0,40)));
        add(aboutholder);       
        add(Box.createRigidArea(new Dimension(0,50)));
        add(report);
		add(Box.createRigidArea(new Dimension(0,20)));
		
		
		setPreferredSize(new Dimension(230,670));
		setBackground(MainPanel.backgroundcolor);
	}   
	
	//***************************************************************************************
	//print out forecastbrick
	//***************************************************************************************
	public void paintComponent (Graphics page) {
		
		super.paintComponent(page);
		
		page.setColor(forecastbrick.getColor());
		int i = forecastbrick.getShape();
		if (i < 2) {
			int y = -13;
			page.fillRect(forecastbrick.get0x()*SIZE+FORECASTBRICK_XOFFSIDE+y, forecastbrick.get0y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
			page.fillRect(forecastbrick.get1x()*SIZE+FORECASTBRICK_XOFFSIDE+y, forecastbrick.get1y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
			page.fillRect(forecastbrick.get2x()*SIZE+FORECASTBRICK_XOFFSIDE+y, forecastbrick.get2y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
			page.fillRect(forecastbrick.get3x()*SIZE+FORECASTBRICK_XOFFSIDE+y, forecastbrick.get3y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);		
		}else {			
			page.fillRect(forecastbrick.get0x()*SIZE+FORECASTBRICK_XOFFSIDE, forecastbrick.get0y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
			page.fillRect(forecastbrick.get1x()*SIZE+FORECASTBRICK_XOFFSIDE, forecastbrick.get1y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
			page.fillRect(forecastbrick.get2x()*SIZE+FORECASTBRICK_XOFFSIDE, forecastbrick.get2y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
			page.fillRect(forecastbrick.get3x()*SIZE+FORECASTBRICK_XOFFSIDE, forecastbrick.get3y()*SIZE+FORECASTBRICK_YOFFSIDE,BLOCK_SIZE, BLOCK_SIZE);
		}
	}
	
	//***************************************************************************************
	//listener for musiccombobox to select background music
	//automaticly play after selection
	//***************************************************************************************
	private class ComboListener implements ActionListener{

		public void actionPerformed (ActionEvent event){
			//if user select the same backmusic, then do nothing
			if (backmusicindex != musicCombo.getSelectedIndex()){
				if (backmusicindex!= 0)Music.stopBackMusic();
				backmusicindex = musicCombo.getSelectedIndex();
				if (backmusicindex != 0)Music.playBackMusic(backmusicindex);
			}
			musicCombo.setFocusable(false);
		}
	}
	
	//***************************************************************************************
	//methods to stop or play background music.
	//***************************************************************************************
	
	//***************************************************************************************
	//normal set methods
	//update the correspond information showed in this panel
	//***************************************************************************************
	public void setforcast (int forecast){
		forecastbrick = new Brick(forecast);
		repaint();	
	}
	
	public void setpointLabel (String point){
		pointLabel.setText("       " + point);
		repaint();
	}
	
	public void setlevelLabel (String level){
		levelLabel.setText("       " + level);
		repaint();
	}
	
	public void setapmLabel (String apm){
		apmLabel.setText("       " + apm);
		repaint();
	}
	
	public void setbpmLabel (String bpm){
		bpmLabel.setText("       " + bpm);
		repaint();
	}
}