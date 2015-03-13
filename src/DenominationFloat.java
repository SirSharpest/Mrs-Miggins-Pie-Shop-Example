
/**
 * Represents a float of a particular money denomination
 * @author Mrs Miggins
 * @version February 2015
 * DenominationFloat will hold a name ("pound"),
 *                  a value (100)
 *                  and a quantity (33)
 */

public class DenominationFloat {
	private UKDenomination name;
	
	private int quantity;
	
	public DenominationFloat(UKDenomination n){
		this(n, 0);
	}
	
	/*
	 * Function to return a boolean
	 * which states if a DF has a anything in it 
	 */
	public boolean isEmpty(){
		
		if(this.quantity == 0){
			return true;
		}
		return false; 
	}
	
	
	public DenominationFloat(UKDenomination n, int q){
		name = n;
		quantity = q;
	}
	
	public UKDenomination getType() {
		return name;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Takes away a quantity of a DF 
	 * @param theFloat
	 * 			the DF to alter 
	 * @param quantity
	 * 			the quantity difference in integer value
	 */
	public void subtractFloat(int iToSubtract){
		
		this.quantity =- iToSubtract;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DenominationFloat other = (DenominationFloat) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + " x " + quantity;
	}
	
}