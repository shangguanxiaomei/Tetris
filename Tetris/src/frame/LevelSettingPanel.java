package frame;
//***************************************************************************************
//LevelSettingPanel.java                                   Author:  Beibei Zhang
//
//set the key here. 
//invoked by the correspond menu in the mainpanel
//
//set player start rows, start speed, computer start rows, start speed and AI level
//AI 1-5 :AI take 300ms, 200ms, 150ms, 100ms, 80ms on each move, AI do not use drop down.
//AI 6-8 :AI take 200ms, 150ms, 125ms one each move, AI use drop down instead of move down.
//***************************************************************************************

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class LevelSettingPanel extends JPanel {
	
	//***************************************************************************************
	//put label and combobox in correspond panel, and put prowPanel, plevelPanel, crowPanel, 
	//and clevelPanel in a bigger panel(rlpanel) by 2*2, then put ok & cancle button in button panel
	//finally arrange rlpanel, AI panel and buttonpanel in y direction by boxlayout.
	//***************************************************************************************
	private static final long serialVersionUID = 1L;
	private JButton ok, cancle;//confirm or cancle button.
	private JFrame frame;
	private JComboBox<String> prowCombo, plevelCombo, crowCombo, clevelCombo, AICombo;
	private JPanel prowPanel, plevelPanel, crowPanel, clevelPanel, AIPanel;
	private JLabel prowLabel, plevelLabel, crowLabel, clevelLabel, AILabel;
	private LevelListener levelListener;//listener for combobox
	private ButtonListener buttonListener;//listener for ok and cancle buttons
	private int hrow, hlevel, crow, clevel, ai;
	private final String FILE = "Configuration.txt";
	private final Font RULE_FONT = new Font("Italic",Font.PLAIN,20);
	private final Font BUTTON_FONT = new Font("Italic",Font.BOLD,20);
	private final Color BACKGROUD = new Color(192,192,192);
	
	public LevelSettingPanel (JFrame frame) {
		this.frame = frame;
		levelListener = new LevelListener();
		buttonListener = new ButtonListener();
		//default setting
		hrow = 0;
		hlevel = 0;
		crow = 0;
		clevel = 0;
		ai = 4;
		
        String[] hrowComboChoise = {"Select start rows...","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
        prowCombo = new JComboBox<String>(hrowComboChoise);
        prowCombo.setFont(RULE_FONT);
        prowCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        prowCombo.addActionListener(levelListener);
        String[] hlevelComboChoise = {"Select start level...","0","1","2","3","4","5","6","7","8","9","10"};
        plevelCombo = new JComboBox<String>(hlevelComboChoise);
        plevelCombo.setFont(RULE_FONT);
        plevelCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        plevelCombo.addActionListener(levelListener);
        String[] crowComboChoise = {"Select start rows...","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
        crowCombo = new JComboBox<String>(crowComboChoise);
        crowCombo.setFont(RULE_FONT);
        crowCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        crowCombo.addActionListener(levelListener);
        String[] clevelComboChoise = {"Select start level...","0","1","2","3","4","5","6","7","8","9","10"};
        clevelCombo = new JComboBox<String>(clevelComboChoise);
        clevelCombo.setFont(RULE_FONT);
        clevelCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        clevelCombo.addActionListener(levelListener);
        String[] AIComboChoise = {"Select Computer Level...","I can win","Kid","Beginner","Normal","Hardcore","Nightmare","Hell","Impossible"};
        AICombo = new JComboBox<String>(AIComboChoise);
        AICombo.setFont(RULE_FONT);
        AICombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        AICombo.addActionListener(levelListener);
        
        prowLabel = new JLabel("Player start rows");
        prowLabel.setFont(RULE_FONT);
        plevelLabel = new JLabel("Player start level");
        plevelLabel.setFont(RULE_FONT);
        crowLabel = new JLabel("Computer start rows");
        crowLabel.setFont(RULE_FONT);
        clevelLabel = new JLabel("Computer start level");
        clevelLabel.setFont(RULE_FONT);
        AILabel = new JLabel("Computer AI level");
        AILabel.setFont(RULE_FONT);
        
        prowPanel = new JPanel();
        prowPanel.setBackground(BACKGROUD);
        prowPanel.add(prowLabel);
        prowPanel.add(Box.createRigidArea(new Dimension(0,70)));
        prowPanel.add(prowCombo);
        plevelPanel = new JPanel();
        plevelPanel.setBackground(BACKGROUD);
        plevelPanel.add(plevelLabel);
        plevelPanel.add(Box.createRigidArea(new Dimension(0,70)));
        plevelPanel.add(plevelCombo);
        crowPanel = new JPanel();
        crowPanel.setBackground(BACKGROUD);
        crowPanel.add(crowLabel);
        crowPanel.add(Box.createRigidArea(new Dimension(0,70)));
        crowPanel.add(crowCombo);
        clevelPanel = new JPanel();
        clevelPanel.setBackground(BACKGROUD);
        clevelPanel.add(clevelLabel);
        clevelPanel.add(Box.createRigidArea(new Dimension(0,70)));
        clevelPanel.add(clevelCombo);
        AIPanel = new JPanel();
        AIPanel.setPreferredSize(new Dimension(10, 130));
        AIPanel.setBackground(BACKGROUD);
        AIPanel.add(Box.createRigidArea(new Dimension(40,0)));
        AIPanel.add(AILabel);
        AIPanel.add(Box.createRigidArea(new Dimension(50,0)));
        AIPanel.add(Box.createRigidArea(new Dimension(500,10)));
        AIPanel.add(AICombo);
        
        //outer panel to hold 4 setting panels
        JPanel rlPanel = new JPanel(); 
        rlPanel.setLayout(new GridLayout(2,2));
        rlPanel.setBackground(BACKGROUD);
        rlPanel.setPreferredSize(new Dimension(500, 300));
        rlPanel.add(prowPanel);
        rlPanel.add(plevelPanel);
        rlPanel.add(crowPanel);
        rlPanel.add(clevelPanel);
        
		ok = new JButton("Ok");
		ok.setFont(BUTTON_FONT);
		ok.addActionListener(buttonListener);
		cancle = new JButton("Cancle");
		cancle.setFont(BUTTON_FONT);
		cancle.addActionListener(buttonListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(BACKGROUD);
		buttonPanel.add(ok);
		buttonPanel.add(Box.createRigidArea(new Dimension(30,0)));
		buttonPanel.add(cancle);
		add(buttonPanel);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(rlPanel);
		add(AIPanel);
		add(Box.createRigidArea(new Dimension(0,30)));
		add(buttonPanel);
		add(Box.createRigidArea(new Dimension(0,20)));
		setBackground(BACKGROUD);
	}
	
	//***************************************************************************************
	//listen to ok & cancle button,if user click ok,store current selection to
	//Configuration.txt file, if user click cancle, do nothing
	//then dispose current frame.
	//***************************************************************************************
	private class ButtonListener implements ActionListener {

		//***************************************************************************************
		//read out all the setting information in Configuration.txt file to a string array
		//then modify the correspond setting information, and write back.
		//***************************************************************************************
		public void actionPerformed (ActionEvent event) {
			if (event.getSource() == ok){
				try{
					FileReader fr = new FileReader(FILE);
					BufferedReader br = new BufferedReader(fr);
					String[] str = new String[42];
					for (int i=0;(str[i] = br.readLine()) != null;i++){}
					br.close();
					str[9] = "player start rows";
					str[10] = Integer.toString(hrow);
					str[11] = "";
					str[12] = "player start level";
					str[13] = Integer.toString(hlevel);
					str[14] = "";
					str[15] = "computer start rows";
					str[16] = Integer.toString(crow);
					str[17] = "";
					str[18] = "computer start level";
					str[19] = Integer.toString(clevel);
					str[20] = "";
					str[21] = "computer AI level";
					str[22] = Integer.toString(ai);
					str[23] = "-----------------------";
					FileWriter fw = new FileWriter(FILE);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw =new PrintWriter(bw);
					for (int i=0;i < 41;i++)	pw.println(str[i]);
					pw.close();
				}
				//do nothing, cause default setting in set in constructor.
				catch (IOException exception){}
			}
			frame.dispose();
		}
	}
	
	//***************************************************************************************
	//listener for comboboxer, set game reference value according to user's selection,
	//and they will be written back to Configuration.txt file if user click ok button
	//***************************************************************************************
	private class LevelListener implements ActionListener{

		public void actionPerformed (ActionEvent event) {
                
			hrow = prowCombo.getSelectedIndex()-1;
			if (hrow == -1)hrow = 0;
			hlevel = plevelCombo.getSelectedIndex()-1;
			if (hlevel == -1)hlevel = 0;
			crow = crowCombo.getSelectedIndex()-1;
			if (crow == -1)crow = 0;
			clevel = clevelCombo.getSelectedIndex()-1;
			if (clevel == -1)clevel = 0;
			ai = AICombo.getSelectedIndex();
			if (ai == 0)ai = 4;
		}
	}
}