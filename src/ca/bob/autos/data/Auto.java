package ca.bob.autos.data;

/**
 * Part of the mvc sample application.
 * Define attributes of one Automobile. Typically thought of as a bean.
 * 
 * @author bwalker
 *
 */

public class Auto {
	private int id;  // Database PK field.
	private String make; // the make of the Car. ie GM, Lada
	private String model;  // The model of the Car. ie 
	private String colour; // Say no more. 
	private int cost;   // Cost of the car as in integer. Implied two decimal place. 

/**
 * blank constructor. Sets all fields to blank.
 */
public Auto() {
	id =-1;
	make = "";
	model="";
	colour="";
	cost = 0;
 }

	// Standard java bean getters and setters.
public void setId(int i) { id = i; }
public void setMake(String s) { make = s; }
public void setModel(String s) { model = s; }
public void setColour(String s) { colour = s; }
public void setCost(int i) { cost = i; }
	
public int getId() { return id; }
public String getMake() { return make; }
public String getModel() { return model; }
public String getColour() { return colour; }
public int getCost() { return cost/100; }

/**
 * Calculated return value that formats the cost.
 * @return Returns cost as formatted string. Eg $999.99
 */
public String getFormattedCost() {
	String retval = "";
	String temp = "" + cost; // convert to string
	if (cost >= 100)    { // make sure car costs more than $1.00
		int len = temp.length();
		retval = "$" + temp.substring(0,len - 2 ) + "." + temp.substring(len-2);		
	}
	else retval = temp;
	
	return retval;
 }

public String toString() { 
	return  
	 "  id=" + id +
	 "  make=" + make + 
	 "  model=" + model + 
	 "  colour=" + colour +
	 "  cost=" + getFormattedCost();
 }
}


