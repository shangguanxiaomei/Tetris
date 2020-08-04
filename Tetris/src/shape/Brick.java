package shape;
//***************************************************************************************
//Brick.java                                   Author:  Beibei Zhang
//
//describ the brick, which consist of four spots, and ite position at 20*10 matrix, its color, shape and the status
//of rotation
//int shape detemine its shape and color
//shape:0=I 1=O 2=T 3=Z 4=S 5=J 6=L 7=initial(for start) 8=initial(for start)
//color:0=(255,0,0) 1=(150,0,150) 2=(128,64,10) 3=(0,255,0)
//4=(0,150,150) 5=(0,0,255) 6=(0,0,0) 7=(255,255,255) 8=(192,192,192)
//
//point[0-3] are 4 blocks of the dropping brick, 
//always let point[0] be leftmost one, point[3] be rightmost one,
//and point[2] be the lowmost one(if posible).
//point[1] is the center one(others rotate around it)
//
//also provide the mothod to control the dropping brick:
//moveleft, moveright, movedown,rotate.
//
//invoked by humanpanel
//***************************************************************************************
import java.awt.Color;

public class Brick {
	
	private Spot[] point = new Spot[4];
    int shape; 
    private Color color;
	private final int ROW = 20;//ydimention of brick container 
	private final int COL = 10;//xdimention of brick container
    private final Color COLOR0 = new Color(255,0,0);
    private final Color COLOR1 = new Color(150,0,150);
    private final Color COLOR2 = new Color(255,153,51);
    private final Color COLOR3 = new Color(0,204,204);
    private final Color COLOR4 = new Color(0,150,150);
    private final Color COLOR5 = new Color(0,0,255);
    private final Color COLOR6 = new Color(0,0,0);
//    private final Color COLOR7 = new Color(255,255,255);
//    private final Color COLOR8 = new Color(192,192,192);
    
    //***************************************************************************************
    //constructor: according to the int shape, initialize the brick's spots position and color
    //in the brick container.
    //***************************************************************************************
    public Brick(int num){
		
    	shape = num;
		
    	switch (shape){
		    case 0:
		    	color = COLOR0;
		        point[0] = new Spot(3,0,color);
		        point[1] = new Spot(4,0,color);
		        point[2] = new Spot(5,0,color);
		        point[3] = new Spot(6,0,color);
		        break;
		    case 1:
		    	color = COLOR1; 
		        point[0] = new Spot(4,0,color);
		        point[1] = new Spot(4,1,color);
		        point[2] = new Spot(5,1,color);
		        point[3] = new Spot(5,0,color);
		        break;
		    case 2:
		    	color = COLOR2;
		        point[0] = new Spot(3,0,color);
		        point[1] = new Spot(4,0,color);
		        point[2] = new Spot(4,1,color);
		        point[3] = new Spot(5,0,color);
		        break;
		    case 3:
		    	color = COLOR3;
		        point[0] = new Spot(3,0,color);
		        point[1] = new Spot(4,1,color);
		        point[2] = new Spot(4,0,color);
		        point[3] = new Spot(5,1,color);
		        break;
		    case 4:
		    	color = COLOR4;
		        point[0] = new Spot(3,1,color);
		        point[1] = new Spot(4,1,color);
		        point[2] = new Spot(4,0,color);
		        point[3] = new Spot(5,0,color);
		        break;
		    case 5:
		    	color = COLOR5;
		        point[0] = new Spot(3,0,color);
		        point[1] = new Spot(4,1,color);
		        point[2] = new Spot(3,1,color);
		        point[3] = new Spot(5,1,color);
		        break;
		    case 6:
		    	color = COLOR6;
		        point[0] = new Spot(3,1,color);
		        point[1] = new Spot(4,1,color);
		        point[2] = new Spot(5,1,color);
		        point[3] = new Spot(5,0,color);
		        break;
//		    case 7:
//		    	color = COLOR7;
//		        point[0] = new Spot(3,0,color);
//		        point[1] = new Spot(4,0,color);
//		        point[2] = new Spot(5,0,color);
//		        point[3] = new Spot(6,0,color);
//		        break;
//		    case 8:
//		    	color = COLOR8;
//		        point[0] = new Spot(3,0,color);
//		        point[1] = new Spot(4,0,color);
//		        point[2] = new Spot(5,0,color);
//		        point[3] = new Spot(6,0,color);
//		        break;
		}
	}
    
