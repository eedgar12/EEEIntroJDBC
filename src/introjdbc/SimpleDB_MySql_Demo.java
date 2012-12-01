package introjdbc;

import java.sql.*;

public class SimpleDB_MySql_Demo {
	private Connection conn;
	private String driverClassName;
	private String url;
	private String userName;
	private String password;

	public static void main(String[] args) {

		SimpleDB_MySql_Demo db = new SimpleDB_MySql_Demo();
		db.driverClassName = "com.mysql.jdbc.Driver";
		db.url = "jdbc:mysql://localhost:3306/person";
		db.userName = "root";
		db.password = "admin";

		try {
			  Class.forName (db.driverClassName);
			  db.conn = DriverManager.getConnection(db.url, db.userName, db.password);
		}
		catch ( ClassNotFoundException cnfex ) {
		   System.err.println(
			  "Error: Failed to load JDBC driver!" );
		   cnfex.printStackTrace();
		   System.exit( 1 );  // terminate program
		}
		catch ( SQLException sqlex ) {
		   System.err.println( "Error: Unable to connect to database!" );
		   sqlex.printStackTrace();
		   System.exit( 1 );  // terminate program
		}
                
		Statement stmt = null;
		ResultSet rs = null;

                String sql = "select * from contact";

		try {
			stmt = db.conn.createStatement();
			rs = stmt.executeQuery(sql);
                        
                        System.out.println("============================");
                        System.out.println("Output from MySQL Server...");
                        System.out.println("============================");
                        
			int count = 0;
			while( rs.next() ) {
                      System.out.println("\nRecord No: " + (count + 1));
				System.out.println( "ID: " + rs.getInt("contact_id") ); // named field
				System.out.println( "Last Name: " + rs.getString("last_name") ); // named field
				System.out.println( "First Name: " + rs.getString("first_name") );
				System.out.println( "Hire Date: " + rs.getDate("birthday") );
				count++;
			}
			System.out.println( "==================\n" + count + " records found." );
		} catch (SQLException sqle) {
			System.out.println(sqle);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// Make sure we close the statement and connection objects no matter what.
			// Since these also throw checked exceptions, we need a nested try-catch
			try {
				stmt.close();
				db.conn.close();
			} catch(Exception e) {
				System.out.println(e);
			}
		}

	}
}
