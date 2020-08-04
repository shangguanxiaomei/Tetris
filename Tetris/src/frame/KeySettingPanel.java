package frame;
//***************************************************************************************
//KeySettingPanel.java                                   Author:  Beibei Zhang
//
//set the key here. 
//invoked by the correspond menu in the mainpanel
//
//set moveleft, moveright, movedown, dropdown, rotate, pause here               
//***************************************************************************************

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class KeySettingPanel extends JPanel {
	
	//***************************************************************************************
	//for each key setting part, there is a panel to hold a label and a textfield.
	//label is used to show the title, textfield is used to input the key
	//then six panel is arrange by 3*2 grid, which followed by a button panel to hold 
	//ok and cancle button. 
	//user's setting will store in six corresponding variables and record in configuration.txt file
	//***************************************************************************************
	private static final long serialVersionUID = 1L;
	private JButton ok, cancle;// confirm or cancle button
	private JFrame frame;
	private Timer timer;
	private JTextField leftTextField, rightTextField, downTextField, dropTextField, rotateTextField, pauseTextField;
	private JPanel leftPanel, rightPanel, downPanel, dropPanel, rotatePanel, pausePanel;
	private JLabel leftLabel, rightLabel, downLabel, dropLabel, rotateLabel, pauseLabel;
	private KListener keyListener;//listener for comboboxes
	private ButtonListener buttonListener;//listener for ok and cancle buttons
	private int left, right, down, drop, rotate, pause;
	private final String FILE = "Configuration.txt";
	private final Font RULE_FONT = new Font("Italic",Font.PLAIN,20);
	private final Font BUTTON_FONT = new Font("Italic",Font.BOLD,20);
	private final Color BACKGROUD = new Color(192,192,192);
	
	public KeySettingPanel (JFrame frame) {
		this.frame = frame;
		keyListener = new KListener();
		buttonListener = new ButtonListener();
		
		//default setting, the number is keycode.
		left = 37;	
		right = 39;
		down = 40;
		drop = 10;
		rotate = 38;
		pause = 32;
		
        leftTextField = new JTextField(8);
        leftTextField.setFont(RULE_FONT);
        leftTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftTextField.addKeyListener(keyListener);
        rightTextField = new JTextField(8);
        rightTextField.setFont(RULE_FONT);
        rightTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightTextField.addKeyListener(keyListener);
        downTextField = new JTextField(8);
        downTextField.setFont(RULE_FONT);
        downTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        downTextField.addKeyListener(keyListener);
        dropTextField = new JTextField(8);
        dropTextField.setFont(RULE_FONT);
        dropTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        dropTextField.addKeyListener(keyListener);
        rotateTextField = new JTextField(8);
        rotateTextField.setFont(RULE_FONT);
        rotateTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        rotateTextField.addKeyListener(keyListener);
        pauseTextField = new JTextField(8);
        pauseTextField.setFont(RULE_FONT);
        pauseTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseTextField.addKeyListener(keyListener);
        
        leftLabel = new JLabel("Move Left:  ");
        leftLabel.setFont(RULE_FONT);
        rightLabel = new JLabel("Move Right: ");
        rightLabel.setFont(RULE_FONT);
        downLabel = new JLabel("Move Down: ");
        downLabel.setFont(RULE_FONT);
        dropLabel = new JLabel("Drop Brick: ");
        dropLabel.setFont(RULE_FONT);
        rotateLabel = new JLabel("Rotate Brick:");
        rotateLabel.setFont(RULE_FONT);
        pauseLabel = new JLabel("Pause Game:");
        pauseLabel.setFont(RULE_FONT);
        
        leftPanel = new JPanel();
        leftPanel.setBackground(BACKGROUD);
        leftPanel.add(leftLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0,70)));
        leftPanel.add(leftTextField);
        rightPanel = new JPanel();
        rightPanel.setBackground(BACKGROUD);
        rightPanel.add(rightLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0,70)));
        rightPanel.add(rightTextField);
        downPanel = new JPanel();
        downPanel.setBackground(BACKGROUD);
        downPanel.add(downLabel);
        downPanel.add(Box.createRigidArea(new Dimension(0,70)));
        downPanel.add(downTextField);
        dropPanel = new JPanel();
        dropPanel.setBackground(BACKGROUD);
        dropPanel.add(dropLabel);
        dropPanel.add(Box.createRigidArea(new Dimension(0,70)));
        dropPanel.add(dropTextField);
        rotatePanel = new JPanel();
        rotatePanel.setBackground(BACKGROUD);
        rotatePanel.add(rotateLabel);
        rotatePanel.add(Box.createRigidArea(new Dimension(0,70)));
        rotatePanel.add(rotateTextField);
        pausePanel = new JPanel();
        pausePanel.setBackground(BACKGROUD);
        pausePanel.add(pauseLabel);
        pausePanel.add(Box.createRigidArea(new Dimension(0,70)));
        pausePanel.add(pauseTextField);
        
        //outer panel to hold six panels
        JPanel rlPanel = new JPanel(); 
        rlPanel.setLayout(new GridLayout(3,2));
        rlPanel.setBackground(BACKGROUD);
        
        rlPanel.add(leftPanel);
        rlPanel.add(rightPanel);
        rlPanel.add(downPanel);
        rlPanel.add(dropPanel);
        rlPanel.add(rotatePanel);
        rlPanel.add(pausePanel);
        
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
		addKeyListener(keyListener);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(rlPanel);
		add(Box.createRigidArea(new Dimension(0,30)));
		add(buttonPanel);
		add(Box.createRigidArea(new Dimension(0,20)));
		setBackground(BACKGROUD);
		
		//timer to set textfield focusable back
		timer = new Timer(100, new TimerListener());
		timer.start();
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
					for(int i=0;(str[i] = br.readLine()) != null;i++) {}
					br.close();
					str[24] = "move left";
					str[25] = Integer.toString(left);
					str[26] = "";
					str[27] = "move right";
					str[28] = Integer.toString(right);
					str[29] = "";
					str[30] = "move down";
					str[31] = Integer.toString(down);
					str[32] = "";
					str[33] = "drop brick";
					str[34] = Integer.toString(drop);
					str[35] = "";
					str[36] = "rotate brick";
					str[37] = Integer.toString(rotate);
					str[38] = "";
					str[39] = "pause";
					str[40] = Integer.toString(pause);
					FileWriter fw = new FileWriter(FILE);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw =new PrintWriter(bw);
					for(int i=0;i < 41;i++)pw.println(str[i]);
					pw.close();
				}
				//do nothing, cause default setting in set in constructor.
				catch (IOException exception){}
			}
			frame.dispose();
		}
	}
	
	//***************************************************************************************
	//listener for keyboard, set control keys and the correspond textfield accroding use's input
	//and they will be written back to Configuration.txt file if user click ok button
	//***************************************************************************************
	private class KListener implements KeyListener{

		//***************************************************************************************
		//each time user press a key keycode is stored in an int variable, and the keytext will
		//be showed in the textfield, the textfield is also setFocusable(false),to automatically
		//switch to next textfield.
		//***************************************************************************************
		public void keyPressed (KeyEvent event) {
			if(event.getSource() == leftTextField){
				left = event.getKeyCode();
				leftTextField.setText(KeyEvent.getKeyText(left));
				delay(30);
				leftTextField.setFocusable(false);
			}
			else if (event.getSource() == rightTextField){
				right = event.getKeyCode();
				rightTextField.setText(KeyEvent.getKeyText(right));
				delay(30);
				rightTextField.setFocusable(false);
			}
			else if (event.getSource() == downTextField){
				down = event.getKeyCode();
				downTextField.setText(KeyEvent.getKeyText(down));
				delay(30);
				downTextField.setFocusable(false);
			}
			else if (event.getSource() == dropTextField){
				drop = event.getKeyCode();
				dropTextField.setText(KeyEvent.getKeyText(drop));
				delay(30);
				dropTextField.setFocusable(false);
			}
			else if (event.getSource() == rotateTextField){
				rotate = event.getKeyCode();
				rotateTextField.setText(KeyEvent.getKeyText(rotate));
				delay(30);
				rotateTextField.setFocusable(false);
			}
			else if (event.getSource() == pauseTextField){
				pause = event.getKeyCode();
				pauseTextField.setText(KeyEvent.getKeyText(pause));
				delay(30);
				pauseTextField.setFocusable(false);
			}
		}

		public void keyTyped (KeyEvent event) {}

		public void keyReleased (KeyEvent event) {}
	}
	
	public static void delay(int ms) {
	  	  try {Thread.sleep(ms);} 
	  	  catch (InterruptedException e) {}
	}
	

	//***************************************************************************************
	//user timer to set textfield setFocusable(true) back periodly
	//***************************************************************************************
	private class TimerListener implements ActionListener{		
		
		public void actionPerformed (ActionEvent event){
			leftTextField.setFocusable(true);
			rightTextField.setFocusable(true);
			downTextField.setFocusable(true);
			dropTextField.setFocusable(true);
			rotateTextField.setFocusable(true);
			pauseTextField.setFocusable(true);
		}
	}
}