    //***************************************************************************************
    //according the brick's position in the  current container(Block[][]), try to rotate the brick
    //in the container, and give each spot of the brick new position.
    //if can not rotate at current position, all spots remain the same places
    //
    //spot[1] is the rotate axis,so it won't change,so first try to move the other three spots of
    //the brick, and judge if the new positions is occupied or out of boundary,if one of them is,
    //then cannot rotate the brick; otherwise, rotate it by give each of the spots new position.
    //***************************************************************************************
    public boolean rotate(Block[][] graph){
        int x0, x2, x3, y0, y2, y3;
        boolean canrotate = true;
        x0 = 0;
        x2 = 0;
        x3 = 0;
        y0 = 0;
        y2 = 0;
        y3 = 0;        
    	switch (shape){
	        case 0:
	    	    if (point[1].gety() == point[0].gety()){ 
	    	    	x0 = point[1].getx();
	    	    	y0 = point[1].gety()-1;
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()+2;
	    	    	x3 = point[1].getx();
	    	    	y3 = point[1].gety()+1; 	    	
	    	    }
	    	    else{
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety();
	    	    	x2 = point[1].getx()+1;
	    	    	y2 = point[1].gety();
	    	    	x3 = point[1].getx()+2;
	    	    	y3 = point[1].gety(); 
	    	   }
	           break;
	        case 1:
	        	x0 = point[1].getx();
    	    	y0 = point[1].gety()-1;
    	    	x2 = point[1].getx()+1;
    	    	y2 = point[1].gety();
    	    	x3 = point[1].getx()+1;
    	    	y3 = point[1].gety()-1;
	        	break;
	        case 2:
	    	    if (point[0].gety() == point[3].gety()){ 
	    	    	if (point[0].gety() == point[2].gety()-1){
		    	    	x0 = point[1].getx()-1;
		    	    	y0 = point[1].gety();
		    	    	x2 = point[1].getx();
		    	    	y2 = point[1].gety()+1;
		    	    	x3 = point[1].getx();
		    	    	y3 = point[1].gety()-1; 
	    	    		}
	    	    	else {
		    	    	x0 = point[1].getx();
		    	    	y0 = point[1].gety()-1;
		    	    	x2 = point[1].getx();
		    	    	y2 = point[1].gety()+1;
		    	    	x3 = point[1].getx()+1;
		    	    	y3 = point[1].gety(); 
	    	    	}
	    	    }
	    	    else if (point[0].gety() == point[3].gety()+1){
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety();
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()-1;
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety(); 
	    	    }
	    	    else {
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety();
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()+1;
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety(); 
	    	    }
	    	    break;	
	        case 3:
	    	    if (point[1].gety() == point[3].gety()){
	    	    	x0 = point[1].getx();
	    	    	y0 = point[1].gety()+1;
	    	    	x2 = point[1].getx()+1;
	    	    	y2 = point[1].gety();
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety()-1;
	    	    }
	    	    else{
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety()-1;
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()-1;
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety();
	    	   }
	           break;
	        case 4:
	    	    if (point[1].gety() == point[0].gety()){
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety()-1;
	    	    	x2 = point[1].getx()-1;
	    	    	y2 = point[1].gety();
	    	    	x3 = point[1].getx();
	    	    	y3 = point[1].gety()+1;
	    	    }
	    	    else{
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety();
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()-1;
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety()-1;
	    	   }
	           break;
	        case 5:
	    	    if (point[1].gety() == point[0].gety()+1){ 
	    	    	if (point[1].gety() == point[2].gety()){
		    	    	x0 = point[1].getx();
		    	    	y0 = point[1].gety()-1;
		    	    	x2 = point[1].getx();
		    	    	y2 = point[1].gety()+1;
		    	    	x3 = point[1].getx()+1;
		    	    	y3 = point[1].gety()-1;
	    	    	}
	    	    	else {
		    	    	x0 = point[1].getx()-1;
		    	    	y0 = point[1].gety();
		    	    	x2 = point[1].getx()+1;
		    	    	y2 = point[1].gety()+1;
		    	    	x3 = point[1].getx()+1;
		    	    	y3 = point[1].gety();
	    	     	}
	    	    }
	    	    else if (point[1].gety() == point[0].gety()){
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety()+1;
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()+1;
	    	    	x3 = point[1].getx();
	    	    	y3 = point[1].gety()-1;
	    	    }
	    	    else {
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety()-1;
	    	    	x2 = point[1].getx()-1;
	    	    	y2 = point[1].gety();
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety();
	    	    }
	    	    break;
	        case 6:
	    	    if (point[1].gety() == point[3].gety()+1){ 
	    	    	if (point[1].gety() == point[2].gety()){
		    	    	x0 = point[1].getx();
		    	    	y0 = point[1].gety()-1;
		    	    	x2 = point[1].getx();
		    	    	y2 = point[1].gety()+1;
		    	    	x3 = point[1].getx()+1;
		    	    	y3 = point[1].gety()+1;
	    	    	}
	    	    	else {
		    	    	x0 = point[1].getx()-1;
		    	    	y0 = point[1].gety();
		    	    	x2 = point[1].getx()+1;
		    	    	y2 = point[1].gety();
		    	    	x3 = point[1].getx()+1;
		    	    	y3 = point[1].gety()-1;
	    	    	}
	    	    }
	    	    else if (point[1].gety() == point[3].gety()){
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety()-1;
	    	    	x2 = point[1].getx();
	    	    	y2 = point[1].gety()+1;
	    	    	x3 = point[1].getx();
	    	    	y3 = point[1].gety()-1;
           	    }
	    	    else {
	    	    	x0 = point[1].getx()-1;
	    	    	y0 = point[1].gety();
	    	    	x2 = point[1].getx()-1;
	    	    	y2 = point[1].gety()+1;
	    	    	x3 = point[1].getx()+1;
	    	    	y3 = point[1].gety();
	    	    }
	    	    break;
    	}
    	canrotate = x0 > -1 && x3 < COL && y0 > -1 && y2 > -1 && y3 > -1 && y0 < ROW && y2 < ROW && y3 < ROW;
    	canrotate = canrotate && graph[x0][y0].getFill() == 0 && graph[x2][y2].getFill() == 0 && graph[x3][y3].getFill() == 0;
    	if (canrotate){
    		point[0].Resetxy(x0, y0);
    		point[2].Resetxy(x2, y2);
    		point[3].Resetxy(x3, y3);    	}
    	return canrotate;
    }
    
