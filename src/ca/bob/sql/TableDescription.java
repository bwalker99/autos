package ca.bob.sql;
import java.sql.*;

/**
 * Title:        Table Description.
 * Description:  Describe a table.
 * Copyright:    Copyright (c) 2002
 * @author BW
 * @version 1.0
 */
public class TableDescription {
 private java.sql.Connection conn = null;
 private String dsn;
 private String tablename;

  public TableDescription(String adsn) {
  this.dsn = adsn;
  }

  public TableDescription(Connection C) {
	    conn = C;
	  }
   
   public static void main (String args [])  {
   String tablename;
   if (args.length < 2) {
    System.out.println("Usage: TableDescription <DSN Name> <Table Name> {DatabaseInfo}\n");
    System.exit(0);
    }
  System.out.println("Table Descriptor. DSN: " + args[0] + " Table: " + args[1]);
  TableDescription TD = new TableDescription(args[0]);
  if (!TD.connect()) {
    System.out.println("Error connecting to database.");
    System.exit(1);
    }
  tablename = args[1];
  TD.show(tablename,args.length);
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

 
  public void show(String tablename,int nargs) {
  ResultSetMetaData rsmd;
  //ca.langara.util.Formats fm = new  ca.langara.util.Formats();

  if (nargs > 2) 
    DatabaseInfo.show(conn);

  try {
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("SELECT * from " + tablename + " where 0 = 1");
    ResultSetMetaData metadata = rs.getMetaData();

    int numColumns = metadata.getColumnCount();	// how many columns
    for(int i = 1; i <= numColumns; i++) {
      System.out.print(space(metadata.getColumnName(i),30,false));

	switch (metadata.getColumnType(i)) {
          case Types.CHAR:
		System.out.println("CHAR (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	  case Types.INTEGER:
       		System.out.println("INTEGER (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	 case Types.SMALLINT:
		System.out.println("SMALLINT (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	case Types.VARCHAR:
		System.out.println("VARCHAR (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	case Types.TINYINT:
		System.out.println("TINYINT (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	case Types.FLOAT:
		System.out.println("FLOAT (" + metadata.getColumnDisplaySize(i) + ")");
        	break;
	case Types.DATE:
		System.out.println("DATE (" + metadata.getColumnDisplaySize(i) + ")");
	        break;
	case Types.TIMESTAMP:	// 93 - This is also DATETIME on SQL Server.
		System.out.println("TIMESTAMP (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	case Types.DECIMAL:
		System.out.println("DECIMAL (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	case Types.NUMERIC:
		System.out.println("NUMERIC (" + metadata.getColumnDisplaySize(i) + ")");
		break;
	default:
		System.out.println("*UNKNOWN* Types value: " + metadata.getColumnType(i) + " (" + metadata.getColumnDisplaySize(i) + ")");
                break;
	}
    }
  }
  catch (SQLException e) {
    System.out.println("Error reading database: " + e.getMessage());
    }

  }

  /**
   * Space out a string to a specifiec length
   * @param spacee - the original string
   * @param l  - the new length required - either appended or prepended with spaces.
   * @param front - true if prepend spaces, false if append.
   */
  private String space(String spacee, int l, boolean front) {
    String temp_spacee = spacee;
    if (temp_spacee.length() > l) return temp_spacee.substring(0, l-1);
    StringBuffer SB = new StringBuffer(" ");
    SB.setLength(l - temp_spacee.length());
    String spaces = SB.toString().replace('\0', ' ');
    if (front) return spaces + temp_spacee;
    return temp_spacee + spaces;
   }
}