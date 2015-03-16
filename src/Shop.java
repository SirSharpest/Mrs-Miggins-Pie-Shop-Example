import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

////////////////////////////////////////////////////////////////
/////
////		TODO FIX ALL NAMING CONVENSIONS
////		ADD str - i - bl for basic variables
////
////////////////////////////////////////////////////////////////


/**
 * This is the main class - the program is run by java Shop
 *
 * @author Mrs Miggins
 * @version 1.0 (22nd February 2015)
 */

public class Shop {

	private String shopName;
	private Scanner scan;
	private UKTill till;
	private ArrayList<Item> shopItems;
	private int totalCostDue; // Store as pence
	private boolean isSaleProcessed = false; 
	
	//if running from eclipse then use "./" if running from commandline "../"
	private static final String SHOP_STOCK_DATA_FILE = "../stock.txt";
	private static final String SHOP_TILL_DATA_FILE = "../till.txt";

	//private backups of till and items in case of cancelled sale
	private UKTill backupTill; 
	private ArrayList<Item> backupShopItems; 
	
	public Shop(String name) {
		shopName = name;
		till = new UKTill();
		scan = new Scanner(System.in);
		shopItems = new ArrayList<Item>();
		
		
	}

	/**
	 * Using stockShop an owner can add various goods to the system. We can then
	 * can add an item, set the price and set the stock level.
	 */
	public void stockShop() {		

		do{
			//variables used to construct the temp item
			String strBarcode, strName, strPrice, strQuantity; 
			 
			
			//ask user for barcode 
			System.out.println("Please enter barcode of item"); 
			strBarcode = scan.nextLine(); 
			
			//check that only numbers are in the barcode
			//if the string contains anything but 0-9 then try again
			while(!strBarcode.matches("[0-9]+")){
				System.out.println("Please enter numbers only for a barcode");
				System.out.println("Try again"); 
				strBarcode = scan.nextLine();
			}
			
			//check that barcode ID is unique
			while(isExistingID(strBarcode)){
				System.out.println("Please enter in only unique barcodes");
				System.out.println("Try again");
				strBarcode = scan.nextLine();
			}
			
			//ask user for name of item
			System.out.println("Please enter name of item");
			strName = scan.nextLine();
			
			//check for illegal characters that might break program 
			if(strName.contains(":")){
				//replacing :  characters with a -
				String strTmp = strName.replaceAll(":", "-");
				strName = strTmp; 
			}
			//get cost of item from user
			System.out.println("Please enter cost of item in pennies");
			strPrice = scan.nextLine();
			
			//ensure that the price is entered in numbers with no characters
			while(!strPrice.matches("[0-9]+")){
				System.out.println("Please enter numbers only for price");
				System.out.println("Try again"); 
				strPrice = scan.nextLine();
			}
			
			//get the amount of items from user
			System.out.println("Please enter quantity of item");
			strQuantity = scan.nextLine();
			
			//ensure that the quantity given is only an integer value
			while(!strQuantity.matches("[0-9]+")){
				System.out.println("Please enter numbers only for the quantity");
				System.out.println("Try again"); 
				strQuantity = scan.nextLine();
			}
			
			//creating a temporary item and converting the strings 
			//used for quantity and price to integer values by casting
			Item tmpItem = new Item(strBarcode, strName, 
					Integer.parseInt(strPrice), Integer.parseInt(strQuantity)); 
			
			//confirming with the user that the entered info is correct
			System.out.println("Continue with this information correct: " + tmpItem.toString());
			
			//if info is correct then add to stock list
			if(doContinue()){
				shopItems.add(tmpItem);
				System.out.println("Item added to stock");
			}else{
				System.out.println("Item not added");
			}

			
		//continue loop if the user wishes or else break
		}while(doContinue());
		

	}
	

	/**
	 * Using startTill, an owner can add the various denomination floats to the till
	 * specifying the name, value and quantity of each item (If she has 33
	 * "10p pieces", each of which is worth 10, she enters "10p piece", 10, 33
	 * (one separate lines)).
	 */
	public void startTill() {
		do {
			UKDenomination ct = getDenominationType();
			int nc = getInt("Number of these coins: ");
			DenominationFloat m = new DenominationFloat(ct, nc);
			till.addFloat(m);

			System.out.println("Denomination floats entered into till: " + m);
		} while (doContinue());
	}

	