    //***************************************************************************************
    //method moveLeft and move right:
    //similar to rotate, try to move the brick first, if can move in current container, move it;
    //if not,stay the same place
    //***************************************************************************************
    public boolean moveLeft(Block[][] graph){
		boolean notfill = true;
    	for(int i = 0; i < 4 && point[i].getx() > 0; i++){
    		if (graph[point[i].getx()-1][point[i].gety()].getFill() == 1){
    			notfill = false;
    			break;
    		}
    	}
    	
        if (notfill && point[0].getx() > 0)
    	    for (int i = 0; i < 4; i++){
    	    	point[i].setx(point[i].getx()-1);
    	    }
        return notfill;
    }
    
    public boolean moveRight(Block[][] graph){
		boolean notfill = true;
    	for(int i = 0; i < 4 && point[i].getx() < COL-1; i++){
    		if (graph[point[i].getx()+1][point[i].gety()].getFill() == 1){
    			notfill = false;
    			break;
    		}
    	}
    	
        if (notfill && point[3].getx() < COL-1)
    	    for (int i = 0; i < 4; i++){
    	    	point[i].setx(point[i].getx()+1);
    	    }
        return notfill;
    }
    //***************************************************************************************
    //method moveDown:
    //simular to the other to move method, try to move, judge, and move(if can).
    //but other move methods and rotate method are void method.
    //only movedown method return a int to tell the caller if it can move down
    //if it return 1, which means the current brick can not movedown in current container
    //the caller knows the current brick is already at bottom, and it will generater a new 
    //brick.
    //
    //0 means false and 1 means true, this is C convention.
    //***************************************************************************************
    public int moveDown(Block[][] graph){
		int cannotdown = 0;
		boolean notfill = true;
    	for(int i = 0; i < 4 && point[i].gety() < ROW-1; i++){
    		if (graph[point[i].getx()][point[i].gety()+1].getFill() == 1){
    			notfill = false;
    			break;
    		}
    	}
    	
    	if(notfill && point[2].gety() < ROW-1 && point[1].gety() < ROW-1 && point[0].gety() < ROW-1 && point[3].gety() < ROW-1)
	    	for(int i = 0; i < 4; i++){
	    		point[i].sety(point[i].gety()+1);
	    	}
    	else
    		cannotdown = 1;
    	return cannotdown;
    }
    
    //***************************************************************************************
    //method trymoveDown:
    //similar to the method movedown,but is just judge, do not really move the brick.
    //if it return 1, which means the current brick can not movedown in current container
    //the caller knows the current brick is already at bottom, and it will generate a new 
    //brick.
    //
    //0 means false and 1 means true, this is C convention.
    //***************************************************************************************
    public int trymoveDown(Block[][] graph){
		int cannotdown;
		boolean notfill = true;
    	for(int i = 0; i < 4 && point[i].gety() < ROW-1; i++){
    		if (graph[point[i].getx()][point[i].gety()+1].getFill() == 1){
    			notfill = false;
    			break;
    		}
    	}
    	
    	if(notfill && point[2].gety() < ROW-1 && point[1].gety() < ROW-1 && point[0].gety() < ROW-1 && point[3].gety() < ROW-1)
	    	cannotdown = 0;
    	else
    		cannotdown = 1;
    	return cannotdown;
    }
    
    //normal set and get method.
    public Color getColor(){
    	return color;
    }
    
    public int getShape(){
    	return shape;
    }
    
    public int get0x(){
    	return  point[0].getx();
    }
    
    public int get0y(){
    	return  point[0].gety();
    }
    
    public int get1x(){
    	return  point[1].getx();
    }
    
    public int get1y(){
    	return  point[1].gety();
    }
    
    public int get2x(){
    	return  point[2].getx();
    }
    
    public int get2y(){
    	return  point[2].gety();
    }
    
    public int get3x(){
    	return  point[3].getx();
    }
    
    public int get3y(){
    	return  point[3].gety();
    }
}