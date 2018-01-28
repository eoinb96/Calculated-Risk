/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Constants to be used in games logic

package util;

import java.util.HashMap;
import java.util.Map;

import model.Player;
import model.Territory;

public class ModelConstants {
	
	public static final int NUM_PLAYERS = 2;
	public static final int NUM_NEUTRALS = 4;
	public static final int NUM_PLAYERS_PLUS_NEUTRALS = NUM_PLAYERS + NUM_NEUTRALS;
	
	public static Map<String, Player> PLAYERS = new HashMap<>();
	public static Map<String, Territory> TERRITORIES = new HashMap<>();

	public static final int INIT_UNITS_PLAYER = 36;
	public static final int INIT_UNITS_NEUTRAL = 24;
	
	public static final int[] NUM_REINFORCEMENT_PER_TERRITORY_CARD = {4, 6, 8, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
		
	public static final int NUM_COUNTRIES = 42;
	public static final int INIT_COUNTRIES_PLAYER = 9;
	public static final int INIT_COUNTRIES_NEUTRAL = 6;
		
	public static final String[] COUNTRY_NAMES = {
		"Ontario","Quebec","NW Territory","Alberta","Greenland","E United States","W United States","Central America","Alaska",
		"Great Britain","W Europe","S Europe","Ukraine","N Europe","Iceland","Scandinavia",
		"Afghanistan","India","Middle East","Japan","Ural","Yakutsk","Kamchatka","Siam","Irkutsk","Siberia","Mongolia","China",
		"E Australia","New Guinea","W Australia","Indonesia",
		"Venezuela","Peru","Brazil","Argentina",
		"Congo","N Africa","S Africa","Egypt","E Africa","Madagascar" };
	
	public static double[][] COUNTRY_COORD = {
			{250,195},     // 0
			{320,195},
			{165,130},
			{165,185},
			{415,80},
			{250,275},
			{175,255},
			{195,340},
			{65,125},
			{450,220},
			{460,285},      // 10
			{530,285},
			{610,195},
			{510,230},
			{445,150},
			{515,130},
			{690,265},
			{735,390},
			{610,370},
			{945,265},
			{730,200},      // 20
			{890,125},
			{990,130},
			{815,390},
			{850,200},
			{780,160},
			{870,255},
			{800,315},
			{935,590},
			{955,495},
			{840,590},       // 30
			{830,490},
			{235,410},
			{235,500},
			{320,490},
			{250,600},
			{525,520},
			{450,430},
			{525,620},
			{535,380},
			{595,480},        // 40
			{610,605}
		};
	
	public static final int[][] ADJACENT = { 
			{4,1,5,6,3,2},    // 0
			{4,5,0},
			{4,0,3,8},
			{2,0,6,8},
			{14,1,0,2},
			{0,1,7,6}, 
			{3,0,5,7},
			{6,5,32},
			{2,3,22},
			{14,15,13,10},    
			{9,13,11,37},     // 10
			{13,12,18,39,10},
			{20,16,18,11,13,15},
			{15,12,11,10,9},
			{15,9,4},
			{12,13,14},
			{20,27,17,18,12}, 
			{16,27,23,18},
			{12,16,17,40,39,11},
			{26,22},
			{25,27,16,12},    // 20
			{22,24,25},        
			{8,19,26,24,21},
			{27,31,17},
			{21,22,26,25},
			{21,24,26,27,20},
			{24,22,19,27,25},
			{26,23,17,16,20,25},
			{29,30},          
			{28,30,31},
			{29,28,31},      // 30
			{23,29,30},
			{7,34,33},       
			{32,34,35},
			{32,37,35,33},
			{33,34},
			{37,40,38},      
			{10,11,39,40,36,34},
			{36,40,41},
			{11,18,40,37},
			{39,18,41,38,36,37},  //40
			{38,40}
		};
	
	public static final int NUM_CONTINENTS = 6;
	
	public static final int[] CONTINENT_VALUES = {5, 5, 7, 2, 2, 3};
	
	public static final String[] CONTINENT_NAMES = {
			"N America",
			"Europe",
			"Asia",
			"Australia",
			"S America",
			"Africa" };
	
	public static final int[] CONTINENT_COUNTRIES = {9, 7, 12, 4, 4, 6};
	
	public static final int[] CONTINENT_IDS = {
			0,0,0,0,0,0,0,0,0,
			1,1,1,1,1,1,1,
			2,2,2,2,2,2,2,2,2,2,2,2,
			3,3,3,3,
			4,4,4,4,
			5,5,5,5,5,5 };
}
