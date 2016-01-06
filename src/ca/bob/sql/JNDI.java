
/**
 * Get's database connection from a web context. 
 * For example: in Tomcat the JNDI database definition is in $TOMCAT_HOME/conf/context.xml
 * Oct 2009 BW-Modified to lookup jBoss JNDI connections. 
 */
package ca.bob.sql;

import javax.naming.*;
import javax.sql.*;
import java.sql.*;

public class JNDI {
  // JNDI Datasource Name
  private static String dsName     = "java:comp/env/jdbc/";
  private static String ctxName     = "java:comp/env";
  private static String defaultDB = "DEFAULT";
  private static String stat       = "Not Connected";
  private static boolean connected = false;
  private static boolean localdebug = false;
  private static final int LOGIN_TIMEOUT = 30;

  public static Connection getConnection(String dbName) {
    Connection conn = null;
    if (localdebug)
          System.out.println("JNDI:Attempting connection to : " + dbName);    
    try {
      Context init = new InitialContext();
      Context ctx = (Context)init.lookup(ctxName);
      if (ctx == null) {
        throw new Exception("JNDI:getConnection failed: no context");
      }
      
      DataSource ds = null;
      // Try a Tomcat style connection first. NOTE: Inner try catch block 
      try { 
          ds = (DataSource) ctx.lookup("jdbc/" + dbName);   //   Tomcat and Resin lookup JNDI like this.
         }
      catch (javax.naming.NameNotFoundException N) {
          System.err.println(new java.util.Date() + "  JNDI:Couldn't get connection Tomcat style connection.");
    	  ds = null;
         }
      
     if (ds == null)   // Try jBoss style lookup.   Exception will be caught by this outer try 
           ds = (DataSource) init.lookup("java:/" + dbName);    //   jBoss looks up JNDI like this.  That's the good thing about standards, there's so many to choose from. 
     
      if (ds != null) {
    	  
 // Note: setLoginTimeout does not seem to be supported. Resin accepts the command but it does not change the return value of getLoginTimeout(). 
//          Tomcat does not accept the command and throws and exception.  BW Oct 2007    	  
//       System.out.println("1 - DataSource connection timeout is: " + ds.getLoginTimeout());    	  
//    	  ds.setLoginTimeout(LOGIN_TIMEOUT);
//        System.out.println("2 - DataSource connection timeout is: " + ds.getLoginTimeout());
    	  
        conn = ds.getConnection();
        if (conn != null) {
          stat = " Got Connection " + conn.toString() + " to " + dsName + dbName;
          connected = true;
          }
        else 
            System.err.println(new java.util.Date() + "  JNDI:Couldn't get connection");
      }
      else 
         System.err.println(new java.util.Date() + "  JNDI:Datasource is null. Couldn't get connection");
    }
    catch (Exception e) {
      System.err.println(new java.util.Date() + "  " + "JNDI:Exception:" + e.getMessage());
      //e.printStackTrace();
    }
    return conn;
  }

public static Connection getConnection() { 
  return getConnection(defaultDB);
  }

  public boolean isConnected() {
    return connected;
  }

  public String getstat() {
    return stat;
  }
}