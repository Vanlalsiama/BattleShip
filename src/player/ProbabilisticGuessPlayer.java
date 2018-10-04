package player;

import java.util.ArrayList;
import java.util.Scanner;

//import player.SampleRandomGuessPlayer.OwnShip;
import ship.Ship;
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
	private boolean hunting;
	private boolean targeting;
	private ArrayList<ShipLocation> shipLocations;
	private ArrayList<Coordinate> shots;
	private ArrayList<Coordinate> hits;
	private boolean aircraft, cruiser, frigate, patrol, sub;
	private Coordinate lastHit;
	private Coordinate firstHit;
	private boolean isHit=false;
	private class OwnShip { private OwnShip() {}
	/*  24 */     Ship ship = null;
	/*  25 */     int[] rowCdns = { -1, -1, -1, -1, -1 };
	/*  26 */     int[] clnCdns = { -1, -1, -1, -1, -1 };
	/*  27 */     boolean[] isdown = { true, true, true, true, true };
	/*     */     
	/*     */      }
	/*  30 */   OwnShip[] ownShips = new OwnShip[5];
	
    @Override
    public void initialisePlayer(World world) 
    {
    	this.world = world;
    	this.hunting = false;
    	this.targeting = false;
    	this.shipLocations = world.shipLocations;
    	this.shots = world.shots;
    	hits = new ArrayList<Coordinate>();
    	aircraft = true;
    	cruiser = true;
    	frigate = true;
    	patrol = true;
    	sub = true;
    	firstHit = null;
    	lastHit = null;
    	// To be implemented.
    } // end of initialisePlayer()

	@Override
    public Answer getAnswer(Guess guess) 
    {	Answer localAnswer = new Answer();
		   
		    	/*  71 */     return localAnswer;
    } // end of getAnswer()


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
    	System.out.println("guess row = " + guess.row);
    	System.out.println("guess column = " + guess.column);

    	return guess;
    } // end of makeGuess()

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
    //	System.out.println("North temp.row = " +temp.row);
    //	System.out.println("temp.column = " +temp.column);
    		chanceNorth += patrol(i-1, j);
			chanceNorth += frigate(i-1, j);
			chanceNorth += sub(i-1, j);
			chanceNorth += cruiser(i-1, j);
			chanceNorth += aircraft(i-1, j); 
			System.out.println("chance North = " + chanceNorth);
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
    		System.out.println("East temp.row = " +temp.row);
    	System.out.println("temp.column = " +temp.column);
    		chanceEast += patrol(i, j+1);
    		chanceEast += frigate(i, j+1);
    		chanceEast += sub(i, j+1);
    		chanceEast += cruiser(i, j+1);
    		chanceEast += aircraft(i, j+1); 
    		System.out.println("chance east = " + chanceEast);
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
    	System.out.println("South temp.row = " +temp.row);
    	System.out.println("temp.column = " +temp.column);
    		chanceSouth += patrol(i+1, j);
    		chanceSouth += frigate(i+1, j);
    		chanceSouth += sub(i+1, j);
    		chanceSouth += cruiser(i+1, j);
    		chanceSouth += aircraft(i+1, j); 
    		System.out.println("chance south = " + chanceSouth);
    		if(chanceSouth > highestCount)
    		{
    			highestCount = chanceSouth;
    			best.row = i+1;
				best.column = j;
    		}
    	}

    	temp.row = i;
    	temp.column = j-1;
    	if(shots.contains(temp) == false && hits.contains(temp) == false && shots.contains(temp) == false)
    	{
    	System.out.println("West temp.row = " +temp.row);
    	System.out.println("temp.column = " +temp.column);
    		chanceWest += patrol(i, j-1);
    		chanceWest += frigate(i, j-1);
    		chanceWest += sub(i, j-1);
    		chanceWest += cruiser(i, j-1);
    		chanceWest += aircraft(i, j-1);
    		System.out.println("chance west = " + chanceWest);
    		if(chanceWest > highestCount)
    		{
    			highestCount = chanceWest;
    			best.row = i;
				best.column = j-1;
    		}
    	}
    	
    	if(highestCount == 0)
    	{
    		System.out.println("Higest count = " + highestCount);
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
    public Coordinate checkBest()
    {
    	int highestCount = 0;
    	int chance = 0;
    	Coordinate best = world.new Coordinate();
    	
    	for(int i = 0; i < 10; i++)
    	{
    		for(int j = 0; j < 10; j++)
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
    				System.out.println("row " + cdn.row + "column " + cdn.column +"chance is " + chance);
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
    					
    			//	System.out.println("HIGHEST " + highestCount);
    			}
    		}
    	}
    	
    	return best;
    }
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
    
	@Override
    public void update(Guess guess, Answer answer) 
    {
    	
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
    		targeting = true;
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
    		targeting = false;
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
        // To be implemented.
    } // end of update()


    @Override
    public boolean noRemainingShips() 
    {
    	
    	 return true;
    } // end of noRemainingShips()

} // end of class ProbabilisticGuessPlayer
