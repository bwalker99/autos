package ca.bob.autos.model;
/**
 * Sample Auto application. Example of a Helper or Model class that works with a java bean<br/>
 * Each bean (in this case, Auto.java) has a helper or model that reads, saves, and deletes the associated record from the database. 
 * These 'helpers' can be put into a Factory and/or constructed on the fly based on naming conventions. <br/><br/>
 * 
 * The underlying database tables are expected to have a RubyOnRails type of layout. That is, an identifying PK id number field.<br/>
 * The 'save' method can extract information from any object, as that is what is passed in. In a servlet environment, it is typically a servlet request object,
 *  and the information is extracted from a html form.
 *  
 *  Database connection is created outside this class and passed in. 
 */

import java.sql.*;

import ca.bob.autos.data.Auto;

public class ModelAuto {
	
	java.sql.Connection conn;   // default database connection. Initialized when instantiated.
	
	public ModelAuto(java.sql.Connection conn) throws SQLException { 
		this.conn = conn;
	}
	
	 /**
	  * Get info for one auto
	  * @param index The database PK of the auto you want.
	  */
	
	public  Auto get(int index)  throws SQLException {
		String sql = "select * from autos where id = ?";	
		Auto A = new Auto();				
			PreparedStatement ps = conn.prepareStatement(sql);			
			ps.setInt(1,index);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { 
				A.setId(rs.getInt("id"));				
				A.setMake(rs.getString("make"));
				A.setModel(rs.getString("model"));				
				A.setColour(rs.getString("colour"));
				A.setCost(rs.getInt("cost"));
			}
		rs.close();
		ps.close();
		
		return A; // Return the bean containing the data. 
	} 
	
	
	
	/**
	 * Save info based on data in html form.
	 * @param O An object that holds the data to be saved. Typically a html servlet request object, 
	 * @return An object that represents the data just saved. 
	 */
	public  Object save(Object O)   throws SQLException {
		Object retval = new Object();
		
		if (O instanceof javax.servlet.http.HttpServletRequest) { 
			retval = saveFromRequestObject((javax.servlet.http.HttpServletRequest)O);
		}
		else if (O instanceof Auto) {
			retval = saveFromAutoObject((Auto)O);
		}
		return retval;
	}
	/**
	 * Save auto info based on data in html form. 
	 * @return An object that represents the data just saved. 
	 */
	public  Object saveFromRequestObject(Object O)   throws SQLException { 		
		// The object to get data from could be anything. In this case it is a Servlet Request Object
		javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest)O;
	
		// Get index number of record. If -1, assume this is a new record. 
		String id = null;
		id = request.getParameter("id");
		int int_index = -1;
		boolean exists = false;
	    
		if (id != null) {
		  try {  int_index = Integer.parseInt(id); }
		  catch (NumberFormatException nfe) { 
			 throw new SQLException("ID  must be supplied.",nfe);  // TODO Message should come from elsewhere. 
	 	     }
		 }
	 	
	    // Using resultset, as we can treat most of the fields for updates and inserts the same way, 
			conn.setAutoCommit(false);    // for mysql
			Statement st1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet RS = st1.executeQuery("SELECT autos.*  FROM autos where id = " + id );	        	        
	
			exists = RS.next();   // If the record exists, moving to the next record will be true. 
	        if (!exists) {	        	
	        	RS.moveToInsertRow();
	        	//  Assume that the ID field will be auto-populated.   	        	
	        }
	
	        RS.updateString("make",changeToNull(request.getParameter("make")));
 	        RS.updateString("model",changeToNull(request.getParameter("model")));	
	        RS.updateString("colour",changeToNull(request.getParameter("colour")));
       
	        String temp=  request.getParameter("cost");	        
	        int cost = 0;
	        if (temp != null) 	        {
				// take the value that the user has entered, round to the nearest dollar and convert to cents
	        	cost = (Math.round(Float.parseFloat(temp))*100);   	
	        }
	         RS.updateInt("cost",cost);
	        
	        if (exists) 
	        	RS.updateRow();
	        else  
	        	RS.insertRow();	        

	        // Assumption. If this is a new record, the ID will be assigned by a database trigger. Must retrieve it here. 
		    if (!exists) { 
		      ResultSet RSind = st1.executeQuery("SELECT max(id) id FROM autos"); // This is safe, as have not yet committed, so database is locked.
		    	if (RSind != null) {
		      	   RSind.next();
		       	   int_index = RSind.getInt("id");   // Get new value assigned by database	     
		       	   RSind.close();
		        }
		    }	        
		    
	        conn.commit();
	        RS.close(); 
	        st1.close();
	        
		
   // The save function returns what it just saved as an object.		
	 return get(int_index);		

	}
	
	/**
	 * Save from an Auto object.
	 * @param A The Auto object passed in
	 * @return A saved version of the Auto object.
	 * @throws SQLException
	 */
	public  Object saveFromAutoObject(Auto A)   throws SQLException { 		
		int id = A.getId();
		boolean exists = false;
	    	 	
	    // Using resultset, as we can treat most of the fields for updates and inserts the same way, 
			conn.setAutoCommit(false);    // for mysql
			Statement st1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet RS = st1.executeQuery("SELECT autos.*  FROM autos where id = " + id );	        	        
	
			exists = RS.next();   // If the record exists, moving to the next record will be true. 
	        if (!exists) {	        	
	        	RS.moveToInsertRow();        	
	        }
	
	        RS.updateString("make",A.getMake());
 	        RS.updateString("model",A.getModel());	
	        RS.updateString("colour",A.getColour());
       
	         RS.updateInt("cost",A.getCost());
	        
	        if (exists) 
	        	RS.updateRow();
	        else  
	        	RS.insertRow();	        

	        // Assumption. If this is a new record, the ID will be assigned by a database trigger. Must retrieve it here. 
		    if (!exists) { 
		      ResultSet RSind = st1.executeQuery("SELECT max(id) id FROM autos"); // This is safe, as have not yet committed, so database is locked.
		    	if (RSind != null) {
		      	   RSind.next();
		       	   id = RSind.getInt("id");   // Get new value assigned by database	     
		       	   RSind.close();
		        }
		    }	        
		    
	        conn.commit();
	        RS.close(); 
	        st1.close();
	        
		
   // The save function returns what it just saved as an object.		
	 return get(id);		

	}
		
	
	
/**
 * Check if user input is an empty string.
 * If user input is empty string, change the input from empty string to null.
 * @param temp
 * @return tempNull
 * @author bnam
 */
	
private String changeToNull(String requestParm) {	
	String tempNull = requestParm;
    if (tempNull == null || tempNull.length() == 0) {
    	tempNull = null;     	
    }

return tempNull;
}	
	
	

/**
 * delete the request record.
 */	
	public  void delete(int index)   throws SQLException { 
		  PreparedStatement ps = conn.prepareStatement("Delete from autos where id = ?");      
  		  ps.setInt(1,index);
		  ps.executeUpdate();
		  conn.commit();
		  ps.close();
	    
   }	
}