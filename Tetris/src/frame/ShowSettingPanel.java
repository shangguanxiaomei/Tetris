package frame;
//***************************************************************************************
//ShowSettingPanel.java                                   Author:  Beibei Zhang
//
//to show the setting information in a TextArea.
//invoked by the correspond menu in the mainpanel.
//
//setting information is stored in Configuration.txt file in the same directory as jar file
//read them is a string array(each string get one line) and print the string array in TextArea.
//***************************************************************************************

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.swing.*;

public class ShowSettingPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JTextArea settingTextArea;
	private final String FILE = "Configuration.txt";
	private final Font RULE_FONT = new Font("Italic",Font.PLAIN,20);
	
	//show setting information in TextArea, and put the TextArea in a panel.
	public ShowSettingPanel () {
		settingTextArea = new JTextArea(25,25);
		settingTextArea.setFont(RULE_FONT);
		try{
			FileReader fr = new FileReader(FILE);
			BufferedReader br = new BufferedReader(fr);
			String[] str = new String[42];
			for (int i=0;(str[i] = br.readLine()) != null;i++) {}
			br.close();
			//these six strings are control key information.
			//because they are stored in form of keycode in Configuration.txt file
			//so convert to keytext here to printout.
			str[25] = KeyEvent.getKeyText(Integer.parseInt(str[25]));
			str[28] = KeyEvent.getKeyText(Integer.parseInt(str[28]));
			str[31] = KeyEvent.getKeyText(Integer.parseInt(str[31]));
			str[34] = KeyEvent.getKeyText(Integer.parseInt(str[34]));
			str[37] = KeyEvent.getKeyText(Integer.parseInt(str[37]));
			str[40] = KeyEvent.getKeyText(Integer.parseInt(str[40]));
			//print setting information string by string(line by line)
			for (int i = 0;i < 42;i +=3)settingTextArea.append(str[i]+": "+str[i+1]+"\n"+"\n");
		}
		//if Configuration.txt file is ruined, and cant be read, show user how to restore it.
		catch (IOException exception){
			settingTextArea.append("Error!!!");
			settingTextArea.append("Restore default setting to fix it.");
		}
		add(settingTextArea);
	}
}