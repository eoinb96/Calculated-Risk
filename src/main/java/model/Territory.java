/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Instances of this class are used to store attributes for each territory
//These instances are stored in the HashMap:
//ModelConstants.TERRITORRIES<String territoryName, Territory territory>

package model;

public class Territory {
	
	private double[] territoryCoordinates;		//X and Y coordinate for this territory
	private String continent;					//Continent which this territory is in
	private boolean isOccupied;					//True if players occupies it
	private Player occupier;					//Player which is occupying
	private int numberOfUnits;					//Number of units at this territory
	
	//Creates new territory
	public Territory(double territoryCoordinateX, double territoryCoordinateY, String continent) {
		this.territoryCoordinates = new double[2];
		this.territoryCoordinates[0] = territoryCoordinateX;
		this.territoryCoordinates[1] = territoryCoordinateY;
		this.continent = continent;
		this.isOccupied = false;
		this.occupier = null;
		this.numberOfUnits = 0;
	}
	
	public double[] getTerritoryCoordinates() {
		return this.territoryCoordinates;
	}
	
	public String getContinent() {
		return this.continent;
	}
	
	//Check if occupied or not
	public boolean isOccupied() {
		return this.isOccupied;
	}
	
	//Allocates player to territory / changes occupier and adds units to it
	public void setOccupier(Player occupier, int numberOfUnits) {
		this.occupier = occupier;
		this.isOccupied = true;
		this.numberOfUnits = numberOfUnits;
	}
	
	//Returns player instances which occupies this territory
	public Player getOccupier() {
		return this.occupier;
	}
	
	//adds units to territory and reduces occupiers unitNumber variables accordingly
	public void addFreeUnits(int numberOfUnits) {
		if (!isOccupied) {
			throw new TerritoryNotOccupiedException();
		}
		else {
			this.numberOfUnits += numberOfUnits;
			this.occupier.addFreeUnitsToTerritory(numberOfUnits);
		}
	}
	
	public void receiveUnits(int numberOfUnits) {
		this.numberOfUnits += numberOfUnits;
	}
	
	public void removeUnits(int numberOfUnits) {
		if (!isOccupied) {
			throw new TerritoryNotOccupiedException();
		}
		else {
			this.numberOfUnits -= numberOfUnits;
			this.occupier.removeUnits(numberOfUnits);
		}
	}
	
	public int getNumberOfUnits() {
		return this.numberOfUnits;
	}
}
