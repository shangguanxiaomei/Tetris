package shape;
//***************************************************************************************
//Block.java                                   Author:  Beibei Zhang
//
//Block is the element of the 20*10 matrix which record the status of game panel: which
//block is filled, and the color of the block. its xposition and yposition are the value 
//pixels.
//
//invoked by humanpanel
//***************************************************************************************
import java.awt.*;

public class Block {

	//four parameters of block
	private int xposition, yposition;
	private Color color;
	private int fill;
	
	//normal method: constructor, set, get, reset
	public Block(int x, int y, Color ys, int fill){
		xposition = x;
		yposition = y;
		color = ys;
		this.fill = fill;
	} 
    
	public void blockReset(Color ys, int fill){
		color = ys;
		this.fill = fill;
	}
	
	public int getXposition(){
		return xposition;
	}
	
	public int getYposition(){
		return yposition;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getFill(){
		return fill;
	}
	
	public void setFill(int fill){
		this.fill = fill;
	}
}
