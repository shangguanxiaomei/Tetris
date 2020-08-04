package frame;
//***************************************************************************************
//RuleSettingPanel.java                                   Author:  Beibei Zhang
//
//set the game rule here. 
//invoked by the correspond menu in the mainpanel
//
//can set Attack Strength, Points for Levelup and Levelup Bonus here
//Attack Strength: how many lines will be send to opponent if N line is disposed once.
//Points for Levelup: how many points to get levelup
//Levelup Bonus: how many unfilled line will be disposed when levelup
//***************************************************************************************

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class RuleSettingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton ok,cancle;// confirm or cancel button
	private JFrame frame;
	private JComboBox<String> sendCombo, levelCombo, levelupCombo;//correspond combobox for user to select
	private JLabel sendLabel, levelLabel, levelupLabel;//correspond label to explain combobox
	private JPanel sendPanel, levelPanel, levelupPanel;//correspond panel to hold label and combobox 
	private RuleListener ruleListener;//listener for comboboxes
	private ButtonListener buttonListener;//listener for ok and cancle buttons
	private int send, levelupperpoints, deleterrowwhenlevelup; //user's selection store here
	private final String FILE = "Configuration.txt";//store selection in this file 
	private final Font RULE_FONT = new Font("Italic",Font.PLAIN,20);
	private final Font BUTTON_FONT = new Font("Italic",Font.BOLD,20);
	private final Color BACKGROUD = new Color(192,192,192);
	
	//***************************************************************************************
	//put label and combobox in correspond panel, and put ok & cancle button in button panel
	//then arrange three selection panels and one buttonpanel in y direction by boxlayout
	//***************************************************************************************
	public RuleSettingPanel (JFrame frame){
		this.frame = frame;//make ok or cancle button can dispose current frame
		ruleListener = new RuleListener();
		buttonListener = new ButtonListener();
		//default setting
		send = -1;
		levelupperpoints = 10000;
		deleterrowwhenlevelup = 0;
		
        String[] sendComboChoise = {"Attack Strength","N-2","N-1","N","N+1"};
        sendCombo = new JComboBox<String>(sendComboChoise);
        sendCombo.setFont(RULE_FONT);
        sendCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendCombo.addActionListener(ruleListener);
        String[] levelComboChoise = {"Points for Levelup","1000","2000","3000","5000","10000","N/A"};
        levelCombo = new JComboBox<String>(levelComboChoise);
        levelCombo.setFont(RULE_FONT);
        levelCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelCombo.addActionListener(ruleListener);
        String[] levelupComboChoise = {"Levelup Bonus","0","1","2","3","5","10","All"};
        levelupCombo = new JComboBox<String>(levelupComboChoise);
        levelupCombo.setFont(RULE_FONT);
        levelupCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelupCombo.addActionListener(ruleListener);
        
        sendLabel = new JLabel("How many disposed lines will be sent to your opponent?");
        sendLabel.setFont(RULE_FONT);
        levelLabel = new JLabel("How many points are for every level?");
        levelLabel.setFont(RULE_FONT);
        levelupLabel = new JLabel("How many row will be disposed when levelup?");
        levelupLabel.setFont(RULE_FONT);
        
        sendPanel = new JPanel();
        sendPanel.setBackground(BACKGROUD);
        sendPanel.add(sendLabel);
        sendPanel.add(Box.createRigidArea(new Dimension(0,80)));
        sendPanel.add(sendCombo);
        levelPanel = new JPanel();
        levelPanel.setBackground(BACKGROUD);
        levelPanel.add(levelLabel);
        levelPanel.add(Box.createRigidArea(new Dimension(0,70)));
        levelPanel.add(levelCombo);
        levelupPanel = new JPanel();
        levelupPanel.setBackground(BACKGROUD);
        levelupPanel.add(levelupLabel);
        levelupPanel.add(Box.createRigidArea(new Dimension(0,70)));
        levelupPanel.add(levelupCombo);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(500, 650));
        add(Box.createRigidArea(new Dimension(0,60)));
        add(sendPanel);
        add(Box.createRigidArea(new Dimension(0,50)));
        add(levelPanel);
        add(Box.createRigidArea(new Dimension(0,60)));
        add(levelupPanel);
        add(Box.createRigidArea(new Dimension(0,60)));
        
		ok = new JButton("Ok");
		ok.setFont(BUTTON_FONT);
		ok.addActionListener(buttonListener);
		cancle = new JButton("Cancle");
		cancle.setFont(BUTTON_FONT);
		cancle.addActionListener(buttonListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(BACKGROUD);
		buttonPanel.add(ok);
		buttonPanel.add(Box.createRigidArea(new Dimension(100,0)));
		buttonPanel.add(cancle);
		add(buttonPanel);
		
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
					for (int i=0;(str[i] = br.readLine()) != null;i++) {}
					br.close();
					str[0] = "lines to send";
					str[1] = Integer.toString(send);
					str[2] = "";
					str[3] = "points per level";
					str[4] = Integer.toString(levelupperpoints);
					str[5] = "";
					str[6] = "levelup bonus";
					str[7] = Integer.toString(deleterrowwhenlevelup);
					str[8] = "-----------------------";
					FileWriter fw = new FileWriter(FILE);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw =new PrintWriter(bw);
					for (int i=0;i < 41;i++)pw.println(str[i]);
					pw.close();
				}
				//do nothing, cause default setting in set in constructor.
				catch (IOException exception){}
			}
			frame.dispose();
		}
	}
	
	//***************************************************************************************
	//listener for comboboxer, set three parameters value according to user's selection,
	//and they will be written back to Configuration.txt file if user click ok button
	//***************************************************************************************
	private class RuleListener implements ActionListener{

		public void actionPerformed (ActionEvent event) {
                
			send = sendCombo.getSelectedIndex()-3;
			if (send == -3)send = -1;			
			levelupperpoints = levelCombo.getSelectedIndex()*1000;
			if (levelupperpoints == 0)levelupperpoints = 10000;
			if (levelupperpoints == 6000)levelupperpoints = 99999999;
			if (levelupperpoints == 5000)levelupperpoints = 10000;
			if (levelupperpoints == 4000)levelupperpoints = 5000;			
			deleterrowwhenlevelup = levelupCombo.getSelectedIndex()-1;
			if (deleterrowwhenlevelup == -1)deleterrowwhenlevelup = 0;
			if (deleterrowwhenlevelup == 6)deleterrowwhenlevelup = 20;
			if (deleterrowwhenlevelup == 5)deleterrowwhenlevelup = 10;
			if (deleterrowwhenlevelup == 4)deleterrowwhenlevelup = 5;
		}
	}
}