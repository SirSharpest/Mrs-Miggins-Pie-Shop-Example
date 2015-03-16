//import java.util.ArrayList;

/**
 * My first attempt at the UKTill 
 * Contains floats of various UK denominations
 * 
 * @author Mrs Miggins
 * @version February 2015
 */

public class UKTill {
	private DenominationFloat[] contents;
	 

	/**
	 * Builds an empty till
	 */
	public UKTill() {
		contents = new DenominationFloat[UKDenomination.values().length];

		// Initialise the array with empty floats for each denomination. This is
		// kind of having empty trays for each denomination in the till!
		for (UKDenomination denom : UKDenomination.values()) {
			DenominationFloat denomFloat = new DenominationFloat(denom, 0);
			contents[denom.ordinal()] = denomFloat;
		}
		
		 
	}

	/**
	 * Enables a user to add a denomination float to the Till. Will add a copy
	 *
	 * @param theFloat
	 *            the denomination float to be added to the Till as a copy
	 */
	public void addFloat(DenominationFloat theFloat) {
		// Add to existing float
		DenominationFloat currentFloat = contents[theFloat.getType().ordinal()];
		currentFloat.setQuantity(currentFloat.getQuantity()
				+ theFloat.getQuantity());
	}
	

	/**
	 * Enables a user to clear a denomination float from the Till.
	 *
	 * @param denominationType
	 *            Denomination float to be removed
	 */
	public void removeFloat(UKDenomination denominationType) {
		contents[denominationType.ordinal()].setQuantity(0);
	}

	/**
	 * Empties out the till and returns the contents
	 * @return An array containing the contents of the till, with
	 * each element dealing with a given UK denomination. If the denomination
	 * does not exist in the till then returns a DenominationFloat with zero
	 * quantity.
	 */
	public DenominationFloat[] emptyTill() {
		DenominationFloat[] tillContents = new DenominationFloat[contents.length];
		for (int i = 0; i < contents.length; i++) {
			DenominationFloat denom = new DenominationFloat(
					contents[i].getType(), contents[i].getQuantity());
			tillContents[i] = denom;
			contents[i].setQuantity(0);
		}
		return tillContents;
	}
	
	
	
	/**
	 * Calculates the change to be returned from the till and decrements the
	 * till for each denomination
	 * 
	 * @param changeDue
	 *            The amount in pence we wish to extract from the till. 
	 * @return An array of denominations of different values that are in sum equal
	 *         to the provided changeDue value in pence. If there isn't
	 *         enough change in the till then display an error message saying 
	 *         that Mrs Miggins owes the customer some money!
	 */	
	public DenominationFloat[] getChange(int changeDue) {
		
		// ArrayList<DenominationFloat> changeX = new ArrayList<DenominationFloat>();
		
		
		DenominationFloat[] change  = new DenominationFloat[UKDenomination.values().length];

		// Initialise the array with empty floats for each denomination. This is
		// a way of being able to subtract a 12 array from a 12 array and the indices
		// will match up !
		for (UKDenomination denom : UKDenomination.values()) {
			DenominationFloat denomFloat = new DenominationFloat(denom, 0);
			change[denom.ordinal()] = denomFloat;
		}
		
		
		
		
		//This variable dictates if the value has been edited within the loop
		//if it has then we know that there's reason to try again to make the change
		//more accurate
		boolean hasValueChanged;
		
		//Loop to create change 
		do{
			
			//reset boolean 
			hasValueChanged = false;
			
			//this is the same for every statement of this loop
			//we check if this type of currency can be used as change
			//if so we take away its value from what is due
			//take it out of the till 
			//and add it to the change that will be returned
			if(changeDue >= UKDenomination.pound_50.getValue()
					&& contents[11].getQuantity() > 0 ){	
				changeDue -= 5000;
				contents[11].changeFloat(-1);
				change[11].changeFloat(1);
			}
			if(changeDue >= UKDenomination.pound_20.getValue()
					&& contents[10].getQuantity() > 0 ){	
				changeDue -= 2000;
				contents[10].changeFloat(-1);
				change[10].changeFloat(1);
				hasValueChanged = true; 
			}
			if(changeDue >= UKDenomination.pound_10.getValue()
					&& contents[9].getQuantity() > 0 ){	
				changeDue -= 1000;
				contents[9].changeFloat(-1);
				change[9].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pound_5.getValue()
					&& contents[8].getQuantity() > 0 ){	
				changeDue -= 500;
				contents[8].changeFloat(-1);
				change[8].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pound_2.getValue()
					&& contents[7].getQuantity() > 0 ){	
				changeDue -= 200;
				contents[7].changeFloat(-1);
				change[7].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pound_1.getValue()
					&& contents[6].getQuantity() > 0 ){	
				changeDue -= 100;
				contents[6].changeFloat(-1);
				change[6].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pence_50.getValue()
					&& contents[5].getQuantity() > 0 ){	
				changeDue -= 50;
				contents[5].changeFloat(-1);
				change[5].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pence_20.getValue()
					&& contents[4].getQuantity() > 0 ){	
				changeDue -= 20;
				contents[4].changeFloat(-1);
				change[4].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pence_10.getValue()
					&& contents[3].getQuantity() > 0 ){	
				changeDue -= 10;
				contents[3].changeFloat(-1);
				change[3].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pence_5.getValue()
					&& contents[2].getQuantity() > 0 ){	
				changeDue -= 5;
				contents[2].changeFloat(-1);
				change[2].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pence_2.getValue()
					&& contents[5].getQuantity() > 0 ){	
				changeDue -= 2;
				contents[1].changeFloat(-1);
				change[1].changeFloat(1);
				hasValueChanged = true;
			}
			if(changeDue >= UKDenomination.pence_1.getValue()
					&& contents[0].getQuantity() > 0 ){	
				changeDue -= 1;
				contents[0].changeFloat(-1);
				change[0].changeFloat(1);
				hasValueChanged = true;
			}
			
			
			
		}while(changeDue > 0 || hasValueChanged);
		
		//return the array of the change required 
		return change;
	}
	
	
	

	/**
	 * toString returns a formatted String representing denomination floats in
	 * the Till
	 *
	 * @return String a formatted representation of the Till contents
	 */
	public String toString() {
		// StringBuilder is a more efficient way of building Strings. The
		// problem with the String class is that String objects are immutable, i.e.
		// their contents cannot be changed. So every operation you run on a String object
		// (e.g. concatenation + operation) results in another String object being created
		// that holds the result of the operation. This is slow and takes up lots of memory. 
		// StringBuilder allows you to append to an existing StringBuilder.
		StringBuilder results = new StringBuilder();
		for (DenominationFloat f : contents) {
			results.append(f).append('\n');
		}
		return results.toString();
	}
	
	
}
