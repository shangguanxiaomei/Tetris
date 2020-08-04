package frame;
//***************************************************************************************
//ComputerPanel.java                                   Author:  Beibei Zhang
//
//the panal is described by a 20*10 matrix, each element recode the status of every block
//then plotted on the panel.
//dropping brick is controlled by AI here,and settled in the container.
//
//right after each dropping brick is generated, all kinds of possible move order is tested
//and graded, and the best move order is selected.
//each time AI is invoke, it move the dropping brick one step according to the move order.
//***************************************************************************************

import java.awt.*;
import java.io.*;
import javax.swing.*;
import shape.*;

public class ComputerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	//unique variables of this panel
	private Block[][] trygraph;//copy of the graph,AI test move here
	private MoveOrder[] moveorder;//all kinds of moveorder, horizontal move, vertical move, and the grade
	private Brick trydroppingbrick;//copy of the dropping brick
	private int grade;//grade for one moveorder
	private int superAI;//0:AI move the dropping brick down; 1: AI drop the dropping brick down
    //player panel has the same following variables
	private Block[][] graph;
	private Brick droppingbrick;
	private int[] statistics;//statistic information, for detail see statisticpanel
	private int action, brick;// total action, and total brick AI did, statistic information
	private int forecast;//shape and color of forecastbrick
	private int point;//AI score
	private int pointperlevel;//point needed to level up
	private int startlevel;//initial level
	private int startrow;//initial rows
	private int level;//current level
	private int levelupbonus;//how many unfilled row will be disposed when levelup
	private int next;//status of the game, is the d
	private int dead;//status of the game.
	private int send;//status of the game.
	private int sendfix;//setting
	private final int ROW = 20; 
	private final int COL = 10;
	private final int BLOCK_SIZE = 31;//block size
	private final int SIZE = 33;//block occupied area.
	private final String FILE = "Configuration.txt";
	
	//***************************************************************************************
	//constructor, initialize the computer panel
	//***************************************************************************************
	public ComputerPanel () {
		
		droppingbrick = new Brick(7);
		graph = new Block[COL][ROW];
		trygraph = new Block[COL][ROW];
		for (int col = 0; col < graph.length; col++){
			for (int row = 0; row < graph[col].length; row++){
				graph[col][row] = new Block(10+SIZE*col,10+SIZE*row, Color.white, 0);
				trygraph[col][row] = new Block(10+SIZE*col,10+SIZE*row, Color.white, 0);
			}
		}

		moveorder = new MoveOrder[41];
		for (int i=0;i < 41;i++)	moveorder[i] = new MoveOrder();
	
		setPreferredSize(new Dimension(350, 670));
		setBackground(MainPanel.backgroundcolor);		
		restart();//initialize the status
	}
	
	//***************************************************************************************
	//paint the panel, player panel has the exactly same method
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
		
		//paint the dropping brick
		page.setColor(droppingbrick.getColor());
		page.fillRect(graph[droppingbrick.get0x()][droppingbrick.get0y()].getXposition(), graph[droppingbrick.get0x()][droppingbrick.get0y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);
		page.fillRect(graph[droppingbrick.get1x()][droppingbrick.get1y()].getXposition(), graph[droppingbrick.get1x()][droppingbrick.get1y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);
		page.fillRect(graph[droppingbrick.get2x()][droppingbrick.get2y()].getXposition(), graph[droppingbrick.get2x()][droppingbrick.get2y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);
		page.fillRect(graph[droppingbrick.get3x()][droppingbrick.get3y()].getXposition(), graph[droppingbrick.get3x()][droppingbrick.get3y()].getYposition(),BLOCK_SIZE,BLOCK_SIZE);	
	}
	
	//***************************************************************************************
	//start/restart computer panel reset status
	//***************************************************************************************
	public void restart () {		
		clean();//reset container.
		point = 0;
		send = 0;
		dead = 0;
		action = 0;
		brick = 0;
		//set/reset statistics array
		statistics = new int[7];
		for (int i = 0;i < 7;i++)	statistics[i] = 0;
		//try to read level setting from Configeration.txt file
		try{
			FileReader fr = new FileReader(FILE);
			BufferedReader br = new BufferedReader(fr);
			String[] str = new String[42];
			for(int i=0;(str[i] = br.readLine()) != null;i++) {}
			br.close();
			sendfix = Integer.parseInt(str[1]);
			pointperlevel = Integer.parseInt(str[4]);
			levelupbonus = Integer.parseInt(str[7]);
			startrow = Integer.parseInt(str[16]);
			startlevel = Integer.parseInt(str[19]);
			//set AImode
			if(Integer.parseInt(str[22]) < 6)
				superAI = 0;
			else
				superAI = 1;
		}
		catch (IOException exception){}
		//if failed to read, use default setting
		catch (java.lang.NumberFormatException exception){
			sendfix = -1;
			pointperlevel = 10000;
			levelupbonus = 0;
			startrow = 0;
			startlevel = 0;
			superAI = 0;
		}
		level = startlevel;
		droppingbrick = new Brick((int) (Math.random()*7));
		for (int i = 0;i < startrow;i++)rowgenerate();		
		setmoveorder();
	}
	
	//***************************************************************************************
	// move the dropping brick down check if is at bottom 
	// invoked by computer timer from mainpanel
	//***************************************************************************************
	public int movedown () {	    
		next = droppingbrick.moveDown(graph);
		repaint();
		if (next == 1) refresh();
		return dead;
	}
	
	//***************************************************************************************
	//point++, used for each time when computertimer in mainpanel triggered
	//current point plus one
	//***************************************************************************************
	public void pointpp () {
		point++;
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
		if (level - startlevel < (int) point/pointperlevel)	levelup();//judge if level up
		droppingbrick = new Brick(forecast);//new dropping brick
		brick++;//update statistics information
		//check death -- if new dropping brick overlap the current block
		if (graph[droppingbrick.get1x()][droppingbrick.get1y()].getFill() == 1)	dead = 1;
		forecast = (int) (Math.random()*7);//new forecast brick
        setmoveorder();
        next = 0;
		repaint();
	}
	
	//***************************************************************************************
	//player panel has the exactly same method
	//check if any row is fullfilled so that it can be disposed
	//if disposed several row, give the point bonus, calculate the send, record information 
	//in the statistics table
	//***************************************************************************************
	private int judge () {
		boolean fullfill;
		int points = 0;
		send = 0;
		for	(int row = ROW-1; row > -1; row--){//check row by row
			fullfill = true;
			for	(int col = 0; col < COL; col++){
				if (graph[col][row].getFill() == 0){
					fullfill = false;
					break;
				}
			}
			if (fullfill){
				//delete the fullfilled row
				for	(int row2 = row; row2 > 0; row2--){
					for	(int col = 0; col < COL; col++){
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
		return points;		
	}
	
	//***************************************************************************************
	//player panel has the exactly same method
	//generate a row from bottom, because of opponent's send
	//***************************************************************************************
	public void rowgenerate () throws java.lang.StackOverflowError{
		
		//check if the dropping brick is on the way, if it is settle it first, then generate a row
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
	private void rowdelete () {
		for (int col = 0; col < COL; col++){
			for (int row = ROW-1; row > 0; row--){
				graph[col][row].blockReset(graph[col][row-1].getColor(), graph[col][row-1].getFill());
			}
				graph[col][0].blockReset(Color.white, 0);
		}
	}
	
	//***************************************************************************************
	//player panel has the exactly same method
	//when level, level++ and give the level bonus
	//***************************************************************************************
	private void levelup () {
		level++;
		for (int i = 0;i < levelupbonus;i++)rowdelete();
	}
	
	//***************************************************************************************
	//player panel has the exactly same method
	//clean the container
	//***************************************************************************************
	private void clean () {
	    for (int col = 0; col < COL; col++){
			for (int row = 0; row < ROW; row++){
				graph[col][row].blockReset(Color.white, 0);
			}
		}
	}
	
	//***************************************************************************************
	//move the brick one step each time get invoked
	//invoke by AI timer from main panel
	//the way to move is stored in moveorder[0],including one down move , then horizontal move
	//and rotate move, if these move are done , move the dropping brick down
	//***************************************************************************************
	public void AI () {
		//move down first, to make every dropping brick can rotate
		if (moveorder[0].getdown() == 1){
			droppingbrick.moveDown(graph);
			moveorder[0].setdown(0);
		}
		//then rotate, because sometimes dropping brick can not rotate at edge
		else{
			if (moveorder[0].getrotate() > 0){
				boolean boo = droppingbrick.rotate(graph);
				if (boo)moveorder[0].setrotate(moveorder[0].getrotate()-1);
			}
			//then move left or right positive number means right, negative means left
			else{
				if (moveorder[0].getright() != 0){
					if (moveorder[0].getright() > 0){
						boolean boo =droppingbrick.moveRight(graph);
						if (boo)moveorder[0].setright(moveorder[0].getright()-1);
					}
					else{
						boolean boo =droppingbrick.moveLeft(graph);
						if (boo)moveorder[0].setright(moveorder[0].getright()+1);
					}
				}
				//if all done, move down , check if it is at bottom.
				//if it is, call refresh() to settle it...
				else{ 
					if (superAI == 0){
						next = droppingbrick.moveDown(graph);
						if (next == 1) refresh();
					}		
					else{	
						for (;droppingbrick.moveDown(graph) == 0;){}
						refresh();
					}
				}
			}
		}
		action++;
		repaint();
    }
	
	//***************************************************************************************
	//set moveorder ,invoked immediatly after new dropping brick is generated.
	//***************************************************************************************
	public void setmoveorder () {
		
		//try all kinds of defferent move schedule, give them grade
		//then choose the best one
		for (int rotate = 0;rotate < 4;rotate++)
			for (int right = -4;right < 6;right++){
				moveorder[rotate*10+right+5].setrotate(rotate);
				moveorder[rotate*10+right+5].setright(right);
				trymove(graph,droppingbrick,right,rotate);//simulate move in duplicated container
				moveorder[rotate*10+right+5].setgrade(evaluate());//grade the simulated move
			}
		//choose the best move schedule and store it in moveorder[0].
		moveorder[0] = moveorder[1]; 
		for (int i = 2;i < 41;i++){
			//higher grade win
			if (moveorder[0].getgrade() < moveorder[i].getgrade()){
				moveorder[0] = moveorder[i]; 
			}
			//if grades are same, less step win.
			else if (moveorder[0].getgrade() == moveorder[i].getgrade() && Math.abs(moveorder[0].getright()) < Math.abs(moveorder[1].getright()) && moveorder[0].getrotate() >=moveorder[i].getrotate()){
				moveorder[0] = moveorder[i]; 
			}
		}
//		System.out.println("brick:" + droppingbrick.getShape());
//		System.out.println("rotate:" + moveorder[0].getrotate());
//		System.out.println("right:" + moveorder[0].getright());
//		System.out.println("                                            ");
	}
	
	//***************************************************************************************
	//simulate move duplicated dropping brick in the duplicated container
	//with the same order set in moveorer:
	//move down once, rotate, move horizontally, and move down 
	//finally settle
	//***************************************************************************************
	public void trymove (Block[][] graph, Brick droppingbrick, int right, int rotate) {
		//copy container to duplicate container
		for (int col = 0; col < COL; col++){
			for (int row = 0; row < ROW; row++){
				trygraph[col][row].blockReset(graph[col][row].getColor(), graph[col][row].getFill());
			}
		}
		//copy dropping brick
		trydroppingbrick = new Brick(droppingbrick.getShape());
		//move with the scheduled setting
		trydroppingbrick.moveDown(trygraph);
		for (;rotate > 0;rotate--)trydroppingbrick.rotate(trygraph);
		if (right < 0)
			for (;right < 0;right++)trydroppingbrick.moveLeft(trygraph);
		else
			for (;right > 0;right--)trydroppingbrick.moveRight(trygraph);
		for (;trydroppingbrick.moveDown(trygraph) == 0;){}
		//settle
		trygraph[trydroppingbrick.get0x()][trydroppingbrick.get0y()].blockReset(trydroppingbrick.getColor(), 1);
		trygraph[trydroppingbrick.get1x()][trydroppingbrick.get1y()].blockReset(trydroppingbrick.getColor(), 1);
		trygraph[trydroppingbrick.get2x()][trydroppingbrick.get2y()].blockReset(trydroppingbrick.getColor(), 1);
		trygraph[trydroppingbrick.get3x()][trydroppingbrick.get3y()].blockReset(trydroppingbrick.getColor(), 1);
	}
	
	//***************************************************************************************
	//grade the moveorder, most difficult part
	//***************************************************************************************
	private int evaluate () {
		// judge the trygraph first, see how many rows can be disposed
		//and get the new trygraph afte dispose, similar to method judge
		boolean fullfill;
		grade = 0;
		for (int row = ROW-1; row > -1; row--){
			fullfill = true;
			for (int col = 0; col < COL; col++){
				if (trygraph[col][row].getFill() == 0){
					fullfill = false;
					break;
				}
			}
			if (fullfill){
				for (int row2 = row; row2 > 0; row2--){
					for (int col = 0; col < COL; col++){
						trygraph[col][row2].blockReset(trygraph[col][row2-1].getColor(), trygraph[col][row2-1].getFill());
						trygraph[col][0].blockReset(Color.white, 0);
					}
				}
				grade = grade*2+100;
				row++;
			}
		}
		
		//find the surface of settled block
		//if the surface is too high(surface[*] is small), that mean danger.
		int[] surface = new int[COL];
		int high = 0;
		for (int col = 0; col < COL; col++){
			surface[col] = ROW-1;
			for (int row = 0; row < ROW; row++){
	            if (trygraph[col][row].getFill() == 1){
	            	surface[col] = row-1;
	            	if (surface[col] < 10 ) high = 1;
	            	break;
	            }
			}
		}
		// if surface is low, prefer dispose more than one row once
		// otherwise, prefer dispose row if possible
		if (grade > 0 & high == 0) grade = (grade - 200)*2;
		
		//perfer settle the brick to the left
		//to make the surface is higher on the left, and lower on the right
		for(int col = 0; col < COL; col++){
			for(int row = 0; row < ROW; row++){
				grade -= trygraph[col][row].getFill()*col;
			}
		}
		
		//prefer there is no peak or gap in the surface
		for (int col = 1;col < COL-1;col++){
			if (surface[col]-surface[col-1] > 1 && surface[col]-surface[col+1] > 1)grade += (surface[col-1]+surface[col+1]-surface[col]*2)*30;
			if (surface[col-1]-surface[col] > 1 && surface[col+1]-surface[col] > 1)grade += (surface[col]*2-surface[col-1]-surface[col+1])*20;
		}
		//edge
		if (Math.abs(surface[0]-surface[1]) > 1)grade -= (surface[0]-surface[1])*(surface[0]-surface[1])*10;
		if (Math.abs(surface[9]-surface[8]) > 1)grade -= (surface[9]-surface[8])*(surface[9]-surface[8])*10;
		//prefer there is no one side peak
		for (int col = 1;col < COL;col++){
			if (Math.abs(surface[col]-surface[col-1]) > 1)grade -=  (surface[col]-surface[col-1])*(surface[col]-surface[col-1])*3;
		}
		for (int col = 0;col < COL;col++){
			//prefer there is no hole.
			for (int row = surface[col]+2;row < ROW;row++)if(trygraph[col][row].getFill() == 0)grade -= 400+(row-surface[col])*20;
			//prefer the surface is not too high
			if (surface[col] < 8)grade -= (8 - surface[col])*(8 - surface[col])*15;
		}
		return grade;		
	}
	//***************************************************************************************
	//reset the status when stop
	//***************************************************************************************
	public void stop () {
		clean();
		level = startlevel;
		point = 0;
		send = 0;
		dead = 0;
		droppingbrick = new Brick(7);
		repaint();
	}
	
	//***************************************************************************************
	//normal set, get function,
	//used by main panel
	//***************************************************************************************
	public int getlevel () {
		return level;
	}
	
	public void setsend (int send) {
		this.send = send;
	}
	
	public int getsend () {
		return send;
	}
	
	public int[] getstatistics (){
		statistics[0] = action;
		statistics[1] = brick;
		return statistics;
	}
	
	//***************************************************************************************
	//moveorder class, store the the schedule of a move order, and the grade of it
	//***************************************************************************************
	public class MoveOrder{
		
		private int right;
		private int rotate;
		private int down;
		private int grade;
		
		public MoveOrder () {
			right = 0;
			rotate = 0;
			grade = 0;
			down = 1;
		}
		public void setright (int right) {
			this.right = right;
		}
		
		public int getright () {
			return right;
		}
		
		public void setrotate (int rotate) {
			this.rotate = rotate;
		}
		
		public int getrotate () {
			return rotate;
		}
		
		public void setdown (int down) {
			this.down = down;
		}
		
		public int getdown () {
			return down;
		}
		
		public void setgrade (int grade) {
			this.grade = grade;
		}
		
		public int getgrade () {
			return grade;
		}
	}
}