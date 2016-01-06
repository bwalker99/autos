package ca.bob.autos.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

import ca.bob.autos.data.Auto;
import ca.bob.autos.model.ModelAuto;

/**
 * This servlet controller returns a list autos.<br/> 
 */
public class AutoLister extends  javax.servlet.http.HttpServlet {
	
	  public void init() throws ServletException {
	      // Do required initialization, if needed. 
	  }

	  public void doGet(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException	  {
	    doPost(request,response); // No differentiation between post and get. Not for production systems.
	  }
	  
	  public void doPost(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException	  {
	 
	java.sql.Connection conn = null; 

	// Create an Array that will be returned to the view. Note: blank to start. 
	java.util.List<Auto> V = new java.util.ArrayList<Auto>();	  

	  try {
		// Create database connection. Parameters defined in the web server. For Tomcat: ...conf/context.xml
		  conn = ca.bob.sql.JNDI.getConnection("Autos");  
		  if (conn == null)    		 
		     throw new ServletException("Error connecting to database."); // ugly message. 
		  
	      ModelAuto MA = new ModelAuto(conn);
		  
		  PreparedStatement ps = conn.prepareStatement("SELECT id FROM autos order by 1 ");
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  V.add(MA.get(rs.getInt("id")));
		  }
		  rs.close();
		  ps.close();
	  }	  
	  catch (SQLException e) { 
		  e.printStackTrace();
		 throw new ServletException(e);  // A not very nice message. 
	  }
	  finally {     // Important. Always close database. Note that it doesn't really close. Connection are cached by webserver. 
		  if ( conn != null) {
		    try {    conn.close(); }
		    catch (SQLException e) { } 
		    conn = null;
	    }
	 }

// Put the list of autos on the Request object to be processed by the jsp view.	  
   request.setAttribute("Autos",V);
   
   // Forward to view
   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/autolist.jsp");
   dispatcher.forward(request,response);

	   
   }
}
