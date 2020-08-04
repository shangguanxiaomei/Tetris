package shape;
//***************************************************************************************
//Spot.java                                   Author:  Beibei Zhang
//
//spot describe each block of the dropping brick:
//its position and color.
//its xposition and yposition are the positions in the matrix.
//
//invoked by Brick
//***************************************************************************************
import java.awt.Color;

public class Spot {
	
	public int xposition, yposition;
	public Color color;
	
	public Spot(int x, int y, Color color){
		xposition = x;
		yposition = y;
		this.color = color;
	}
	
	public void Resetxy(int x, int y){
		xposition = x;
		yposition = y;
	}
	
	public void setx(int x){
		xposition = x;
	}
	
	public void sety(int y){
		yposition = y;
	}
	
	public int getx(){
		return xposition;
	}
	
	public int gety(){
		return yposition;
	}
	
	public Color getColor(){
		return color;
	}
}
