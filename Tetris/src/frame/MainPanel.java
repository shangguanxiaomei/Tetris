package frame;
//***************************************************************************************
//MainPanel.java                                   Author:  Beibei Zhang
//
//Outmost panel,contain playerpanel,computerpanel and menubar,which interact here. 
//***************************************************************************************

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class MainPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
	private PlayerPanel player;
	private ComputerPanel computer;
	private JPanel framepanel;//panel to hold player panel and computer panel
	//four timers, first two is to trigger the brick move down in the panel
	//the third one trigger the AI, last one count the game time.
	private Timer playertimer, computertimer, AItimer, timetimer;
	private int second;//record the game time
	private int gameover;//game state
	private int ctrlpressed;//to recorde if ctrl key is pressed, used for hot key
	private int left, right, down, drop, rotate, pause;//store control keycode
	private MenuListener menuListener;// listener for the menu
	private int gamemode;//0:single 1:vs computer
	//recorde statistics data of the game p for player, c for computer
	//	element 0 is total action
	//	element 1 is total brick dropped
	//  element 2 is one-row-dispose times
	//	element 3 is two-row-dispose times
	//	element 4 is three-row-dispose times
	//	element 5 is four-row-dispose times
	//	element 6 is total rows sent to opponent
	private int[] pstatistics, cstatistics;
	private JMenuBar menuBar;//container of menu
	private JMenu menu, setting, other;//three menu in the menubar
	//menuitem named after its position, item1* means it belong to 1st menu,
	//item*1 means it is the first one of in the menu, and so on
	private JMenuItem item11, item12, item13, item21, item22, item23, item24, item25, item31;
	private final Font MENU_FONT = new Font("Italic",Font.BOLD,20);
	private final String FILE = "Configuration.txt";
	//public static final Color backgroundcolor = new Color(204, 204, 255);
	public static final Color backgroundcolor = new Color(176,218,255);
	
	public MainPanel() {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		menuListener = new MenuListener();
		menuBar = new JMenuBar();
		
		//first menu
		menu = new JMenu("New Game");
		menu.setFont(MENU_FONT);
		menu.setMnemonic(KeyEvent.VK_N);
		item11 = new JMenuItem("Single Player");
		item11.setFont(MENU_FONT);
		item11.setMnemonic(KeyEvent.VK_G);
		item11.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		item11.addActionListener(menuListener);
		item12 = new JMenuItem("Vs Computer");
		item12.setFont(MENU_FONT);
		item12.setMnemonic(KeyEvent.VK_C);
		item12.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		item12.addActionListener(menuListener);
		item13 = new JMenuItem("Exit");
		item13.setFont(MENU_FONT);
		item13.setMnemonic(KeyEvent.VK_X);
		item13.addActionListener(menuListener);
		item13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menu.add(item11);
		menu.add(item12);
		menu.addSeparator();
		menu.add(item13);
		
		//second menu
		setting = new JMenu("Setting");
		setting.setFont(MENU_FONT);
		setting.setMnemonic(KeyEvent.VK_S);
		item21 = new JMenuItem("Game Rules");
		item21.setFont(MENU_FONT);
		item21.setMnemonic(KeyEvent.VK_U);
		item21.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		item21.addActionListener(menuListener);
		item22 = new JMenuItem("Level Setting");
		item22.setFont(MENU_FONT);
		item22.setMnemonic(KeyEvent.VK_L);
		item22.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		item22.addActionListener(menuListener);
		item23 = new JMenuItem("Define Keys");
		item23.setFont(MENU_FONT);
		item23.setMnemonic(KeyEvent.VK_K);
		item23.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		item23.addActionListener(menuListener);
		item24 = new JMenuItem("Show Current Setting");
		item24.setFont(MENU_FONT);
		item24.setMnemonic(KeyEvent.VK_H);
		item24.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		item24.addActionListener(menuListener);
		item25 = new JMenuItem("Restore Default Setting");
		item25.setFont(MENU_FONT);
		item25.setMnemonic(KeyEvent.VK_D);
		item25.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		item25.addActionListener(menuListener);
		setting.add(item21);
		setting.add(item22);
		setting.add(item23);
		setting.addSeparator();
		setting.add(item24);
		setting.add(item25);
		
		//third menu
		other = new JMenu("Others");
		other.setFont(MENU_FONT);
		other.setMnemonic(KeyEvent.VK_O);
		item31 = new JMenuItem("Statistics");
		item31.setFont(MENU_FONT);
		item31.setMnemonic(KeyEvent.VK_T);
		item31.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		item31.addActionListener(menuListener);
		other.add(item31);
		
		menuBar.add(menu);
		menuBar.add(setting);
		menuBar.add(other);
		menuBar.add(Box.createHorizontalGlue());
		add(menuBar);
		
		//initialize
		gamemode = 1;//default setting
		playertimer = new Timer(10000, new TimerListener());
		computertimer = new Timer(1000, new TimerListener());
		AItimer = new Timer(150, new TimerListener());
		timetimer = new Timer(1000, new TimerListener());
		
		//put player panel and computer panel in a bigger panel, and add to this panel
		framepanel = new JPanel();
		player = new PlayerPanel();	
		computer = new ComputerPanel();
		framepanel.add(player);
		framepanel.add(computer);
		framepanel.setBackground(backgroundcolor);
		add(framepanel);
		setFocusable(true);
		addKeyListener(new KListener());
		
		//if file Configuration.txt does not exit, -- first time run or be deleted.
		//create a new one with default setting
		try {
			FileReader fr = new FileReader(FILE);
			fr.close();
		}
		catch (IOException exception){restore();}
		restart();//start game			
	}
	
	//***************************************************************************************
	//listen to the menu, act if get selection
	//***************************************************************************************
	private class MenuListener implements ActionListener{
		
		public void actionPerformed (ActionEvent event){
			
			//set gamemode to single play
			if (event.getSource() == item11){
				gamemode = 0;
				restart();
			}
			
			//set gamemode to vs computer
			if (event.getSource() == item12){
				gamemode = 1;
                restart();
			}
			
			//quit game
			//pause game and showOptionDialog to confirm quit
			//if user choose quit, exit game; if user choose cancle unpause game
			if (event.getSource() == item13){
				if (playertimer.isRunning()) pause();
				Object[] options = { "QUIT", "CANCEL" };
				int choice = JOptionPane.showOptionDialog(framepanel, "Quit Program?","Confirm Quit",2,JOptionPane.QUESTION_MESSAGE,null,options, options[0]);
				if (choice == 0)System.exit(0);
				pause();
			}
			
			//pause game and invoke rulesettingpanel to set rules
			if (event.getSource() == item21){
				if (playertimer.isRunning()) pause();
				JFrame ruleSettingframe = new JFrame("Set Rules");
				ruleSettingframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ruleSettingframe.setResizable(false);	
				Point p = framepanel.getLocationOnScreen();
				ruleSettingframe.setLocation(210+p.x, p.y);
			
				RuleSettingPanel ruleSetting = new RuleSettingPanel(ruleSettingframe);
				ruleSettingframe.getContentPane().add(ruleSetting);

				ruleSettingframe.pack();
				ruleSettingframe.setVisible(true);
			}
			
			//pause game and invoke levelsettingpanel to set levels
			if (event.getSource() == item22){
				if (playertimer.isRunning()) pause();
				JFrame levelSettingframe = new JFrame("Set Level");
				levelSettingframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				levelSettingframe.setResizable(false);
				Point p = framepanel.getLocationOnScreen();
				levelSettingframe.setLocation(210+p.x, 40+p.y);
			
				LevelSettingPanel levelSetting = new LevelSettingPanel(levelSettingframe);
				levelSettingframe.getContentPane().add(levelSetting);

				levelSettingframe.pack();
				levelSettingframe.setVisible(true);
			}
			
			//pause game and invoke keysettingpanel to set keys
			if (event.getSource() == item23){
				if (playertimer.isRunning())  pause();
				JFrame keySettingframe = new JFrame("Define Keys");
				keySettingframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Point p = framepanel.getLocationOnScreen();
				keySettingframe.setLocation(189+p.x, 120+p.y);
			
				KeySettingPanel keySetting = new KeySettingPanel(keySettingframe);
				keySettingframe.getContentPane().add(keySetting);

				keySettingframe.pack();
				keySettingframe.setVisible(true);
			}
			
			//pause game and invoke showsettingpanel to show settings.
			if (event.getSource() == item24){
				if (playertimer.isRunning()) pause();
				JFrame showSettingframe = new JFrame("Current Setting");
				showSettingframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Point p = framepanel.getLocationOnScreen();
				showSettingframe.setLocation(222+p.x, p.y);
			
				ShowSettingPanel showSetting = new ShowSettingPanel();
				showSettingframe.getContentPane().add(showSetting);

				showSettingframe.pack();
				showSettingframe.setVisible(true);
			}
			
			//restore default setting
			//pause game and showOptionDialog to confirm restore
			//if user choose restore, restore and restart game; if user choose cancle, unpause game
			if (event.getSource() == item25){
				if (playertimer.isRunning()) pause();
				Object[] options = { "RESTORE", "CANCEL" };
				int choice = JOptionPane.showOptionDialog(framepanel, "Restore to default setting?","Confirm",2,JOptionPane.QUESTION_MESSAGE,null,options, options[0]);
				if (choice == 0){
					restore();
					restart();
				}
				else
					pause();
			}
			
			//pause and show current game statistisc by invoke statiticspanel 
			if (event.getSource() == item31){
				if (playertimer.isRunning()) pause();
				JFrame statisticsframe = new JFrame("Statistics");
				statisticsframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				statisticsframe.setResizable(false);
				Point p = framepanel.getLocationOnScreen();
				statisticsframe.setLocation(215+p.x, p.y);
			
				pstatistics = player.getstatistics();
				cstatistics = computer.getstatistics();
				
				StatisticsPanel statistics = new StatisticsPanel(pstatistics, cstatistics, second);
				statisticsframe.getContentPane().add(statistics);

				statisticsframe.pack();
				statisticsframe.setVisible(true);
			}
		}

	}
	
	//***************************************************************************************
	//listen to four timers.
	//***************************************************************************************
	public class TimerListener implements ActionListener{
		
		public void actionPerformed (ActionEvent event){
			
			//time to move down the dropping brick in player panel
			if (event.getSource() == playertimer){
				player.pointpp();
				//move down the dropping brick and check if cause death
			    if (player.movedown() == 1){
			    	pause();
			    	gameover = 1;
			    }
			    //check the send cause it may cause send
			    for (;player.getsend() > 0;player.setsend(player.getsend()-1))computer.rowgenerate();
			    //reset dropping speed because the move may cause levelup
			    playertimer.setDelay((int)(1000/(player.getlevel()+1)));
			}
			
			//time to move down the dropping brick in computer panel
			else if (event.getSource() == computertimer){
				computer.pointpp();
			    if (computer.movedown() == 1){//movedown and check death
			    	pause();
			    	player.winplay();
			    	gameover = 1;
			    }
			    //check send and reset speed
			    for (;computer.getsend() > 0;computer.setsend(player.getsend()-1))player.rowgenerate();
			    computertimer.setDelay((int)(1000/(computer.getlevel()+1)));
			}
			
			//time to run AI(AI will take one move each time)
			else if (event.getSource() == AItimer)
				computer.AI();
			
			//one second passed.update time
			else{
				second++;
				if (second%2 == 0) player.setabpm();//update apm&bpm every two second.
			}
		}
	}
	
	//***************************************************************************************
	//keylistener, get command from keyboad then follow the command
	//***************************************************************************************
    private class KListener implements KeyListener{
		
		public void keyPressed (KeyEvent event){
			
			//count the amount of actions from user, for statistics
			if (gameover == 0) player.setaction(player.getaction()+1);
			
			//basic control,move dropping brick or pause/unpause game
			if (event.getKeyCode() == left)	if (playertimer.isRunning() == true) player.moveleft();
			if (event.getKeyCode() == right) if (playertimer.isRunning() == true) player.moveright();
			if (event.getKeyCode() == drop) if (playertimer.isRunning() == true) player.dropdown();
			if (event.getKeyCode() == rotate) if (playertimer.isRunning() == true) player.rotate();
			//this is different from other because the playertimer can also make dropping brick in
			//playerpanel move down, so if keyboardplay is put in the function movedown like others
			//playertimer can also cause keyboardplay,but I want keyboardplay only when the user try
			//to control the dropping brick in the player panel, so I put keyboardplay out side of
			//movedown function in player panel.
			if (event.getKeyCode() == down) if (playertimer.isRunning() == true){
				player.movedown();
				player.keyboardplay();
			}
			
			if (event.getKeyCode() == pause) pause();
			
			//hotkey combinations
			//ctrl+pageup or ctrl+pagedown: cheat
			//ctrl+r:restart
			//ctrl+p:pause/unpause game
			//ctrl+q:quit game
			//
			//realized by when get a key pressed, check if ctrl is pressed.
		    switch (event.getKeyCode()){
		    	case KeyEvent.VK_PAGE_UP:
		    		if (ctrlpressed == 1){
		    			player.cheat();
		    			if (playertimer.isRunning() == false){
		    				playertimer.start();
		    			    player.backmusicplay();
		    			}
		    		}
		    		break;
		    	case KeyEvent.VK_PAGE_DOWN:
		    		if (ctrlpressed == 1){
		    			player.rowdelete();
		    			if (playertimer.isRunning() == false){
		    				playertimer.start();
		    			    player.backmusicplay();
		    			}
		    		}
		    		break;
		    	case KeyEvent.VK_P:
		    		if (ctrlpressed == 1){
		    			pause();
		    		}
		    		break;
		    	case KeyEvent.VK_R:
		    		if (ctrlpressed == 1){
		    			restart();
		    		}
		    		break;
		    	case KeyEvent.VK_Q:
		    		if (ctrlpressed == 1){
		    			System.exit(0);
		    		}
		    		break;
		    	case KeyEvent.VK_CONTROL:
		    		ctrlpressed = 1;
		    		break;
		    	default:
		    }
		    //consider if cause sending row to computer after move
		    for (;player.getsend() > 0;player.setsend(player.getsend()-1))computer.rowgenerate();
		    //reset dropping speed because the move may cause levelup
		    playertimer.setDelay((int)(1000/(player.getlevel()+1)));
		}
        public void keyTyped (KeyEvent event){}
        public void keyReleased (KeyEvent event){
        	if (event.getKeyCode() == KeyEvent.VK_CONTROL)ctrlpressed = 0;
        }
	}
	
    //***************************************************************************************
    //pause/unpause game
    //***************************************************************************************
	public void pause () {
		if (gameover == 0){//if game is over, this wont work
			if (gamemode == 1){
				//pause vs computer game, stop four timers and backgroud music
				if (playertimer.isRunning()){
					playertimer.stop();
					computertimer.stop();
					AItimer.stop();
					player.backmusicstop();
					timetimer.stop();
				}
				//unpause vs computer game, start four timers and backgroud music
				else{
					playertimer.start();
					computertimer.start();
					AItimer.start();
					player.backmusicplay();
					timetimer.start();
				}
			}
			else{
				//pause vs single game, stop two timers and backgroud music
				//the other two timer about compute panel always keep stop in single mode
				if (playertimer.isRunning()){
					playertimer.stop();
					player.backmusicstop();
					timetimer.stop();
				}
				//unpause vs singel game, start two timers and backgroud music
				//the other two timer about compute panel always keep stop in single mode
				else{
					playertimer.start();
					player.backmusicplay();
					timetimer.start();
				}
			}
		}
	}
	
	//***************************************************************************************
	//start/restart game, initialize/reset some variables here
	//***************************************************************************************
	public void restart () {
		second = 0;//reset game time
		gameover = 0;//reset game stats
		//try to read control setting and AI level from file Configuration.txt
		try{
			FileReader fr = new FileReader(FILE);
			BufferedReader br = new BufferedReader(fr);
			String[] str = new String[42];
			for (int i=0;(str[i] = br.readLine()) != null;i++) {}
			br.close();
			left = Integer.parseInt(str[25]);
			right = Integer.parseInt(str[28]);
			down = Integer.parseInt(str[31]);
			drop = Integer.parseInt(str[34]);
			rotate = Integer.parseInt(str[37]);
			pause = Integer.parseInt(str[40]);
			//according to AI level,set the delay of aitimer
			int aidelay = 150;
			switch (Integer.parseInt(str[22])){
			    case 1:
		    	    aidelay = 500;
		    	    break;
		        case 2:
		    	    aidelay = 300;
		    	    break;
		        case 3:
		        	aidelay = 200;
		        	break;
		        case 4:
		        	aidelay = 150;
		        	break;
		        case 5:
		        	aidelay = 100;
		        	break;
		        case 6:
		        	aidelay = 80;
		        	break;
		        case 7:
		        	aidelay = 60;
		        	break;
		        case 8:
		        	aidelay = 40;
		        	break;
			    default:
			}
			AItimer.setDelay(aidelay);
		}
		catch (IOException exception){}
		//if Configuration.txt file is damaged, use default setting
		catch (java.lang.NumberFormatException exception){
			left = 37;
			right = 39;
			down = 40;
			drop = 10;
			rotate = 38;
			pause =32;
			AItimer.setDelay(300);
		}
		//start/restart game for single play
		if (gamemode == 0){
			player.restart();
		    player.backmusicplay();
		    playertimer.start();
		    computer.restart();
		    framepanel.removeAll();
		    framepanel.add(player);
		    Tetris.frame.pack();
		    //super.setSize(500, 500);
		    repaint();		    
		    //reset computer statistics table
		    //stop the computer panel and make droppingbrick as same color as background
		    //so it look not exist, and stop computertimer and Ai timer.
		    //so everything in computer panel wont change for ever.
		    computer.stop();
		    computertimer.stop();
			AItimer.stop();
			timetimer.start();//start count game time.
		}
		//start/restart game for vs computer
		else{
			framepanel.add(computer);
			Tetris.frame.pack();
		    player.restart();
		    computer.restart();
		    player.backmusicplay();
		    playertimer.start();
			computertimer.start();
			AItimer.start();
			timetimer.start();//start count game time.
			repaint();
		}
	}
	
	//***************************************************************************************
	//write default setting to file Configuration.txt
	//***************************************************************************************
	public void restore () {	
		//write to string array first, each string hold a line
		//then write string to file
		String[] str = new String[42];
		str[0] = "lines to send";
		str[1] = Integer.toString(-1);
		str[2] = "";
		str[3] = "points per level";
		str[4] = Integer.toString(10000);
		str[5] = "";
		str[6] = "levelup bonus";
		str[7] = Integer.toString(0);
		str[8] = "-----------------------";
		str[9] = "player start rows";
		str[10] = Integer.toString(0);
		str[11] = "";
		str[12] = "player start level";
		str[13] = Integer.toString(0);
		str[14] = "";
		str[15] = "computer start rows";
		str[16] = Integer.toString(0);
		str[17] = "";
		str[18] = "computer start level";
		str[19] = Integer.toString(0);
		str[20] = "";
		str[21] = "computer AI level";
		str[22] = Integer.toString(4);
		str[23] = "-----------------------";
		str[24] = "move left";
		str[25] = Integer.toString(37);
		str[26] = "";
		str[27] = "move right";
		str[28] = Integer.toString(39);
		str[29] = "";
		str[30] = "move down";
		str[31] = Integer.toString(40);
		str[32] = "";
		str[33] = "drop brick";
		str[34] = Integer.toString(10);
		str[35] = "";
		str[36] = "rotate brick";
		str[37] = Integer.toString(38);
		str[38] = "";
		str[39] = "pause";
		str[40] = Integer.toString(32);
		try{
			FileWriter fw = new FileWriter(FILE);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw =new PrintWriter(bw);
			for (int i=0;i < 41;i++)pw.println(str[i]);
			pw.close();
		}
		catch (IOException e){}
	}
}