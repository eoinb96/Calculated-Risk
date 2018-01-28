/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Human and bot players have different attributes. 
//This enuration allows them to be differentiated for this reason
//as well as not waiting for user input if it is a bots turn etc...

package model;

public enum PlayerType {
	HUMAN,
	BOT;
}