	/**
	 * Using runTill, an owner can sell items. Customers put in their order, the
	 * system then tells her how much to charge.
	 */
	public void runTill() {
		
		//set variable to false so we can track if sale is completed 
		isSaleProcessed = false; 
		
		
		//using nested loop to allow for multiple input
		//of multiple items up for sale at once
		do{
			
			//using this boolean for loop condition of input
			boolean isValidInput = false;
			//Variable to hold ID of sold item
			String id; 
			
			//get the barcode of the item from the user 
			do{
				System.out.println("Please enter in barcode of item"); 
				id = scan.nextLine(); 
				
				//check if is existing barcode
				if(!isExistingID(id)){
					System.out.println("Invalid barcode");
				}
				
			}while(!isExistingID(id));
			
			//get the index of the item that we would like to sell 
			int index = getItemIndex(id);
			
			//variable to hold quantity
			int quantity = 0;
			
			//get the quantity from the user
			do{
				isValidInput = false;
				
				System.out.println("Please enter the quantity you'd like to purchase");
				System.out.println("There are " + shopItems.get(index).getQuanity() + " in stock currently");
				String temp = (scan.nextLine()); 
				
				//ensure that the quantity given is only an integer value
				if(!temp.matches("[0-9]+")){
					System.out.println("Please enter numbers only for the quantity");
					//try again
					continue; 
				}
				if(Integer.parseInt(temp) > shopItems.get(index).getQuanity()){
					System.out.println("Please ensure that you have choosen a quantity that we can provide");
					//try again
					continue; 
				}
				//this input is valid therefore set it and break loop 
				else{
					quantity = Integer.parseInt(temp);
					isValidInput = true; 
				}
				
			//run loop while input is not valid 
			}while(!isValidInput);
			
			
			//remove the quantity of items from stock and proceed
				//variable to hold old quantity temp
			int oldQuant = shopItems.get(index).getQuanity();
			shopItems.get(index).setQuanity(oldQuant - quantity);
			
			//
			totalCostDue += (shopItems.get(index).getCost()*quantity); 
			
			//ask user if data is correct and output price
			System.out.println("Order is for: " + shopItems.get(index).getName() + " x " + quantity);
			System.out.println("The cost of this is: " + (shopItems.get(index).getCost()*quantity) );
					
 
			
		}while(doContinue());
	
		System.out.println("Your total cost is now: " + totalCostDue);


		
		

	}	
	


	/**
	 * Using getChange, an owner can tell the system how much of each
	 * denomination she has been given by the customer and the till tells her
	 * what to giveback.
	 */
	public void getChange() {
		System.out.println("Hand over the money!");
		
		do {
			
				//same method used previously to get input for the till from the owner
				//restructured to take in the payment 
				UKDenomination ct = getDenominationType();
				int nc = getInt("Number of these coins: ");
				DenominationFloat m = new DenominationFloat(ct, nc);
				till.addFloat(m);
				System.out.println("Denomination floats entered into till: " + m);
				//remove value of these coins from total cost
				totalCostDue -= (ct.getValue() * nc);
				
		} while (totalCostDue > 0);
		
		// Calculate change
		if (totalCostDue == 0){
			System.out.println("You provided the exact amount, thank you!");
		} else {
			
			DenominationFloat[] change = till.getChange(Math.abs(totalCostDue));
			System.out.println("Here is your change:");
			isSaleProcessed = true; 
			for (DenominationFloat m: change){
				if (m != null){
					System.out.println(m);
				}
			}
		}
	}

	/**
	 * Using getBalance it tells the owner what is left in the till
	 */
	public void getBalance() {
		//System.out.println(till);
		System.out.println(till);
		
		
	}
	
	/**
	 * Function to output a list of current stock 
	 */
	public void getStockList(){
		
		for(int index = 0; index < shopItems.size(); index++){
			
			System.out.println(shopItems.get(index).toString());
		}
	}

