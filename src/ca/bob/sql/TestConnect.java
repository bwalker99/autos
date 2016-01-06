package ca.bob.sql;

import java.sql.SQLException;
import java.util.*;

public class TestConnect {
	
	
	private java.sql.Connection conn = null;
	 static String dsn;
	 TableDescription TD;
	 static long testlength,reportinterval;

	  public TestConnect(String adsn) {
	  this.dsn = adsn;
	  }

	
	 public static void main (String args []) 	  {
	  if (args.length < 1) {
	    System.out.println("Usage: TestConnect <DSN Name> <Test Length> <Report Interval>");
	    System.out.println("    Times should be in seconds.");
	    System.exit(0);
	    }
	  dsn = args[0];
	  testlength = 1000 * 60 * 60 * 2;  // Test length default of 2 hours.
	  reportinterval = 1000 * 60 * 15; // Report interval default of 15 minutes.	  

	  if (args.length > 1) 
		  testlength = Integer.parseInt(args[1]) * 1000;

	  if (args.length > 2) 
		  reportinterval = Integer.parseInt(args[2]) * 1000;	  

	  System.out.println("TestConnect. DSN: " + args[0] + " TestLength=" + testlength / 1000 + " seconds. ReportInterval=" + reportinterval / 1000 + " seconds.");
		  
// Now just sit and loop and report time.
	  final java.text.DateFormat timeFmt = java.text.DateFormat.getTimeInstance(java.text.DateFormat.MEDIUM);

	  final Timer timer = new Timer();
	
	  /**
	   * Note. This is a nested class that extends TimerTask. 
	   * It is commonly called an 'inline class' as it has no separate definition. 
	   * Note the ending  '};'  
	   */
	  TimerTask displayTime = new TimerTask() {
		      final long starttime = System.currentTimeMillis();
		      final long endtime = starttime + testlength; 
			  TestConnect T2 = new TestConnect(dsn);
			  boolean first = true;
		      public void run() {
		    	  if (first) { 
		    		  T2.connect();
		    		  T2.testConnection();
		    		  first = false;
		    	  }
		    	  
		    	  System.out.println(timeFmt.format(new Date()));
		    	  
		    	  if (System.currentTimeMillis() > endtime) { 
			    	  System.out.println("Stopping...");
		    		  timer.cancel();		    	  
		    		  T2.testConnection();
		    		  T2.close();
		    	  }
		    	  } 
		};

	  timer.schedule(displayTime,0,reportinterval);

	  }
	  
	   
	 private boolean connect() {
	     conn = Jdbc.getConnection(dsn);
	     if (conn == null) {
	        System.out.println("Trying for JNDI connection.");
	        conn = JNDI.getConnection(dsn);  // Try it this way now!!!
	        if (conn == null) {
	          System.out.println("Error getting database connection.");          
	          return false;
	          }
	      }  
	    return true;
	  }

private void close() { 
	  System.out.println("Closing database connection");	
		  try { 	 conn.close();  }
		  catch (SQLException e) { 
			  System.out.println("Error closing connection:" + e.getMessage());
		   }
	  }

private void testConnection() {
	try { 
		java.sql.Statement st = conn.createStatement(); 
		// java.sql.ResultSet rs = st.executeQuery("select sysdate from dual");  // Note: Test is for Oracle only.
		java.sql.ResultSet rs = st.executeQuery("select 1");  // Note: Tests is for SQL Server
		String now = "database read failed.";
		if (rs.next())
		   now = rs.getString(1);    // Should convert date to String.
		System.out.println("Results of database query: " +  now);
		rs.close();
		st.close();
	}
	catch (SQLException e) { 
		  System.out.println("Error performing database test:" + e.getMessage());		
	}
}
}
