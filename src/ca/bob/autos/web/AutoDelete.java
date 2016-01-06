package ca.bob.autos.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.bob.autos.model.ModelAuto;

import java.io.IOException;
import java.sql.*;


/**
 * Delete servlet for Auto.  <br/> 
 */
public class AutoDelete extends  javax.servlet.http.HttpServlet {
	
	  public void init() throws ServletException {
	  }

	  public void doGet(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException	  {
	    doPost(request,response); // No differentiation between post and get. Not for production systems.
	  }
	  
	  public void doPost(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException	  {

	String temp = request.getParameter("id");
	int id= -1;
	try {  id = Integer.parseInt(temp); }
	catch (NumberFormatException nfe) { 
		throw new ServletException("ID  must be supplied.",nfe);  // TODO Message should come from elsewhere. 
		}
			 
	 java.sql.Connection conn = null;
	  try {
		  conn = ca.bob.sql.JNDI.getConnection("Autos");
		  conn.setAutoCommit(false);   // for mysql 
		  if (conn == null) 
			  throw new ServletException("Error connecting to database.");  
		  
		  ModelAuto MA = new ModelAuto(conn); 
		  MA.delete(id);
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
	  
	 	  // After delete, list all autos.
	   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/list");
	   dispatcher.forward(request,response);

	}

}