	/**
	 * runMenu provides the main menu to the shop allowing a user to select
	 * their required operation
	 */
	public void runMenu() {
		// This is the main menu which runs the whole shop

		String choice;
		do {
			printMenu();
			choice = scan.nextLine();

			switch (choice) {
			case "1":
				stockShop();
				break;
			case "2":
				startTill();
				break;
			case "3":
				runTill();
				break;
			case "4":
				getChange();
				break;
			case "5":
				getBalance();
				break;
			case "6":
				getStockList();
				break;
			case "7":
				System.out.println("Thankyou for running " + shopName
						+ " program");
				break;
			default:
				System.err.println("Incorrect choice entered");
			}
		} while (!choice.equals("7"));
	}

	

	/**
	 * Saves data to the shop database (stock and till)
	 * @exception IOException
	 *                thrown when file problems occur
	 */
	public void save() throws IOException {
		
		//if this is false
		//then we add back stock to the list
		//and take money back out of the till
		if(!isSaleProcessed){
			cancelSale(); 
		}
		
		//saving the till 
		saveTill();
		//saving the stock
		saveStock(); 
		
		
	}
	
	
	public void saveStock(){
		
		//this function saves only the stock and its
		//info to a .txt file, it is set to overwrite all previous
		//data, I have done this to ensure the correct format within
		//the file is kept 
		
		StringBuilder strStock = new StringBuilder();
		for(int index = 0; index < shopItems.size(); index++){
			
				strStock.append(shopItems.get(index).toString()).append('\n');
			
		}
		
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SHOP_STOCK_DATA_FILE
				, false)))) {
		    out.print(strStock);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		
		}
		
	}
	
	public void saveTill(){
		
		//The first part of this function saves only the till and its
		//contents to a .txt file, it is set to overwrite all previous
		//data, I have done this to ensure the correct format within
		//the file is kept 
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SHOP_TILL_DATA_FILE
				, false)))) {
		    out.println(till);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		
		}
		
	}
	
	/**
	 * Loads data from the shop database (stock and till)
	 * @exception IOException
	 *                thrown when file problems occur
	 */
	public void load() throws IOException {
		

		//load all of the till data 
			loadTill();
		//load all of the stock data
			loadStock();
		//set backup data
			backUpUnchangedData(); 
	  }
	
	
	
	/**
	 * Function to find the index location of the item 
	 * Whose barcode we have
	 * @param itemID
	 * 		ID reference of Item in the shopItems array list 
	 * @return
	 */
	private int getItemIndex(String itemID){
		
		//looping through until we get to the item which matches 
		for(int index = 0; index < shopItems.size(); index++){
			if(shopItems.get(index).getIdentifier().compareTo(itemID) == 0){
				return index; 
			}
		}
		
		System.out.println("Something went terribly wrong \nTry turnning machine off and on again");
		return -1; 
	}
	
	/**
	 * backup function to run after load
	 */
	private void backUpUnchangedData(){
		
		backupTill = till; 
		backupShopItems = shopItems; 
		
	}
	
	/**
	 * use function to restore from backup following cancelled sale
	 */
	private void cancelSale(){
		
		till = backupTill; 
		shopItems = backupShopItems;
	}

	
	
	private boolean doContinue() {
		
		System.out.println("Continue? (Y/N)");
		String answer = scan.nextLine().toUpperCase();
			
		return answer.equals("Y");


	}

	private int getInt(String message) {
		boolean correct = false;
		int result = 0;
		do {
			System.out.println(message);
			try {
				result = scan.nextInt();
				scan.nextLine();
				correct = true;
			} catch (InputMismatchException ime) {
				System.err.println("Please enter an number");
				scan.nextLine();
			}
		} while (!correct);
		return result;
	}
	
	@SuppressWarnings("unused")
	private void displayCost(String message, int amountInPence) {
		System.out.format("%s %d.%02d\n", message, amountInPence / 100, amountInPence % 100);
	}
	
	private UKDenomination getDenominationType() {
		UKDenomination result;
		do {
			System.out.println("Enter the denomination type. One of: ");
			for (UKDenomination denom: UKDenomination.values()){
				System.out.print(denom + " ");
			}
			String choice = scan.nextLine();
			result = UKDenomination.fromString(choice);
			if (result == null){
				System.err.println("Incorrect denomination entered. Try again!");
			}
		}while(result == null);
		return result;
	}

	private void printMenu() {
		
		
		
		System.out.println("Welcome to " + shopName + ". Please enter choice:");
		System.out.println("1 - Stock the shop");
		System.out.println("2 - Add coins to the till");
		System.out.println("3 - Process customer order");
		System.out.println("4 - Process customer payment");
		System.out.println("5 - Display till balance");
		System.out.println("6 - Display current stock info");
		System.out.println("7 - Exit shop program");
	}
	


		
	
	
	private void loadStock(){
		
		//going to store all of the files used in this
		ArrayList<String> strItems = new ArrayList<String>();
		
		
		//Reading in code 
		Scanner readStockData = null;
		try {
			readStockData = new Scanner (new File(SHOP_STOCK_DATA_FILE));
		} catch (FileNotFoundException e) {
			// Something went wrong loading the till
						System.err.println("Sorry but we were unable to load shop stock data: "
								+ e.getMessage());
		}
		
		//assigning delimiters of both newline and a ':'
		readStockData.useDelimiter(":|\\n");
		
	
		//loop to read all of the text file
		while (readStockData.hasNext()){
		   strItems.add(readStockData.next()); 	   
		}
		
		//finding the number of items by dividing by 4
		//as there are 4 properties per item
		//there will always be 4 properties 
		//or the files will not be saved
		int numOfItems = (strItems.size() / 4);
		
		
		
		//closing the reading file
		readStockData.close();
		
		//For this loop, 0 is the ID, 1 is the Name, 
		//2 is the price in pence and 3 is the quantity 
		//
		//This loop adds the read in data to the programs stock list
		for(int index = 0; index < numOfItems; index++){
			
			Item itmTemp = new Item(strItems.get(0+(4*index)), 
					strItems.get(1+(4*index)), 
					Integer.valueOf(strItems.get(2+(4*index))),
					Integer.valueOf(strItems.get(3+(4*index))));
			
			shopItems.add(itmTemp); 
			
			
		}

	}
	
	
	/**
	 * 
	 * This part loads the till data 
	 * it uses the scanner class to read info from the 
	 * external .txt file
	 * 
	 * created to shorten the load function
	 * and to make easier to debug individual file loading
	 * 
	 */
	private void loadTill(){
		
		//This array will hold all is read in from the scanner
		//from the text file 
		String[] readTillResults;
		readTillResults = new String[24]; 
		
		//Reading in code 
		Scanner readTillData = null;
		try {
			readTillData = new Scanner (new File(SHOP_TILL_DATA_FILE));
		} catch (FileNotFoundException e) {
			// Something went wrong loading the till
						System.err.println("Sorry but we were unable to load shop till data: "
								+ e.getMessage());
		}
		
		//assigning delimiters of both newline and a ':'
		readTillData.useDelimiter(" x |\\n");
		
		
		//index used to keep track of 
		//position of applying info read from file
		int index = 0; 
		//loop to read all of the text file
		while (readTillData.hasNext() && index < 24)
		{
		   readTillResults[index] = readTillData.next(); 
		   //increment index
		   index++; 
		   
		}
		//closing the reading file
		readTillData.close();
		
		//this uses a modified version of the already implemented 
		//getDenominationType function as well as the startTill function
		for(index = 0; index <  24; index+=2){
			UKDenomination ct = UKDenomination.fromString(readTillResults[index]);
			int nc = Integer.valueOf(readTillResults[index+1]);
			DenominationFloat m = new DenominationFloat(ct, nc);
			till.addFloat(m);
			
			
		}
		
		
	}

	/**
	 * Function which will check if the ID of an item already exists 
	 * in stock
	 * @param String strID
	 */
	private boolean isExistingID(String strID){
		
		for(int index = 0  ; index < shopItems.size(); index++){
			
			if(shopItems.get(index).getIdentifier().compareTo(strID) ==
					0){
				return true; 
			}
			
		}
	
		return false; 
	}
	
	
	public static void main(String[] args) {
		// Don't touch any of this code
		Shop migginsPieShop = new Shop("Mrs Miggins Pie Shop");
		try {
			migginsPieShop.load();
		} catch (IOException e) {
			// Something went wrong so start a new shop
			System.err.println("Sorry but we were unable to load shop data: "
					+ e.getMessage());
		}

		migginsPieShop.runMenu();
		
		
		try {
			migginsPieShop.save();
		} catch (IOException e) {
			System.err
					.println("Sorry but we just lost everything. Unable to save shop data: "
							+ e.getMessage());
		}
	}

}
