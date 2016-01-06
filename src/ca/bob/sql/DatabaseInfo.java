package ca.bob.sql;

import java.sql.*;

/**
 * Title: DatabaseInfo
 * Description:  Show jdbc database information.
 * @author BW
 * @version 1.0
 */

public class DatabaseInfo {
 
public static void show(Connection conn)  {
  try {
	DatabaseMetaData MD = conn.getMetaData();
        System.out.println("---------------- Database Info -----------------");
	System.out.println("JDBC Driver name          : " + MD.getDriverName());
	System.out.println("JDBC Driver version       : " + MD.getDriverVersion());
	System.out.println("JDBC Driver Major version : " + MD.getDriverMajorVersion());
	System.out.println("JDBC Driver Minor version : " + MD.getDriverMinorVersion());
	System.out.println("Database product name     : " + MD.getDatabaseProductName());
	System.out.println("Database product version  : " + MD.getDatabaseProductVersion());
	System.out.println("Max Number of Connections : " + MD.getMaxConnections());
        System.out.println("Max Number of Statements  : " + MD.getMaxStatements());

        System.out.println("AutoCommit Enabled?       : " + conn.getAutoCommit());
	System.out.println("Database catalog name     : " + conn.getCatalog());
	System.out.println("Database Isolation level  : " + conn.getTransactionIsolation());
	System.out.println("Database ReadOnly         : " + conn.isReadOnly());
        System.out.println("------------------------------------------------");
	}
  catch (SQLException e) {
	System.err.println(" SQLException: " + e.getMessage() + ":" + e.getSQLState());
	}
  }
}