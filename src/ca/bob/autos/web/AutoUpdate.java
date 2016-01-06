package ca.bob.autos.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.bob.autos.model.ModelAuto;


import java.io.IOException;
import java.sql.*;

import ca.bob.autos.data.Auto;

/**
 * Update servlet for Auto.  <br/>
 */
public class AutoUpdate extends  javax.servlet.http.HttpServlet {
	
	  public void init() throws ServletException {
	  }

	  public void doGet(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException	  {
	    doPost(request,response); // No differentiation between post and get. Not for production systems.
	  }
	  
	  public void doPost(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException	  {

	 java.sql.Connection conn = null;
	 
	 Auto A = new Auto();  // Blank. in case we need it.
	 request.setAttribute("Auto",A);   // put blank one in case we bail out.
	 
	  try {
		  conn = ca.bob.sql.JNDI.getConnection("Autos");
		  if (conn == null) 
			  throw new ServletException("Error connecting to database.");  
		  
		  // Create the Model and give it the database connection object.
		  ModelAuto MA = new ModelAuto(conn); 
		  A = (Auto) MA.save(request);     // Use the model to save
	  }
	  catch (SQLException e) { 
		  throw new ServletException(e);
	  }
	  finally { 
		  if (conn != null) { 
		     try {    conn.close(); }
		     catch (SQLException e) { } 
		     conn = null;
		   }
	  }	  
	  
	  // Two possible outcomes from an update: 1) List all, or 2) redisplay
	   // 1) Let's do this one...
	   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/list");
	   
	   //2
	//   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lookup&id=" + A.getId());
	//   request.setAttribute("Auto",A);		// Reset the bean with the real object.
	   
	   // forward to view
	   dispatcher.forward(request,response);

	}

}

