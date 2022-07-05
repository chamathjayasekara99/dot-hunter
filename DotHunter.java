
import java.util.ArrayList;
import java.util.Scanner;
//interface to get point's posisiton
interface IPositions{
	int getxPOS();
	int getyPOS();
	default boolean killingChance(int c,int b,int sx,int sy,int hx,int hy) {
		boolean chance =false;
		int point= ((int)Math.pow((sx-c),2)/(int)Math.pow(hy,2))
				+((int)Math.pow((sy-b),2)/(int)Math.pow(hx,2));
		//if point <1 means  soldier is inside the eclipse
		//if point ==1 means  soldier is on the eclipse
		if(point<1||point ==1)
		   chance=true;//Their is chance to kill soldier
		
		return chance;
	}
}
class SoundException extends Exception{
	public SoundException(String alert) {
		super(alert);
	}
}

class Hunter  implements IPositions{
    private int x=0;
    private int y=0;
    private String color;
	private String name;
	private int superDotCount=0;//to count superdots
	private int[] previousPoint=new int[2];

	public Hunter(String name,String color) {
		//Assign name and color to the hunters
		this.name=name;
		this.color=color;
	}
	public void setxPOS(int x) {
		//Assining previous points
		previousPoint[0]=this.x;
		previousPoint[1]=this.y;
		//Assuming hunter spawn in the middle of the map
		//Means hunter can go somewhere in x=-9,y=-8
		
	   if(this.x<=250&&this.x>=-250) {	
		this.x=x;
	   }else {
		   //Reached boundary limit from x axis
		   try {
			   //Alert user
				throw new SoundException("Oh oo!!");
		   }catch(SoundException e) {
					System.out.println(e.getMessage());
					//Retaking inputs
					Scanner sc = new  Scanner(System.in);
					setxPOS(sc.nextInt());
		   }
	   }
	}
	public  void setyPOS(int y){
		//Assuming hunter spawn in the middle of the map
		//Means hunter can go somewhere in x=-9,y=-8
		if(this.y<=360&&this.y>=-360) {
			this.y=y;
		}else {
			//Reached boundary limit from y axis
			try {
			     throw new SoundException("Oh oo!!");
			}catch(SoundException e) {
				System.out.println(e.getMessage());
				Scanner sc = new  Scanner(System.in);
				setyPOS(sc.nextInt());
				
			}
		}		
	}
	//get the hunter position
	public int getxPOS() {
		return this.x;
	}
	
	public int getyPOS() {
		return this.y;
	}
    public void hunt(Board board) {
		
        //Make soldier walk
    	board.moveSoldiers();
    	//Randomly setting dots
    	//each time hunter hunt dot will be reduced
    	board.addNewDot();
    	
		//If the remaining dot count is zero hunter wins
		if(board.getDotCount()==0) {
			System.out.println("You won");
			System.exit(0);
		}
		//If the dot is zero hunter's super dotcount increases 
		if(board.getDotType().equalsIgnoreCase("superDot")) {
			System.out.println("Hunting super dots");								
		       //store super dots
			superDotCount+=1;
			
	   }
		//if the do type is dot remove dots
		if(board.getDotType().equalsIgnoreCase("dot")) {
			System.out.println("Hunting  dots");
		}
		//Check each  soldiers are in the shooting range of the soldier
		//Also hunter need to have  at least one super dots hunter can kill soldier
	     for(int i=0;i<3;i++){
             if(killingChance(this.x,this.y,(int)(board.getSoldierDetails(i).get(0)),
                (int)(board.getSoldierDetails(i).get(1)),this.x,this.y)&&superDotCount>=1) {
			//kill a soldier
            	  board.killSoldier(i);
			//remove super dots as hunter kills a soldier
			      superDotCount-=1;			      			      
		     }
	    }
	}
	public  void move(Hunter hunter) {
		//If hunters current position is different from previous ones
		//hunter moves
		if(hunter.x!=previousPoint[0]||hunter.y!=previousPoint[1]) {
	         System.out.println("Hunter is moving, X:"+hunter.x+" Y:"+hunter.y);
		}//if hunter sin't moved
		else {
			  System.out.println("Hunter isn't moving, X:"+hunter.x+" Y:"+hunter.y);
		}
			
		
	}
	//c=h,b=k(center cordinates)        
	//sx=x,sy=y(soldier cordinates)
	//hx=b,hy=a(hunters maximum  y(hy) axis and x axis(hx) )
	public boolean killingChance(int c,int b,int sx,int sy,int hx,int hy) {
		boolean chance =false;
		int point= ((int)Math.pow((sx-c),2)/(int)Math.pow(hy,2))
				+((int)Math.pow((sy-b),2)/(int)Math.pow(hx,2));
		//if point <1 means  soldier is inside the eclipse
		//if point ==1 means  soldier is on the eclipse
		if(point<1||point ==1)
		   chance=true;//Their is chance to kill soldier
		
		return chance;
	}
	//get superDotCount
	public int getSuperDotCount() {
		return superDotCount;
		 
	}
	//display hunter's position
	public void displayPosition() {
		System.out.println("Hunter X"+this.getxPOS()+"\n"+
	                       "Hunter Y"+this.getyPOS());
	}
			
}



class Board{
	 private int dot=97;
	 private int superDot=3;
	 private String dotType;
	 private Hunter hunter;
	 
