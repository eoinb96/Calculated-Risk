/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Class to hold useful methods to be used on the games Hashmaps

package util;

import java.util.Map;
import java.util.Objects;

public class HashMapUtilities {
	
	//Returns the key of a map given its value
	public static <T, E> T getKeyFromValue(Map<T, E> map, E value) {
	    for (Map.Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
