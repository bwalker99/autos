/**
 * Example class. Shows two different methods of inserting data to a database using jdbc. 
 * 1) ResultSet. Handy for updating or inserting. 
 * 2) Prepared statement. 
 * BW Jul 2008.
 */
package ca.bob.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tester {
	private java.sql.Connection conn = null;
	 private String dsn;
	 private String tablename = "tester";
	 TableDescription TD;

	  public Tester(String adsn) {
	  this.dsn = adsn;
	  }

	
	 public static void main (String args []) 	  {
	  if (args.length < 1) {
	    System.out.println("Usage: Tester <DSN Name> <DropTestTable>\n");
	    System.exit(0);
	    }
	  System.out.println("Tester. DSN: " + args[0] + " " + args.length);
	  Tester T = new Tester(args[0]);
	  if (!T.connect()) {
	    System.out.println("Error connecting to database.");
	    System.exit(1);
	    }

	  T.create();
	  T.save1(1000);
	  T.show();
	  T.save2(2000);
	  T.show();
	  if (args.length > 1)
	     T.drop();
	 T.close(); 
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
		  try { 	 conn.close();  }
		  catch (SQLException e) { 
			  System.out.println("Error closing connection:" + e.getMessage());
		   }
	  }
		 
		 
private void create() { 
	System.out.println("Creating table : " + tablename);
try { 
	Statement st = conn.createStatement();
	st.execute("create table " + tablename + " ( id int, description varchar(32))");
	TD = new TableDescription(conn);
	TD.show(tablename,0);
}

catch (SQLException e) { 
    System.out.println(this.getClass().getName() + ":" +  e.getMessage());
    e.printStackTrace();
  }
}

/**
 * Insert/Update. Can do both. 
 * @param int_index
 */
private void save1(int int_index) {
 	
  boolean exists = false;
  String sql = "SELECT " + tablename + ".*  FROM " + tablename + "  where id = " + int_index;
  
  System.out.println("\nInserting to table with ResultSet");
  
  // First entry. Insert of it doesn't exist. Update if it does.  
  try {        
       Statement st1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
       ResultSet RS = st1.executeQuery(sql); 
       if  ( RS.next()) { 
    	   System.out.println("** Existing record will be updated: Current values: " +  RS.getInt("id") + " " + RS.getString("description"));
    	   exists = true;
         }
       else {
         RS.moveToInsertRow();
         RS.updateInt("id", int_index);  
          }
       // Always update description.
       RS.updateString("description",new java.util.Date().toString());

       if (exists) 
         RS.updateRow();
       else  
         RS.insertRow();
       
  // Second entry. Random id. Always an insert. 
       RS.moveToInsertRow();
       java.util.Random generator = new java.util. Random();
       RS.updateInt("id", generator.nextInt());
       RS.updateString("description",new java.util.Date().toString());
       RS.insertRow();

       conn.commit();  // Saves two inserts/updates
       RS.close(); 
       st1.close(); 
     }
     catch (SQLException e) { 
       System.out.println(this.getClass().getName() + ":" + e.getMessage());
       e.printStackTrace();
     }
  }

/*
 * Inserting with a prepared statement. Would need more work to do an update.
 */
private void save2(int int_index) {
	System.out.println("\nInserting to table with Prepared Statement. Not trying to do an update.");
	try { 
		PreparedStatement psx = conn.prepareStatement("insert into " + tablename + " values (?,?)");
		psx.setInt(1,int_index);
		psx.setString(2,new java.util.Date().toString());
		psx.execute();

		java.util.Random generator = new java.util. Random();
		psx.setInt(1,generator.nextInt());
		psx.setString(2,new java.util.Date().toString());
		psx.execute();
		
 	   conn.commit();	
	}
	catch (SQLException e) { 
		System.out.println(this.getClass().getName() + ":" + e.getMessage());
	}		
}


private void drop() { 
	System.out.println("Dropping table : " + tablename);
	try { 
		Statement st = conn.createStatement();
		st.execute("drop table " + tablename);
	}
	catch (SQLException e) { 
	    System.out.println(this.getClass().getName() + ":" +  e.getMessage());
	    e.printStackTrace();
	  }
	}
	

private void show() {
 	System.out.println("\nShow entries for table: " + tablename);
	  String sql = "SELECT " + tablename + ".*  FROM " + tablename;	   
	  try {        
	       Statement st = conn.createStatement();
	       ResultSet rs = st.executeQuery(sql);
	       int k = 0;
	       while  ( rs.next()) {  
              System.out.println("Row=" + k + "  id=" + rs.getInt("id") + "  Description = " + rs.getString("description"));
              k++;
	       }
          rs.close(); 
          st.close(); 	       
	       }
	     catch (SQLException e) { 
	       System.out.println(this.getClass().getName() + ":" + e.getMessage());
	       e.printStackTrace();
	     }
	  }

}