	 private ArrayList<Soldier> threeSoldiers=new ArrayList<>();
	 public Board(Soldier[] sold) {
		 for(Soldier solds:sold) {
			 solds.setCordinate();
		 }
	 }
	 public Board(String dotType,Hunter myhunter,Soldier[] sold)  {
		 //Initiating  soldier cordinates
		 this(sold);
		//Initiating  hunter cordinates
		 init(myhunter);
		 this.hunter=myhunter;
		 this.dotType=dotType;
		 for(int i=0;i<3;i++) {
			threeSoldiers.add(sold[i]);
		 }
	System.out.println("Board is ready and three soldiers and the hunter is positioned in the board");
		 
	 }
	 public void killSoldier(int i) {
		 //Kill the soldier who is in the shooting range of the hunter
	    //Assuming that removing from the board is equal to killing a soldier	 
		   
		   threeSoldiers.remove(i);
	  
	 }
	 //Set hunters initial points
	 public void init(Hunter hunter) {
		 hunter.setxPOS(0);
		 hunter.setyPOS(0);
	 }
	 //get the current dot type 
	 public String getDotType() {
		 return this.dotType;
	 }
	 //get current total dotcount
	 public int getDotCount() {
		 return this.dot+this.superDot;
	 }
	 //To move  cordinate  while hunter is hunting dots
	 public void moveSoldiers() {
		 for(int i=0;i<3;i++) {
				threeSoldiers.get(i).setCordinate();
		}
	 }
	 //get a soldier position and color
	 public ArrayList getSoldierDetails(int i) {
		 
		 
		 return threeSoldiers.get(i).getSoldierDetails();
	 }
	 public void addNewDot() {
		 int i=(int)(Math.random()*(2));
		 //dots are randomly choosen acording the i value and no of remaining dots
		 if(i==0&&superDot>=1) {
			 dotType="superDot";
                         this.superDot-=1;
		 }else if(dot>=1&&i!=0) {
			 dotType="dot";
                         this.dot-=1;
		 }
			 
		 
	 }
	 public Soldier getSoldier(int i) {
		 return threeSoldiers.get(i);
	 }
	 /*
	  * for check
	  * 
	  * 
	  */
	 public void displayPosition(int i) {
		 System.out.println("Soldier "+i+": X "+getSoldierDetails(i).get(0)+"\n"
		 		+ "Soldier "+i+": Y "+getSoldierDetails(i).get(1));
	 }
	
}
abstract class Soldier implements IPositions{
	protected int x;
	protected int y;
	protected String color;
    abstract void hunt();
    public String getColor() {
		return color;
	}
	public void setCordinate() {
		//Setting Soldier cordinates randomly
		this.x=(int)(Math.random()*(499)-250);
		this.y=(int)(Math.random()*(719)-360);
	}

	public int getxPOS() {
	     return x;
	}
	public int getyPOS() {
	     return y;
	}
	//Getting soldier details as  a arraylist
	
	public ArrayList getSoldierDetails() {
		ArrayList details =new ArrayList();
		
		details.add(0, getxPOS() );
		details.add(1, getyPOS());
		details.add(2, this.getColor());
		return details;
		
	}
}
class RedSoldier extends Soldier{

      public RedSoldier() {
    	  color="Red";
      }
	@Override
	public void hunt() {
		System.out.println("Killed using a hand");
		System.out.println("Game over");
		
	}
	
	
	
}
class GreenSoldier extends Soldier{

	 public GreenSoldier() {
   	  color="Green";
     }
	@Override
	public void hunt() {
		// TODO Auto-generated method stub
		System.out.println("Killed using a knife");
		System.out.println("Game over");
	}
	
	
}
class BlueSoldier extends Soldier{
	 public BlueSoldier() {
	   	  color="Blue";
	 }
	@Override
	public void hunt() {		
		System.out.println("Killed using a gun");
		System.out.println("Game over");
	
	}
	
	
	
}


public class MainApp {

	public static void main(String[] args)  {
	   Scanner  sc =new Scanner (System.in);
	  
	    
	   Hunter myhunter = new Hunter("Maha Deva","Brown");
	   Soldier threeSoldiers[] = {new RedSoldier(),new RedSoldier(),new GreenSoldier()};
	   Board myboard = new Board("superDot",myhunter,threeSoldiers);
	   
	   System.out.println("User the keyboard up,down,left,right arrow keys to move the hunter");
	   while(true) {  
		   myhunter.setxPOS(sc.nextInt());
		   myhunter.setyPOS(sc.nextInt());
		   myhunter.move(myhunter);
		   myhunter.hunt(myboard);
	 
	 
	   //hunter doesn't have any super dot and 
		   if (myhunter.getSuperDotCount()==0) {
			   for(int i=0;i<3;i++) {
			 //if hunter is in the killing range of any solider their is chance of getting killed
	    	   //firs parameter pair position of the soldier        
	    	   //second parameter pair hunter position
	    	   //second parameter pair hunter shooting range (maximum  y(hy) axis and x axis(hx) )
				   if( threeSoldiers[i].killingChance(threeSoldiers[i].getxPOS(), threeSoldiers[i].getyPOS()
						   ,myhunter.getxPOS(),myhunter.getxPOS(),threeSoldiers[i].getxPOS(), threeSoldiers[i].getyPOS())) {	           		    
		    	  
		    	  //now Blue soldier can kill since he have a gun
					   if(myboard.getSoldierDetails(i).get(2).equals("Blue")) {
						   threeSoldiers[i].hunt();
						   System.exit(0);
					   }
		          //Soldiers  can kill hunter using hand or knife if the hunter and the soldier are in the same poisition
		          
					   if((myboard.getSoldierDetails(i).get(2).equals("Red")||myboard.getSoldierDetails(i).get(2).equals("Green"))
							   && myhunter.getxPOS()==(int)myboard.getSoldierDetails(i).get(0)&&myhunter.getyPOS() ==(int)myboard.getSoldierDetails(i).get(1) ) {
						   threeSoldiers[i].hunt();
						   System.exit(0);
					   }
				   }
			   }
	     
		   }
		   
	   }//end of the while loop
       
	}
}
