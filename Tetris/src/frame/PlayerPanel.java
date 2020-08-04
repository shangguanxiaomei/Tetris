package frame;
//***************************************************************************************
//PlayerPanel.java                                   Author:  Beibei Zhang
//
//the panal is described by a 20*10 matrix, each element recode the status of every block
//then plotted on the panel.
//dropping brick is controlled by the keyboard and get command from mainpanel, 
//then settled on the 20*10 panel.
//
//inclueding player part and middle information part.
//***************************************************************************************

import java.awt.*;
import java.io.*;
import javax.swing.*;
import shape.*;
import frame.Music;

public class PlayerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//unique variables of this panel  
	private InformationPanel information;
	private int apm, bpm, second;//statistic information
	//computer panel has the same following variables
	private Block[][] graph;
	private Brick droppingbrick;
	private int[] statistics;//statistic information, for detail see statisticpanel
	private int action, brick;// total action, and total brick user did, statistic information
	private int forecast;//shape and color of forecastbrick
	private int point; //user score
	private int pointperlevel;//point needed to level up
	private int startlevel;//initial level
	private int startrow;//initial rows
	private int level;//current level
	private int levelupbonus;//how many unfilled row will be disposed when levelup
	private int next;//status of game, is the droppingbrick at botton?
	private int dead;//status of game
	private int send;//status of game 
	private int sendfix;//setting 
	private final int ROW = 20; 
	private final int COL = 10;
	private final int BLOCK_SIZE = 31;//block size
	private final int SIZE = 33;//block ocuppied area.
	private final String FILE = "Configuration.txt";
	
	//***************************************************************************************
	//constructor, initialize the player panel
	//load music.
	//***************************************************************************************
	public PlayerPanel () {
		
		droppingbrick = new Brick(7);//initial value, user wont see it 
		graph = new Block[COL][ROW];
		for (int col = 0; col < graph.length; col++){
			for (int row = 0; row < graph[col].length; row++){
				graph[col][row] = new Block(10+SIZE*col,10+SIZE*row, Color.white, 0);
			}
		}
		
//		try {
//			keyboard = JApplet.newAudioClip(new URL("file", "localhost","sound/keyboard.wav"));
//			cleanrow = JApplet.newAudioClip(new URL("file", "localhost","sound/cleanrow.wav"));
//			win = JApplet.newAudioClip(new URL("file", "localhost","sound/win.wav"));
//			die = JApplet.newAudioClip(new URL("file", "localhost","sound/die.wav"));
//			settle = JApplet.newAudioClip(new URL("file", "localhost","sound/settle.wav"));
//		} 
//		catch (Exception exception) {}
		
		//add information panel to the right
		information = new InformationPanel();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createRigidArea(new Dimension(350,0)));
		add(information);	
		setPreferredSize(new Dimension(580, 670));
		setBackground(MainPanel.backgroundcolor);
		
		restart();//initialize the status
	}
	//***************************************************************************************
	//paint the panel, computer panel has the exactly same method
	//***************************************************************************************
	public void paintComponent (Graphics page) {
		
		super.paintComponent(page);
		
		//paint the container
		for	(int col = 0; col < graph.length; col++){
			for	(int row = 0; row < graph[col].length; row++){
				page.setColor(graph[col][row].getColor());
				page.fillRect(graph[col][row].getXposition(), graph[col][row].getYposition(),BLOCK_SIZE, BLOCK_SIZE);
				}
		}
		
		//paint the droppingbrick
		page.setColor(droppingbrick.getColor());
		page.fillRect(graph[droppingbrick.get0x()][droppingbrick.get0y()].getXposition(), graph[droppingbrick.get0x()][droppingbrick.get0y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);
		page.fillRect(graph[droppingbrick.get1x()][droppingbrick.get1y()].getXposition(), graph[droppingbrick.get1x()][droppingbrick.get1y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);
		page.fillRect(graph[droppingbrick.get2x()][droppingbrick.get2y()].getXposition(), graph[droppingbrick.get2x()][droppingbrick.get2y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);
		page.fillRect(graph[droppingbrick.get3x()][droppingbrick.get3y()].getXposition(), graph[droppingbrick.get3x()][droppingbrick.get3y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);	
	}
	
	//***************************************************************************************
	//start/restart player panel, reset the status
	//***************************************************************************************
	public void restart () {		
		clean();//reset container
		point = 0;
		send = 0;
		dead = 0;
		apm = 0;
		action = 0;
		brick = 0;
		second = 0;
		//set/reset statistics array.
		statistics = new int[7];
		for (int i = 0;i < 7;i++)	statistics[i] = 0;
		//try to read level setting from Configeration.txt file
		try{
			FileReader fr = new FileReader(FILE);
			BufferedReader br = new BufferedReader(fr);
			String[] str = new String[42];
			for (int i=0;(str[i] = br.readLine()) != null;i++) {}
			br.close();
			sendfix = Integer.parseInt(str[1]);
			pointperlevel = Integer.parseInt(str[4]);
			levelupbonus = Integer.parseInt(str[7]);
			startrow = Integer.parseInt(str[10]);
			startlevel = Integer.parseInt(str[13]);
		}
		catch (IOException exception){}
		//if failed to read, use default setting
		catch (java.lang.NumberFormatException exception){
			sendfix = -1;
			pointperlevel = 10000;
			levelupbonus = 0;
			startrow = 0;
			startlevel = 0;
		}
		level = startlevel;
		//set/reset information panel
		information.setpointLabel("0");
		information.setlevelLabel(Integer.toString(level));
		information.setapmLabel("0");
		information.setbpmLabel("0");
		droppingbrick = new Brick((int) (Math.random()*7));
		for (int i = 0;i < startrow;i++)rowgenerate();		
		forecast = (int) (Math.random()*7);
		information.setforcast(forecast);
		repaint();
	}
	
	//***************************************************************************************
	//move the dropping brick, play the music and repaint
	//if move downwards, check if it at bottom, if it is, settle dropping brick 
	//judge if some line can be disposed, and if the move cause death
	//***************************************************************************************
	public void moveleft () {
		Music.keyboardMusic();
		droppingbrick.moveLeft(graph);
		repaint();
	}
	
	public void moveright () {	    
		Music.keyboardMusic();
		droppingbrick.moveRight(graph);
		repaint();
	}

	public int movedown () {	    
		next = droppingbrick.moveDown(graph);//move down and check position
		repaint();
		if (next == 1)refresh();//judge,
		return dead;
	}
	
	public int dropdown () {	    
		for(;droppingbrick.moveDown(graph) == 0;){}//keep movedown to make it at bottom
		Music.keyboardMusic();
		repaint();
		refresh();//judge
		return dead;
	}

	public void rotate () {	 
		Music.keyboardMusic();
		droppingbrick.rotate(graph);
		repaint();
	}

	//***************************************************************************************
	//delete all settled block in the container
	//***************************************************************************************
	public void cheat () {		
		clean();
		Music.keyboardMusic();
		repaint();
	}
	
	//***************************************************************************************
	//point++ and refresh pointlabel in the information label
	//used for each time when playertimer in mainpanel triggered
	//current point plus one
	//***************************************************************************************
	public void pointpp () {
		point++;
		information.setpointLabel(Integer.toString(point));
	}
	
	//***************************************************************************************
	//settle the dropping brick, invoked only when the dropping brick is at bottom of container.
	//and judge if some row is fullfilled, generate new droping brick, check death
	//***************************************************************************************
	public void refresh () {		
		
		//settle the dropping brick
		graph[droppingbrick.get0x()][droppingbrick.get0y()].blockReset(droppingbrick.getColor(), 1);
		graph[droppingbrick.get1x()][droppingbrick.get1y()].blockReset(droppingbrick.getColor(), 1);
		graph[droppingbrick.get2x()][droppingbrick.get2y()].blockReset(droppingbrick.getColor(), 1);
		graph[droppingbrick.get3x()][droppingbrick.get3y()].blockReset(droppingbrick.getColor(), 1);
		point += judge();//judge and pointup
		information.setpointLabel(Integer.toString(point));//update information panel
		if (level - startlevel < (int) point/pointperlevel){//judge if level up
			levelup();
			information.setlevelLabel(Integer.toString(level));//update information panel
		}
		droppingbrick = new Brick(forecast);//generate new droppingbrick
		brick++;//update statistics information
		//check death -- if new dropping brick overlap the current block 
		if (graph[droppingbrick.get1x()][droppingbrick.get1y()].getFill() == 1){
			Music.dieMusic();
			dead = 1;
		}
		forecast = (int) (Math.random()*7);//forcastbrick
		information.setforcast(forecast);
		next = 0;
		repaint();
	}
	
	//***************************************************************************************
	//computer panel has the exactly same method
	//check if any row is fullfilled so that it can be disposed
	//if disposed several row, give the point bonus, calculate the send, record information 
	//in the statistics table
	//***************************************************************************************
	private int judge () {
		boolean fullfill;
		int points = 0;
		send =  0;
		for (int row = ROW-1; row > -1; row--){//check row by row
			fullfill = true;
			for (int col = 0; col < COL; col++){
				if (graph[col][row].getFill() == 0){
					fullfill = false;
					break;
				}
			}
			if (fullfill){
				//delete the fullfilled row
				for (int row2 = row; row2 > 0; row2--){
					for (int col = 0; col < COL; col++){
						graph[col][row2].blockReset(graph[col][row2-1].getColor(), graph[col][row2-1].getFill());
						graph[col][0].blockReset(Color.white, 0);
					}
				}
				points = points*2+100;//update points bonus,it will be (send^2-1)*100
				row++;//make the loop check the same row again because it a new row
				send++;//count the rows disposed
			}
		}
		//record in statistics array
		switch (send){
			case 1:
				statistics[2]++;
				break;
			case 2:
				statistics[3]++;
				break;
			case 3:
				statistics[4]++;
				break;
			case 4:
				statistics[5]++;
				break;
			default:
		}
		if (send != 0){	
			send += sendfix;//fix send according to user's setting
			statistics[6] += send;//recorde statistics information
		}
		if (points == 0)
			Music.settleMusic();
		else
			Music.cleanrowMusic();
		return points;		
	}
	
	//***************************************************************************************
	//computer panel has the exactly same method
	//generate a row from bottom, because of opponent's send
	//***************************************************************************************
	public void rowgenerate () throws java.lang.StackOverflowError{
		//chech if the dropping brick is on the way, if it is settle it first, then generate a row
		if (droppingbrick.trymoveDown(graph) == 1){
			refresh();
			rowgenerate();
		}
		//randomly generate a row from bottom
		else{
			for (int col = 0; col < COL; col++){
				for (int row = 0; row < ROW-1; row++)	graph[col][row].blockReset(graph[col][row+1].getColor(), graph[col][row+1].getFill());
				if ((int) (Math.random()*2) == 1)
					graph[col][ROW-1].blockReset(Color.blue, 1);
				else
					graph[col][ROW-1].blockReset(Color.white, 0);
			}
		}
	}
	
	//***************************************************************************************
	//delete a row from botton, probably because of levelup bonus
	//***************************************************************************************
	public void rowdelete (){
		for (int col = 0; col < COL; col++){
			for (int row = ROW-1; row > 0; row--){
				graph[col][row].blockReset(graph[col][row-1].getColor(), graph[col][row-1].getFill());
			}
				graph[col][0].blockReset(Color.white, 0);
		}
		Music.cleanrowMusic();
	}
	
	//***************************************************************************************
	//computer panel has the exactly same method
	//when level, level++ and give the level bonus
	//***************************************************************************************
	private void levelup (){
		level++;
		for (int i = 0;i < levelupbonus;i++) rowdelete();
	}
	
	//***************************************************************************************
	//computer panel has the exactly same method
	//clean the container
	//***************************************************************************************
	private void clean (){
	    for (int col = 0; col < COL; col++){
			for (int row = 0; row < ROW; row++)	graph[col][row].blockReset(Color.white, 0);
		}
	}
	
	//***************************************************************************************
	//control the music, play, stop...
	//used by main panel
	//***************************************************************************************
	public void backmusicstop (){
		Music.stopBackMusic();
	}
	
	public void backmusicplay (){
		Music.playBackMusic(InformationPanel.backmusicindex);
	}
	public void keyboardplay (){
		Music.keyboardMusic();
	}
	
	public void winplay (){
		Music.winMusic();
	}
	
	//***************************************************************************************
	//normal set, get function,
	//used by main panel
	//***************************************************************************************
	public int getlevel (){
		return level;
	}
	
	public void setsend (int send){
		this.send = send;
	}
	
	public int getsend (){
		return send;
	}
	
	public void setaction (int action){
		this.action = action;
	}
	
	public int getaction (){
		return action;
	}
	
	public int[] getstatistics (){
		statistics[0] = action;
		statistics[1] = brick;
		return statistics;
	}
	
	//***************************************************************************************
	//caller by timetimer from main panel
	//update apm/bpm value every two second
	//***************************************************************************************
	public void setabpm (){
		second += 2;
		apm = (int) 60*action/second;
		bpm = (int) 60*brick/second;
		information.setapmLabel(Integer.toString(apm));
		information.setbpmLabel(Integer.toString(bpm));
	}
}
	
	