package player;

import java.util.ArrayList;
import java.util.Scanner;

import ship.AircraftCarrier;
import ship.Cruiser;
import ship.Frigate;
import ship.PatrolCraft;
//import player.SampleRandomGuessPlayer.OwnShip;
import ship.Ship;
import ship.Submarine;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

/**
 * Probabilistic guess player (task C).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class ProbabilisticGuessPlayer  implements Player
{	
	private World world;
	private boolean targeting;
	private ArrayList<ShipLocation> shipLocations;
	private ArrayList<Coordinate> shots;
	private ArrayList<Coordinate> hits;
	private boolean aircraft, cruiser, frigate, patrol, sub;
	private Coordinate lastHit;
	private Coordinate firstHit;
	private boolean isHit=false;
	
	//inner class to instantiate ownship 
	public class OwnShip { 
				public OwnShip() {}
				Ship ship = null;
				int[] rowCdns = { -1, -1, -1, -1, -1, -1 };
				int[] clnCdns = { -1, -1, -1, -1, -1, -1 };
				boolean[] isdown = { true, true, true, true, true, true };
   
   }
   OwnShip[] ownShips = new OwnShip[10];
	
    @Override
    public void initialisePlayer(World world) 
    {
    	this.world = world;
    	this.targeting = false;
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

	//returns whether the shot was  hit or miss to other player
    @Override
    public Answer getAnswer(Guess guess) 
    {	
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

    //main guess function - type of guess based on whether targeting is active or inactive
    @Override
    public Guess makeGuess() 
    {
        // To be implemented.
    	Guess guess = new Guess();
    	if(targeting == false)
    	{
    		Coordinate best = checkBest();
    		int row = best.row;
    		int column = best.column;
    		guess.row = row;
    		guess.column = column;
    		
    	}
    	if(targeting == true)
    	{
    		int i = lastHit.row;
    		int j = lastHit.column;
    		Coordinate best = checkTargeted(i, j);
    		int bestRow = best.row;
    		int bestColumn = best.column;
    		guess.row = bestRow;
    		guess.column = bestColumn;
    		
    	}

    	return guess;
    } // end of makeGuess()
    
    //checks every adjacent cell to the hit cell. Counts the cell with the highest chance of containing any ship type - called when targeting active
    public Coordinate checkTargeted(int i, int j)
    {
    	Coordinate temp = world.new Coordinate();
    	int highestCount = 0;
    	int chanceNorth = 0;
    	int chanceEast = 0;
    	int chanceSouth = 0;
    	int chanceWest = 0;
    	Coordinate best = world.new Coordinate();
    	
    	temp.row = i-1;
    	temp.column = j;
    	if(shots.contains(temp) == false && hits.contains(temp) == false)
    	{
    		chanceNorth += patrol(i-1, j);
			chanceNorth += frigate(i-1, j);
			chanceNorth += sub(i-1, j);
			chanceNorth += cruiser(i-1, j);
			chanceNorth += aircraft(i-1, j); 
			if(chanceNorth > highestCount)
			{
				highestCount = chanceNorth;
				best.row = i-1;
				best.column = j;
			}
    	}
 
    	temp.row = i;
    	temp.column = j+1;
    	if(shots.contains(temp) == false && hits.contains(temp) == false)
    	{
    		chanceEast += patrol(i, j+1);
    		chanceEast += frigate(i, j+1);
    		chanceEast += sub(i, j+1);
    		chanceEast += cruiser(i, j+1);
    		chanceEast += aircraft(i, j+1); 
    		if(chanceEast > highestCount)
    		{
    			highestCount = chanceEast;
    			best.row = i;
				best.column = j+1;
    		}
    	}

    	temp.row = i+1;
    	temp.column = j;
    	if(shots.contains(temp) == false && hits.contains(temp) == false)
    	{
    		chanceSouth += patrol(i+1, j);
    		chanceSouth += frigate(i+1, j);
    		chanceSouth += sub(i+1, j);
    		chanceSouth += cruiser(i+1, j);
    		chanceSouth += aircraft(i+1, j); 
    		if(chanceSouth > highestCount)
    		{
    			highestCount = chanceSouth;
    			best.row = i+1;
				best.column = j;
    		}
    	}

    	temp.row = i;
    	temp.column = j-1;
    	if(shots.contains(temp) == false && hits.contains(temp) == false)
    	{
    		chanceWest += patrol(i, j-1);
    		chanceWest += frigate(i, j-1);
    		chanceWest += sub(i, j-1);
    		chanceWest += cruiser(i, j-1);
    		chanceWest += aircraft(i, j-1);
    		if(chanceWest > highestCount)
    		{
    			highestCount = chanceWest;
    			best.row = i;
				best.column = j-1;
    		}
    	}
    	
    	if(highestCount == 0)
    	{
    		int firstHitrow = firstHit.row;
    		int firstHitColumn = firstHit.column;
    		highestCount = 0;
    		if(firstHitrow == i && firstHitColumn == j && highestCount == 0)
    		{
    			targeting = false;
    			return checkBest();
    		}
    		return best = checkTargeted(firstHitrow, firstHitColumn);
    	}
    	return best;
    }
    
    //counts the cells to see which cell has the highest possibility of hitting a ship (counts total of all types and combinations) - called when targeting inactive
    public Coordinate checkBest()
    {
    	int highestCount = 0;
    	int chance = 0;
    	Coordinate best = world.new Coordinate();
    	
    	for(int j = 0; j < 10; j++)
    	{
    		for(int i = 0; i < 10; i++)
    		{
  
    			Coordinate cdn = world.new Coordinate();
    			cdn.row = i;
    			cdn.column = j;
    		
    			if(shots.contains(cdn) == false)
    			{
    				
    				chance += patrol(i,j);
    				chance += frigate(i,j);
    				chance += sub(i,j);
    				chance += cruiser(i,j);
    				chance += aircraft(i,j);
    				if(chance > highestCount && hits.contains(cdn) == false)
    				{
    					best = cdn;
    					highestCount = chance;
    					chance = 0;
    				}
    				else if(highestCount == 0 && hits.contains(cdn) == false)
    				{
    					best = cdn;
    					highestCount = chance;
    					chance = 0;
    				}
    				else
    				{
    					chance = 0;
    				}
    			}
    		}
    	}
    	
    	return best;
    }
    
    //checks every combination that an aircraft carrier could be in for that cell
    public int aircraft(int i, int j)
    {

    	Coordinate temp = world.new Coordinate();
    	int chance = 0;
    	if(aircraft)
		{
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.column = j-2;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.row = i+1;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						temp.column = j-1;
						if(shots.contains(temp) == false)
						{
							temp.column = j;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				temp.column = j+2;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					temp.row = i+1;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						temp.column = j+1;
						if(shots.contains(temp) == false)
						{
							temp.column = j;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.column = j-1;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.column = j-2;
					if(shots.contains(temp) == false && temp.column >= 0)
					{
						temp.row = i;
						if(shots.contains(temp) == false)
						{
							temp.column = j-1;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.column = j+1;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					temp.column = j+2;
					if(shots.contains(temp) == false && temp.column < 10)
					{
						temp.row = i;
						if(shots.contains(temp) == false)
						{
							temp.column = j +1;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.row = i-1;
				if(shots.contains(temp) == false && temp.row >= 0)
				{
					temp.row = i-2;
					if(shots.contains(temp) == false && temp.row >= 0)
					{
						temp.column = j;
						if(shots.contains(temp) == false)
						{
							temp.row = i-1;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.row = i+1;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					temp.row = i+2;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						temp.column = j;
						if(shots.contains(temp) == false)
						{
							temp.row = i+1;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				temp.row = i+1;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					temp.row = i+2;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						temp.column = j;
						if(shots.contains(temp) == false)
						{
							temp.row = i+1;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.row = i-2;
				if(shots.contains(temp) == false && temp.row >= 0)
				{
					temp.column = j+1;
					if(shots.contains(temp) == false && temp.column < 10)
					{
						temp.row = i-1;
						if(shots.contains(temp) == false)
						{
							temp.row = i;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.row = i+1;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					temp.column = j;
					if(shots.contains(temp) == false)
					{
						temp.column = j+1;
						if(shots.contains(temp) == false && temp.column < 10)
						{
							temp.row = i;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.row = i-1;
				if(shots.contains(temp) == false && temp.row >= 0)
				{
					temp.column = j;
					if(shots.contains(temp) == false)
					{
						temp.column = j+1;
						if(shots.contains(temp) == false && temp.column < 10)
						{
							temp.row = i;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i+1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row < 10)
			{
				temp.column = j-1;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.row = i;
					if(shots.contains(temp) == false)
					{
						temp.row = i-1;
						if(shots.contains(temp) == false && temp.row >= 0)
						{
							temp.column = j;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.column = j+1;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					temp.row = i;
					if(shots.contains(temp) == false)
					{
						temp.row = i+1;
						if(shots.contains(temp) == false && temp.row < 10)
						{
							temp.column = j;
							if(shots.contains(temp) == false)
							{
								chance++;
							}
						}
					}
				}
			}
		}
    	return chance;
    }
    
  //checks every combination that a cruiser could be in for that cell
    public int cruiser(int i, int j)
    {
    	Coordinate temp = world.new Coordinate();
    	int chance = 0;
    	if(cruiser)
		{
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				temp.row = i+1;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					temp.column = j;
					if(shots.contains(temp) == false && temp.column >= 0)
					{
						chance++;
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.column = j-1;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.row = i;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						chance++;
					}
				}
			}
			temp.row = i+1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row < 10)
			{
				temp.column = j-1;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.row = i;
					if(shots.contains(temp) == false && temp.row >= 0)
					{
						chance++;
					}
				}
			}
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				temp.row = i-1;
				if(shots.contains(temp) == false && temp.row >= 0)
				{
					temp.column = j;
					if(shots.contains(temp) == false && temp.column >= 0)
					{
						chance++;
					}
				}
			}
		}
    	return chance;
    }
    
    //checks every combination that a submarine could be in for that cell
    public int sub(int i, int j)
    {
    	Coordinate temp = world.new Coordinate();
    	int chance = 0;
    	if(sub)
		{
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				temp.column = j+2;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					chance++;
				}
			}
			temp.row = i+1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row < 10)
			{
				temp.row = i+2;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					chance++;
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.column = j-2;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					chance++;
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.row = i-2;
				if(shots.contains(temp) == false && temp.row >= 0)
				{
					chance++;
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.row = i+1;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					chance++;
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.column = j+1;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					chance++;
				}
			}
		}
    	return chance;
    }
    
    //checks every combination that a frigate could be in for that cell
    public int frigate(int i, int j)
    {
    	Coordinate temp = world.new Coordinate();
    	int chance = 0;
    	if(frigate)
		{
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				temp.column = j+2;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					temp.column = j+3;
					if(shots.contains(temp) == false && temp.column < 10)
					{
						chance++;
					}
				}
			}
			temp.row = i+1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row < 10)
			{
				temp.row = i+2;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					temp.row = i+3;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						chance++;
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.column = j-2;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.column = j-3;
					if(shots.contains(temp) == false && temp.column >= 0)
					{
						chance++;
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.row = i-2;
				if(shots.contains(temp) == false && temp.row >= 0)
				{
					temp.row = i-3;
					if(shots.contains(temp) == false && temp.row >= 0)
					{
						chance++;
					}
				}
			}
			temp.row = i-2;
			temp.column =j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.row = i-1;
				if(shots.contains(temp) == false)
				{
					temp.row = i+1;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						chance++;
					}
				}
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				temp.row = i+1;
				if(shots.contains(temp) == false && temp.row < 10)
				{
					temp.row = i+2;
					if(shots.contains(temp) == false && temp.row < 10)
					{
						chance++;
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.column = j-2;
				if(shots.contains(temp) == false && temp.column >= 0)
				{
					temp.column = j+1;
					if(shots.contains(temp) == false && temp.column < 10)
					{
						chance++;
					}
				}
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				temp.column = j+1;
				if(shots.contains(temp) == false && temp.column < 10)
				{
					temp.column = j+2;
					if(shots.contains(temp) == false && temp.column < 10)
					{
						chance++;
					}
				}
			}
		}
    	return chance;
    }
    
    //checks every combination that a patrol ship could be in for that cell
    public int patrol(int i, int j)
    {
    	Coordinate temp = world.new Coordinate();
    	int chance = 0;
		if(patrol)
		{
			temp.row = i;
			temp.column = j+1;
			if(shots.contains(temp) == false && temp.column < 10)
			{
				chance++;
			}
			temp.row = i+1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row < 10)
			{
				chance++;
			}
			temp.row = i;
			temp.column = j-1;
			if(shots.contains(temp) == false && temp.column >= 0)
			{
				chance++;
			}
			temp.row = i-1;
			temp.column = j;
			if(shots.contains(temp) == false && temp.row >= 0)
			{
				chance++;
			}
		}
		return chance;
    }
    
    
    //updates the status of targeting based on returned answer. Sets remaining ships to false if a ship is sunk
	@Override
    public void update(Guess guess, Answer answer) 
    {
    	
    	if(answer.isHit == true && answer.shipSunk == null)
    	{
    		lastHit = world.new Coordinate();
    		lastHit.row = guess.row;
    		lastHit.column = guess.column;
    		if(firstHit == null)
    		{
    			firstHit = world.new Coordinate();
    			firstHit = lastHit;
    		}
    		targeting = true;
    		hits.add(lastHit);
    	}
    	if(answer.isHit == true && answer.shipSunk != null)
    	{
    		Coordinate sinkHit = world.new Coordinate();
    		sinkHit.row = guess.row;
    		sinkHit.column = guess.column;
    		hits.add(sinkHit);
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
    		targeting = false;
    		firstHit = null;
    	}
    	if(answer.isHit == false)
    	{
    		Coordinate miss = world.new Coordinate();
    		miss.row = guess.row;
    		miss.column = guess.column;
    		shots.add(miss);
    	}
        // To be implemented.
    } // end of update()

	//checks if there are no more remaining ships
    @Override
    public boolean noRemainingShips() 
    {
    	for (int i = 0; i < 5; i++) {
    		for (int j = 0; j < ownShips[i].ship.len(); j++) {
    		if (ownShips[i].isdown[j] == false)
    		return false;
    		}
    		}
    	 return true;
    } // end of noRemainingShips()

} // end of class ProbabilisticGuessPlayer
