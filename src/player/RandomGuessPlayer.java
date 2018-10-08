package player;
import ship.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.sun.javafx.collections.MappingChange.Map;

//import player.SampleRandomGuessPlayer.OwnShip;
//import player.SampleRandomGuessPlayer.OwnShip;
//import player.SampleRandomGuessPlayer.OwnShip;
//import player.SampleRandomGuessPlayer.OwnShip;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class RandomGuessPlayer implements Player{

	
	static final int[] rowDeltas = { 1, 0, -1, 0, 1, 0, -1, 0 };
	static final int[] clnDeltas = { 0, -1, 0, 1, 1, 0, -1, 0 }; 
	private World world;
	AircraftCarrier aircraftCarrier;
	Cruiser crusier;
	Frigate frifate;
	PatrolCraft patrolCraft;
	Submarine submarine;
	int row;
	int col;
	private boolean aircraft, cruiser, frigate, patrol, sub;
	private int player;
	private ArrayList<ShipLocation> shipLocations;
    private ArrayList<Coordinate> shots;
   // HashMap<Ship, ArrayList<Coordinate>> hit = new HashMap<Ship, ArrayList<Coordinate>>();
    private boolean updateShot;
    private boolean isHit=false;
    private Ship shipShunk=null;
    private Coordinate firstHit;
    private Coordinate lastHit;
    int rowSize = 0;
    int clnSize = 0;
    private Ship ship;
    //OwnShip[] ownShips = new OwnShip[5];
    
    public class OwnShip { 
		public OwnShip() {}
		Ship ship = null;
		int[] rowCdns = { -1, -1, -1, -1, -1, -1 };
		int[] clnCdns = { -1, -1, -1, -1, -1, -1 };
		boolean[] isdown = { true, true, true, true, true, true };

}
OwnShip[] ownShips = new OwnShip[10];
    
    			
    //String [] diffShips = new String[5];
  //  private ArrayList<Coordinate> hits = new ArrayLishist<Coordinate>();
    private static ArrayList<Integer> hitsInt = new ArrayList<Integer>();
    private static ArrayList<Coordinate> hits = new ArrayList<Coordinate>();
    boolean[][] toGuess;
    
    @Override
    public void initialisePlayer(World world) {
    	this.world = world;
    	//this.targeting = false;
    	this.shipLocations = world.shipLocations;
    	shots = new ArrayList<Coordinate>();
    	hits = new ArrayList<Coordinate>();
    	aircraft = true;
    	cruiser = true;
    	frigate = true;
    	patrol = true;
    	sub = true;
    	firstHit = null;
    	lastHit = null;
    	
    	AircraftCarrier aircraft1=new AircraftCarrier();
    	Cruiser cruiser1=new Cruiser();
    	Frigate frigate1=new Frigate();
    	PatrolCraft patrol1=new PatrolCraft();
    	Submarine sub1=new Submarine();
    	 //instantiates each ship type
    	 int i=0;
         for(ShipLocation location : shipLocations){
         	   ownShips[i] = new OwnShip();
         		   
         	   if(i == 0)
         	   {
         		  ownShips[i].ship = aircraft1;
         	   }
         	   else if(i == 1)
         	   {
         		  ownShips[i].ship = frigate1;
         	   }
         	   else if(i == 2)
         	   {
         		  ownShips[i].ship = sub1;
         	   }
         	   else if(i == 3)
         	   {
         		  ownShips[i].ship = cruiser1;
         	   }
         	   else
         	   {
         		  ownShips[i].ship = patrol1;
         	   }

         				for (int j = 0; j < ownShips[i].ship.len()*ownShips[i].ship.width(); j++) {
         					ownShips[i].rowCdns[j] = location.coordinates.get(j).row;
         					ownShips[i].clnCdns[j] = location.coordinates.get(j).column;
         					ownShips[i].isdown[j] = false;
         					
         					}
         				i = i+1;
         }
        
      
    } // end of initialisePlayer()

    /**/
    
    @Override
    public Answer getAnswer(Guess guess) {	
    	Answer localAnswer = new Answer();
    	localAnswer.shipSunk = null;
        		for (int i = 0; i < 5; i++) {			
        			for (int j = 0; j < ownShips[i].ship.len()*ownShips[i].ship.width(); j++) {
        				if ((guess.row == ownShips[i].rowCdns[j]) && (guess.column == ownShips[i].clnCdns[j])) {
        					localAnswer.isHit = true;
        					ownShips[i].isdown[j] = true;
        					int k = 1;
        					for (int m = 0; m < ownShips[i].ship.len()*ownShips[i].ship.width(); m++) {
        						if (ownShips[i].isdown[m] == false) k = 0;
        					}
        					if (k != 0) {
        						localAnswer.shipSunk = ownShips[i].ship;
        					}
        					return localAnswer;
        				}
        			}
        		}
        		return localAnswer;
} // end of getAnswer()


    @Override
    public Guess makeGuess() {
    	
    	Random random = new Random();
    	Guess guess = new Guess();
    	guess.row= random.nextInt(10)+0;
    	guess.column= random.nextInt(10)+0;
    	
    	return guess;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
    	
    	System.out.println(guess);
    	
    	if(answer.isHit == true && answer.shipSunk == null)
    	{
    		System.out.println("HIT!");
    		lastHit = world.new Coordinate();
    		lastHit.row = guess.row;
    		lastHit.column = guess.column;
    		if(firstHit == null)
    		{
    			firstHit = world.new Coordinate();
    			firstHit = lastHit;
    		}
  
    		hits.add(lastHit);
    	}
    	if(answer.isHit == true && answer.shipSunk != null)
    	{
    		Coordinate sinkHit = world.new Coordinate();
    		sinkHit.row = guess.row;
    		sinkHit.column = guess.column;
    		hits.add(sinkHit);
    		System.out.print("HIT! & SUNK!");
    		if(answer.shipSunk.name().equals("AircraftCarrier"))
    		{
    			aircraft = false;
    		}
    		if(answer.shipSunk.name().equals("Cruiser"))
    		{
    			cruiser = false;
    		}
    		if(answer.shipSunk.name().equals("Frigate"))
    		{
    			frigate = false;
    		}
    		if(answer.shipSunk.name().equals("PatrolCraft"))
    		{
    			patrol = false;
    		}
    		if(answer.shipSunk.name().equals("Submarine"))
    		{
    			sub = false;
    		}
 
    		firstHit = null;
    	}
    	if(answer.isHit == false)
    	{
    		System.out.println("MISS!");
    		Coordinate miss = world.new Coordinate();
    		miss.row = guess.row;
    		miss.column = guess.column;
    		shots.add(miss);
    	}
    	
    	if(answer.isHit == true && answer.shipSunk != null)
    	{
    		Coordinate sinkHit = world.new Coordinate();
    		sinkHit.row = guess.row;
    		sinkHit.column = guess.column;
    		hits.add(sinkHit);
    		System.out.print("HIT! & SUNK!");
    		if(answer.shipSunk.name().equals("AircraftCarrier"))
    		{
    			aircraft = false;
    		}
    		if(answer.shipSunk.name().equals("Cruiser"))
    		{
    			cruiser = false;
    		}
    		if(answer.shipSunk.name().equals("Frigate"))
    		{
    			frigate = false;
    		}
    		if(answer.shipSunk.name().equals("PatrolCraft"))
    		{
    			patrol = false;
    		}
    		if(answer.shipSunk.name().equals("Submarine"))
    		{
    			sub = false;
    		}
    		//targeting = false;
    		firstHit = null;
    	}
    	if(answer.isHit == false)
    	{
    		System.out.println("MISS!");
    		Coordinate miss = world.new Coordinate();
    		miss.row = guess.row;
    		miss.column = guess.column;
    		shots.add(miss);
    	}
    } // end of update()


    @Override
    public boolean noRemainingShips() {
    
    	  for (int i = 0; i < 5; i++) {
      		for (int j = 0; j < ownShips[i].ship.len(); j++) {
        		if (ownShips[i].isdown[j] == false)
        		return false;
        		}
        		}
        	 return true;
    } // end of noRemainingShips()

} // end of class RandomGuessPlayer
