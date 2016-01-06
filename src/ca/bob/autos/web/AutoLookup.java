package ca.bob.autos.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

import ca.bob.autos.data.*;
import ca.bob.autos.model.ModelAuto;

/**
 * Sample application, information for a single Auto is returned. 
 */

public class AutoLookup extends  javax.servlet.http.HttpServlet {
	
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
	 int id;   
	 
	 try { 
		 id = Integer.parseInt(request.getParameter("id"));
	 }
	 catch (NumberFormatException nfe) { 
		 throw new ServletException("Auto ID must be provided.",nfe);  // TODO Ugly message. Need nicer way to handle. 
	 }
	 
	 Auto A = new Auto();
	 request.setAttribute("Auto",A);   // put blank one in case we bail out, OR this is new.
	 
	 if (id != -1) {    // Existing record.
	     try {
		  conn = ca.bob.sql.JNDI.getConnection("Autos");
		  if (conn == null) 
			  throw new ServletException("Error connecting to database.");
		  
		  
		  ModelAuto MA = new ModelAuto(conn);  // create Model
		  A = (Auto) MA.get(id);               // Model returns an Object that represents a table row
		  
		  if (A.getId() == -1)
			  throw new ServletException("Auto ID(" + id + ") does not exist.");
		 
	      }
	     catch (SQLException e) { 
		  throw new ServletException(e);
	     }
	     finally {                   // finally *always* get called. Database will always be closed 
		  if (conn != null) { 
		     try {    conn.close(); }
		     catch (SQLException e) { } 
		     conn = null;
		    }
	     }	  
	   request.setAttribute("Auto",A);		// Put the Auto object on the return so view can read it. 
	}  
	  
	   // Forward to view
	   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/autoedit.jsp");
	   dispatcher.forward(request,response);

 }   // End of create new.
}
