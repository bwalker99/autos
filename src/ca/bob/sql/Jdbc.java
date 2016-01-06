package ca.bob.sql;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * <p>Title: Jdbc</p>
 * <p>Description: Get a one-time connection object. Database definitions are stored in properties file.
 *    One for each database.</p>
 * @author Bob Walker
 * @version 1.0
 */

public final class Jdbc {

  /*
   * Get the database connection.
   * @param database The name of the DSN or database definition properties file.
   * It is assumed there is a file called <propDirectory><database>.props in properties format
   * with the database definitions
   * @return A valid database connection or NULL
   */
public static java.sql.Connection getConnection(String database) {
    java.sql.Connection conn = null;

    try {
      ResourceBundle dbBundle = ResourceBundle.getBundle(database);
      java.util.Properties p = new java.util.Properties();
      Enumeration bundleKeys = dbBundle.getKeys();

      while (bundleKeys.hasMoreElements()) {
        String key = (String)bundleKeys.nextElement();
        String value  = dbBundle.getString(key);
        p.setProperty(key, value);
       }

      String driver = dbBundle.getString("driver");
      String url = dbBundle.getString("url");
      Class classReference = Class.forName(driver);
      java.sql.Driver D = (java.sql.Driver) classReference.newInstance();
      conn = D.connect(url,p);
      conn.setAutoCommit(false);
      conn.setReadOnly(false);
      }
    catch (Exception e) {
      System.err.println("Jdbc:getConnection() - Error creating connection: " + e.getMessage() + 
    		  "\nTrying with properties file.");
      conn = getConnectionAsFile(database);
      // e.printStackTrace();
    }
  return conn;
  }


/**
 * Open properties as a file, not as a Resource Bundle. 
 * This seems to be necessary if this class is in jar file and the properties file is not. 
 * @param database
 * @return
 */
public static java.sql.Connection getConnectionAsFile(String database) {
    java.sql.Connection conn = null;

    try {
      java.util.Properties p = new java.util.Properties();
      p.load(new FileInputStream(database));

      String driver = p.getProperty("driver"); 
      String url = p.getProperty("url");
      Class classReference = Class.forName(driver);
      java.sql.Driver D = (java.sql.Driver) classReference.newInstance();
      conn = D.connect(url,p);
      conn.setAutoCommit(false);
      conn.setReadOnly(false);
      }
    catch (Exception e) {
      System.err.println("Jdbc:getConnectionAsFile() - Error creating connection from properties file: " + e.getMessage());
       e.printStackTrace();
    }
  return conn;
  }


/**
 * Get Connection for default database definition
 * @return
 */

  public static java.sql.Connection getConnection() {
    return Jdbc.getConnection("default");
  }

  
}

