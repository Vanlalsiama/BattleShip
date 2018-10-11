package player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import player.ProbabilisticGuessPlayer.DiffShips;
//import player.RandomGuessPlayer.OwnShip;
import ship.AircraftCarrier;
import ship.Cruiser;
import ship.Frigate;
import ship.PatrolCraft;
import ship.Ship;
import ship.Submarine;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

import java.util.LinkedList;

/**
 * Greedy guess player (task B). Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class GreedyGuessPlayer implements Player {
	private ArrayList<Coordinate> hits = new ArrayList<Coordinate>();
	private Coordinate firstHit;
	private Coordinate lastHit;
	private Coordinate boardSize;
	private World world;
	private int lastX = 1;
	private int lastY = 0;
	private int firstCount=0;

	private int rowSize;
	private int clnSize;
	private ArrayList<Coordinate> huntHits = new ArrayList<Coordinate>();
	//private LinkedList<Coordinate> huntHits = new LinkedList<Coordinate>();
	//private HashSet<Coordinate> visited = new HashSet<Coordinate>();
	private int direction = 0;
	private int len = 0;
	private boolean aircraft, cruiser, frigate, patrol, sub;
	private ArrayList<ShipLocation> shipLocations;
	private ArrayList<Coordinate> shots;

	public class DiffShips { 
		public DiffShips() {}
		Ship ship = null;
		int[] rowCdns = { -1, -1, -1, -1, -1, -1 };
		int[] clnCdns = { -1, -1, -1, -1, -1, -1 };
		boolean[] isShunk = { true, true, true, true, true, true };

	}
	DiffShips[] diffShips = new DiffShips[10];

	@Override
	public void initialisePlayer(World world) {
		Random random = new Random();
		

		this.clnSize = world.numColumn;
		this.rowSize = world.numRow;
		this.world = world;
		this.rowSize = world.numRow;
		this.clnSize = world.numColumn;
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

		AircraftCarrier aircraft1 = new AircraftCarrier();
		Cruiser cruiser1 = new Cruiser();
		Frigate frigate1 = new Frigate();
		PatrolCraft patrol1 = new PatrolCraft();
		Submarine sub1 = new Submarine();
		// instantiates each ship type
		int i=0;
        for(ShipLocation location : shipLocations){
        	   diffShips[i] = new DiffShips();
        		   System.out.println(i);
        	   if(i == 0)
        	   {
        		  diffShips[i].ship = aircraft1;
        	   }
        	   else if(i == 1)
        	   {
        		  diffShips[i].ship = frigate1;
        	   }
        	   else if(i == 2)
        	   {
        		  diffShips[i].ship = sub1;
        	   }
        	   else if(i == 3)
        	   {
        		  diffShips[i].ship = cruiser1;
        	   }
        	   else
        	   {
        		  diffShips[i].ship = patrol1;
        	   }

        				for (int j = 0; j < diffShips[i].ship.len()*diffShips[i].ship.width(); j++) {
        					diffShips[i].rowCdns[j] = location.coordinates.get(j).row;
        					diffShips[i].clnCdns[j] = location.coordinates.get(j).column;
        					diffShips[i].isShunk[j] = false;
        					
        					}
        				i = i+1;
        }
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {

		Answer localAnswer = new Answer();
    	localAnswer.shipSunk = null;
        		for (int i = 0; i < 5; i++) {			
        			for (int j = 0; j < diffShips[i].ship.len()*diffShips[i].ship.width(); j++) {
        				if ((guess.row == diffShips[i].rowCdns[j]) && (guess.column == diffShips[i].clnCdns[j])) {
        					localAnswer.isHit = true;
        					diffShips[i].isShunk[j] = true;
        					int k = 1;
        					for (int m = 0; m < diffShips[i].ship.len()*diffShips[i].ship.width(); m++) {
        						if (diffShips[i].isShunk[m] == false) k = 0;
        					}
        					if (k != 0) {
        						localAnswer.shipSunk = diffShips[i].ship;
        					}
        					return localAnswer;
        				}
        			}
        		}
        		return localAnswer;
	} // end of getAnswer()

	@Override
	public Guess makeGuess() {
		Guess guess = new Guess();

		guess.column = lastX;
		guess.row = lastY;

		if (huntHits.size() > 0) {

			// targeting mode
			if(shots.contains(lastHit) == false || hits.contains(lastHit) == false) {
			switch (direction) {
			case 0: // east
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " E" );
				guess.column = lastHit.column + 1;
				guess.row = lastHit.row;

				break;
			case 1: // north
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " N" );

				guess.column = lastHit.column;
				guess.row = lastHit.row + 1;

				break;
			case 2: // west
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " W" );
				guess.column = lastHit.column - 1;
				guess.row = lastHit.row;
				break;
			case 3: // south
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " S" );
				guess.column = lastHit.column;
				guess.row = lastHit.row - 1;
				break;
				}
			}
		} else {

			// Hunting mode
			
			if (lastX == clnSize-1) {
				lastY++;
				lastX= 0;
			} else if (lastX == clnSize - 2) {
				lastY++;
				lastX = 1;
			} else {
				lastX += 2;
			}
			
		}
		
		return guess;
	}

	@Override
	public void update(Guess guess, Answer answer) {
		if (answer.isHit == true && answer.shipSunk == null) {
			lastHit = world.new Coordinate();
			lastHit.row = guess.row;
			lastHit.column = guess.column;

			if (firstHit == null) {
				firstHit = world.new Coordinate();
				firstHit = lastHit;
			}

			hits.add(lastHit);
			huntHits.add(lastHit);
			len++;
			System.out.println(Integer.toString(len) + " len");
		}

		if (answer.isHit == false) {
			if (huntHits.size() > 0) {
				if (direction == 3) {
					lastHit.column = huntHits.get(0).column;
					lastHit.row = huntHits.get(0).row;
					firstHit = lastHit;
					huntHits.remove(0);
					direction = 0;
					len = 0;
				} else {
					direction++;
					len = 0;
				}
			}

			Coordinate miss = world.new Coordinate();
			miss.row = guess.row;
			miss.column = guess.column;
			shots.add(miss);

		}

		if (answer.isHit == true && answer.shipSunk != null) {
			Coordinate sinkHit = world.new Coordinate();
			sinkHit.row = guess.row;
			sinkHit.column = guess.column;
			hits.add(sinkHit);
			huntHits.clear();
			if (answer.shipSunk.name().equals("AircraftCarrier")) {
				aircraft = false;
			}
			if (answer.shipSunk.name().equals("Cruiser")) {
				cruiser = false;
			}
			if (answer.shipSunk.name().equals("Frigate")) {
				frigate = false;
			}
			if (answer.shipSunk.name().equals("PatrolCraft")) {
				patrol = false;
			}
			if (answer.shipSunk.name().equals("Submarine")) {
				sub = false;
			}
			direction = 0;
			len = 0;
			firstHit = null;
		}

	} // end of update()

	@Override
	public boolean noRemainingShips() {
		for (int i = 0; i < 5; i++) {
    		for (int j = 0; j < diffShips[i].ship.len(); j++) {
    		if (diffShips[i].isShunk[j] == false)
    		return false;
    		}
    		}
    	 return true;

	} // end of noRemainingShips()

} // end of class GreedyGuessPlayer
