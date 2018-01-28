/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//This class is used to generate a random number for
//rolling the dice or allocating random territories
//to bots etc...

package model;

public class RandomNumberGenerator {
	
	//Start and end of range to generate random number within
	private int start;
	private int end;
	
	//Empty constructor for dice
	public RandomNumberGenerator() {
		this.start = 1;
		this.end = 6;
	}
	
	//For range of 0 to N, set 'start' = 0 and 'end' = N + 1
	//For range of 1 to N. set 'start' = 1 and 'end' = N
	public RandomNumberGenerator(int rangeStart, int rangeEnd) {
		this.start = rangeStart;
		this.end = rangeEnd;
	}
	
	//Both methods are the same, more clear to read 'roll dice' than 
	//'get random number' when using this class as a dice
	public int rollDice() {
		int numberRolled = (int)(Math.random() * 6) + 1;
		return numberRolled;
	}
	
	//Used to generate a random number when not a dice
	public int getRandomNumber() {
		int number = -1;
		if (start == 0) {
			number = (int)(Math.random() * end) + 1;
			number--;
		}
		else {
			number = (int)(Math.random() * end) + start;
		}
		return number;
	}
	
	//Reduces range by 1. Used in for arrayLists where items being chosen
	//are at index 0 to N and each one is removed when picked, making the indexes
	//0 to N-1
	public void reduceRange() {
		end--;
	}
}
