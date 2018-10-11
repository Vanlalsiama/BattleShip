package player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

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
	private static ArrayList<Coordinate> hits = new ArrayList<Coordinate>();
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

	public class OwnShip {
		public OwnShip() {
		}

		Ship ship = null;
		int[] rowCdns = { -1, -1, -1, -1, -1, -1 };
		int[] clnCdns = { -1, -1, -1, -1, -1, -1 };
		boolean[] isdown = { true, true, true, true, true, true };

	}

	OwnShip[] ownShips = new OwnShip[10];

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
		int i = 0;
		for (ShipLocation location : shipLocations) {
			ownShips[i] = new OwnShip();

			if (i == 0) {
				ownShips[i].ship = aircraft1;
			} else if (i == 1) {
				ownShips[i].ship = frigate1;
			} else if (i == 2) {
				ownShips[i].ship = sub1;
			} else if (i == 3) {
				ownShips[i].ship = cruiser1;
			} else {
				ownShips[i].ship = patrol1;
			}

			for (int j = 0; j < ownShips[i].ship.len() * ownShips[i].ship.width(); j++) {
				ownShips[i].rowCdns[j] = location.coordinates.get(j).row;
				ownShips[i].clnCdns[j] = location.coordinates.get(j).column;
				ownShips[i].isdown[j] = false;

			}
			i = i + 1;
		}
	} // end of initialisePlayer()

	@Override
	public Answer getAnswer(Guess guess) {

		Answer answer = new Answer();
		/*
		 * answer.shipSunk = null; for (int i = 0; i < 5; i++) { for (int j = 0;
		 * j < ownShips[i].ship.len()*ownShips[i].ship.width(); j++) { if
		 * ((guess.row == ownShips[i].rowCdns[j]) && (guess.column ==
		 * ownShips[i].clnCdns[j])) { answer.isHit = true; ownShips[i].isdown[j]
		 * = true; int k = 1; for (int m = 0; m <
		 * ownShips[i].ship.len()*ownShips[i].ship.width(); m++) { if
		 * (ownShips[i].isdown[m] == false) k = 0; } if (k != 0) {
		 * answer.shipSunk = ownShips[i].ship; } return answer; } }}
		 */
		return answer;
	} // end of getAnswer()

	@Override
	public Guess makeGuess() {
		Guess guess = new Guess();

		guess.column = lastX;
		guess.row = lastY;

		if (huntHits.size() > 0) {

			// targeting mode
			switch (direction) {
			case 0: // east
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " E" );
				guess.column = huntHits.get(0).column + len;
				guess.row = huntHits.get(0).row;

				break;
			case 1: // north
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " N" );

				guess.column = huntHits.get(0).column;
				guess.row = huntHits.get(0).row + len;

				break;
			case 2: // west
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " W" );
				guess.column = huntHits.get(0).column - len;
				guess.row = huntHits.get(0).row;
				break;
			case 3: // south
				// System.out.println(Integer.toString(huntHits.get(0).row) +
				// ","
				// + Integer.toString(huntHits.get(0).column) + " S" );
				guess.column = huntHits.get(0).column;
				guess.row = huntHits.get(0).row - len;
				break;
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
					lastHit.row = huntHits.get(0).row + len;
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
		/*
		 * for (int i = 0; i < 5; i++) { for (int j = 0; j <
		 * ownShips[i].ship.len(); j++) { if (ownShips[i].isdown[j] == false)
		 * return false; } }
		 */
		return true;

	} // end of noRemainingShips()

} // end of class GreedyGuessPlayer
