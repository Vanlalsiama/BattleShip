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
 * Random guess player (task A). Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class RandomGuessPlayer implements Player {

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
	// HashMap<Ship, ArrayList<Coordinate>> hit = new HashMap<Ship,
	// ArrayList<Coordinate>>();
	private boolean updateShot;
	private boolean isHit = false;
	private Ship shipShunk = null;
	private Coordinate firstHit;
	private Coordinate lastHit;
	int rowSize = 0;
	int clnSize = 0;
	private Ship ship;
	// OwnShip[] ownShips = new OwnShip[5];

	public class DiffShips {
		public DiffShips() {
		}

		Ship ship = null;
		int[] rowX = { -1, -1, -1, -1, -1, -1 };
		int[] rowY = { -1, -1, -1, -1, -1, -1 };
		boolean[] isShunk = { true, true, true, true, true, true };

	}

	DiffShips[] diffShips = new DiffShips[10];
	private static ArrayList<Coordinate> hits = new ArrayList<Coordinate>();
	boolean[][] toGuess;

	@Override
	public void initialisePlayer(World world) {
		this.world = world;
		this.rowSize=world.numRow;
		this.clnSize=world.numColumn;
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
			diffShips[i] = new DiffShips();

			if (i == 0) {
				diffShips[i].ship = aircraft1;
			} else if (i == 1) {
				diffShips[i].ship = frigate1;
			} else if (i == 2) {
				diffShips[i].ship = sub1;
			} else if (i == 3) {
				diffShips[i].ship = cruiser1;
			} else {
				diffShips[i].ship = patrol1;
			}

			for (int j = 0; j < diffShips[i].ship.len() * diffShips[i].ship.width(); j++) {
				diffShips[i].rowX[j] = location.coordinates.get(j).row;
				diffShips[i].rowY[j] = location.coordinates.get(j).column;
				diffShips[i].isShunk[j] = false;

			}
			i = i + 1;
		}

	} // end of initialisePlayer()


	@Override
	public Answer getAnswer(Guess guess) {
		Answer answer = new Answer();
		answer.shipSunk = null;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < diffShips[i].ship.len() * diffShips[i].ship.width(); j++) {
				if ((guess.row == diffShips[i].rowX[j]) && (guess.column == diffShips[i].rowY[j])) {
					answer.isHit = true;
					diffShips[i].isShunk[j] = true;
					int k = 1;
					for (int m = 0; m < diffShips[i].ship.len() * diffShips[i].ship.width(); m++) {
						if (diffShips[i].isShunk[m] == false)
							k = 0;
					}
					if (k != 0) {
						answer.shipSunk = diffShips[i].ship;
					}
					return answer;
				}
			}
		}
		return answer;
	} // end of getAnswer()

	@Override
	public Guess makeGuess() {

		Random random = new Random();
		Guess guess = new Guess();
		guess.row = random.nextInt(rowSize) + 0;
		guess.column = random.nextInt(clnSize) + 0;

		return guess;
	} // end of makeGuess()

	@Override
	public void update(Guess guess, Answer answer) {

		System.out.println(guess);

		if (answer.isHit == true && answer.shipSunk == null) {
			System.out.println("HIT!");
			lastHit = world.new Coordinate();
			lastHit.row = guess.row;
			lastHit.column = guess.column;
			if (firstHit == null) {
				firstHit = world.new Coordinate();
				firstHit = lastHit;
			}

			hits.add(lastHit);
		}
		if (answer.isHit == true && answer.shipSunk != null) {
			Coordinate sinkHit = world.new Coordinate();
			sinkHit.row = guess.row;
			sinkHit.column = guess.column;
			hits.add(sinkHit);
			System.out.print("HIT! & SUNK!");
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

			firstHit = null;
		}
		if (answer.isHit == false) {
			Coordinate miss = world.new Coordinate();
			miss.row = guess.row;
			miss.column = guess.column;
			shots.add(miss);
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

} // end of class RandomGuessPlayer
