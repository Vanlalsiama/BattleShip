package player;
import ship.Ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.sun.javafx.collections.MappingChange.Map;

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
	
	private World world;
	//private boolean isVisual=true;
	private int player;
	private ArrayList<ShipLocation> shipLocations;
    private ArrayList<Coordinate> shots;
    HashMap<Ship, ArrayList<Coordinate>> hit = new HashMap<Ship, ArrayList<Coordinate>>();
    private boolean updateShot;
    private boolean isHit=false;
    private Ship shipShunk=null;
    
    @Override
    public void initialisePlayer(World world) {
        this.world=world;
        this.world.shipLocations=world.shipLocations;
        this.shots=world.shots;
       
        
    } // end of initialisePlayer()

    /**/
    
    @Override
    public Answer getAnswer(Guess guess) {	
        Answer answer = new Answer();	
    		for(ShipLocation ship : world.shipLocations){
    			for (Coordinate c : ship.coordinates){
    				boolean d=c.column == guess.column && c.row == guess.row;
    				System.out.println(d);
    						if(d){
    					//this.hit.get(ship.ship).add(c);
    					answer.isHit=true;
    					/*
    					if(this.hit.get(ship.ship).size() == (ship.coordinates).size()){
    						answer.isHit=true;
    					}
    					else{
    						answer.isHit=false;
    					}
    					*/
    				}
    						else{
    							answer.isHit=false;
    						}
    			}
    		}
    		
    		
        return answer;
} // end of getAnswer()


    @Override
    public Guess makeGuess() {
    	Random rand = new Random();
    	Guess guess = new Guess();
    	guess.column = rand.nextInt(9) + 0;
    	guess.row = rand.nextInt(9) + 0;
    	return guess;
    	
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        System.out.println(answer);
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.

        // dummy return
        return true;
    } // end of noRemainingShips()

} // end of class RandomGuessPlayer
