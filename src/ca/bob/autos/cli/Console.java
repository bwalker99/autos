package ca.bob.autos.cli;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.bob.autos.model.ModelAuto;
import ca.bob.autos.data.Auto;

/** 
 * Standalone java server-side app to manage the Auto sample application. 
 * Uses the same 'model' and java beans as the Web version. 
 * @author rwalker
 *
 */
public class Console {

	
	public static void main(String args[]) { 
		System.out.println("Console (arglength=" + args.length + ")\n");
		for (int i = 0; i < args.length ; i++) { 
			System.out.println("" + i + "=" + args[i]);
		}
		if (args.length < 1) {
			help();
			System.exit(0);
		}
		// Standalong java programs work this way. Get an instance of the class and run a function in the class. 
		// Othewise everything has to be static.
		Console C = new Console();
		C.go(args);
	}
	
	/**
	 * Does all the work.
	 * @param args The arguments as passed into the program 
	 */
	public void go(String args[]) {
	java.sql.Connection conn = null;
			
			
	if (args[0].equalsIgnoreCase("list")) { 
		System.out.println("Listing Autos:\n\n");

		try {
		// Create database connection. Parameters defined in properties file for command line programs
	      conn = getConn();
		  ModelAuto MA = new ModelAuto(conn);  // same model as in the web version.
				  
		  PreparedStatement ps = conn.prepareStatement("SELECT id FROM autos order by 1 ");
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  // int id = rs.getInt("id"); 
			  // Auto A = (Auto)MA.get(id); 
			  // System.out.println(A.toString());
			  //   OR
			  System.out.println(MA.get(rs.getInt("id")).toString());
			 }
		 rs.close();
		 ps.close();
		}	  
		catch (SQLException e) { 
		  e.printStackTrace();
		  System.err.println(e.getMessage());
		 }
	}
		else if (args[0].equalsIgnoreCase("update")) {
			
			System.out.println("Update Autos:\n\n");

			try {
		      conn = getConn();
			  ModelAuto MA = new ModelAuto(conn);
			  // Create an Auto object from input parameters. Pass this to the model to save. 
			  Auto A = new Auto();	  
		      A.setId(Integer.parseInt(args[1]));  // if -1, insert new record. If > 0 should be an update.
		      A.setMake(args[2]);
		      A.setModel(args[3]);
		      A.setColour(args[4]);
		      A.setCost(Integer.parseInt(args[5]));
			  Auto A2 = (Auto)MA.saveFromAutoObject(A);
			  System.out.println(A2.toString());
			}	  
			catch (SQLException e) { 
			  e.printStackTrace();
			  System.err.println(e.getMessage());
			 }
			
			
		}	
	
						
		
	}	
		public static void help() { 
			System.out.println("Usage:");
			System.out.println("   Console list | update {id} {make} {model} {colour} {cost} ");
			System.out.println("   Example: Console list");
			System.out.println("   Example: Console update -1 Ford Fiesta Red 10000000 ($10,000) ");
		}
	
		
	private java.sql.Connection getConn() {
		java.sql.Connection conn = null;

		  try { 
			  conn =  ca.bob.sql.Jdbc.getConnection("db.properties");
		      conn.setAutoCommit(false);
		        } 
		  catch (SQLException e) { 
			  e.printStackTrace();
			  System.err.println(e.getMessage());			  
			  System.exit(1);       // Bad practice. How do we know it failed?
		  }
	return conn;	
	}

	}


