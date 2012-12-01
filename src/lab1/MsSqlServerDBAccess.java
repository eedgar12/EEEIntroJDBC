package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class acts as a Strategy implementation of AbstractDBAccess, and 
 * represents the MS-SQLServer 2008 low-level JDBC code. 
 * 
 * @author Instlogin
 */
public class MsSqlServerDBAccess implements AbstractDBAccess {
    private static final boolean DEBUG = true;
    private Connection conn;
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private Statement stmt;
    private PreparedStatement psmt;
    private ResultSet rs;
    private String sql;

    public MsSqlServerDBAccess(String driverClassName,
            String url, String userName, String password) throws Exception {

        this.driverClassName = driverClassName;
        this.url = url;
        this.userName = userName;
        this.password = password;
        Class.forName (driverClassName);
        
    }
    
    /**
     * Deletes records by any existing field and matching value for any table.
     * @param table - the name of the table
     * @param fieldName - the name of the criteria field (where clause)
     * @param fieldValue - the value of the criteria field (where clause)
     * @return the number of records affected.
     * @throws DataAccessException if anything goes wrong.
     */
    public int deleteByCriteriaFiield(String table, String fieldName, Object fieldValue) throws DataAccessException {
        int recordsDeleted = 0;
        
        String sql = "DELETE FROM " + table + " WHERE " + fieldName + "=?";
        
        try {
            conn = DriverManager.getConnection(url, userName, password);
            psmt = conn.prepareStatement(sql);
            
            
            // now set the criteria value
            psmt.setObject(1, fieldValue);
            // now execute the update
            recordsDeleted = psmt.executeUpdate();
            
        } catch (SQLException sqle) {
            throw new DataAccessException("Database query failed due to "
                    + sqle.getMessage());
        } catch (Exception e) {
            throw new DataAccessException("Database query failed due to "
                    + e.getMessage());
        } finally {
            try {
                if(psmt !=null) psmt.close();
                if(conn != null) conn.close();
            } catch(SQLException e) {
                throw new DataAccessException("Database connection cannot be closed due to "
                + e.getMessage());
            } // end try
        } // end finally
        
        return recordsDeleted;
    }
    
    public int deleteById(String table, String pkName, Object pkValue) throws DataAccessException {
        int recordsDeleted = 0;
        
        String sql = "DELETE FROM " + table + " WHERE " + pkName + "=?";
        
        try {
            conn = DriverManager.getConnection(url, userName, password);
            psmt = conn.prepareStatement(sql);
            
            
            // now set the criteria value
            psmt.setObject(1, pkValue);
            // now execute the update
            recordsDeleted = psmt.executeUpdate();
            
        } catch (SQLException sqle) {
            throw new DataAccessException("Database query failed due to "
                    + sqle.getMessage());
        } catch (Exception e) {
            throw new DataAccessException("Database query failed due to "
                    + e.getMessage());
        } finally {
            try {
                if(psmt !=null) psmt.close();
                if(conn != null) conn.close();
            } catch(SQLException e) {
                throw new DataAccessException("Database connection cannot be closed due to "
                + e.getMessage());
            } // end try
        } // end finally
        
        return recordsDeleted;
    }

    @Override
    public List<Map> getAllRecords(String tableName) throws DataAccessException {
        if(tableName == null || tableName.length() == 0) {
            throw new DataAccessException("Database query failed. Missing query parameters.");
        }
        
        // NOTE the vendor-specific syntax!!!
        sql = "SELECT TOP 1000 * FROM " + tableName;

        return performQuery(); 
    }
    
    
    @Override
    public List<Map> getOneToManyRecords(String oneTable, String manyTable,
        String pk, String fk, Long criteriaKey) throws DataAccessException {
        
        // validate parameters
        if(oneTable == null || oneTable.length() == 0
            || manyTable == null || manyTable.length() == 0
            || pk == null || fk == null || criteriaKey == null) {
            throw new DataAccessException("Database query failed. Missing query parameters.");
        }
        
        sql = "SELECT t1.*,t2.* FROM "
                + oneTable + " As t1," + manyTable + " As t2 WHERE t2."
                + fk + "=t1." + pk + " AND t1." + pk + "=" + criteriaKey;
        
        if(DEBUG) System.out.println(sql);
        
        return performQuery();
    }

    private List<Map> performQuery() throws DataAccessException {
        ResultSetMetaData metaData = null;
        final List<Map> list=new ArrayList<Map>();
        Map record = null;

        // do this in an excpetion handler so that we can depend on the
        // finally clause to close the connection
        try {
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            metaData = rs.getMetaData();
            final int fields = metaData.getColumnCount();

            while( rs.next() ) {
                    record = new HashMap();
                    for( int i=1; i <= fields; i++ ) {
                            try {
                                record.put( metaData.getColumnName(i), rs.getObject(i) );
                            } catch(NullPointerException npe) {
                                // no need to do anything... if it fails, just ignore it and continue
                            }
                    } // end for
                    list.add(record);
            } // end while

        } catch (SQLException sqle) {
            throw new DataAccessException("Database query failed due to "
                    + sqle.getMessage());
        } catch (Exception e) {
            throw new DataAccessException("Database query failed due to "
                    + e.getMessage());
        } finally {
            try {
                if(stmt !=null) stmt.close();
                if(conn != null) conn.close();
                System.out.println("connection closed ok");
            } catch(SQLException e) {
                throw new DataAccessException("Database connection cannot be closed due to "
                + e.getMessage());
            } // end try
        } // end finally

        return list; // will  be null if none found
    }    
    
    public int updateRecordById(String table, String criteriaField, 
            Object criteriaValue, String[] fieldNames, Object[] fieldValues) 
            throws DataAccessException {
        
        int recsUpdated = 0;
        
        /*
         * SQL Syntax for Update:
         *     UPDATE table SET column1=value1, column2=value2
         *     WHERE criteriaField = criteriaValue
         * NOTE: surround all values with single quotes if String data
         */
        
        // construct where clause:
        String sqlWhere = " WHERE " + criteriaField + "=?";
        // construct sql update clause:
        String sqlUpdate = "UPDATE " + table + " SET ";
        // for every field there should be one value
        for(int i = 0; i < fieldNames.length; i++) {
            if(i < fieldNames.length-1) {
                sqlUpdate += fieldNames[i] + "=?,";
            } else {
                 sqlUpdate += fieldNames[i] + "=?";
            }
        }
        
        String sql = sqlUpdate + sqlWhere;
        System.out.println(sql);
        
        
        try {
            conn = DriverManager.getConnection(url, userName, password);
            psmt = conn.prepareStatement(sql);
            
            //loop over values an set them in a type-independent way
            for(int i=0; i < fieldValues.length; i++) {
                psmt.setObject(i+1, fieldValues[i]);
            }
            // now set the criteria value
            psmt.setObject(fieldValues.length+1, criteriaValue);
            // now execute the update
            recsUpdated = psmt.executeUpdate();
            
        } catch (SQLException sqle) {
            throw new DataAccessException("Database query failed due to "
                    + sqle.getMessage());
        } catch (Exception e) {
            throw new DataAccessException("Database query failed due to "
                    + e.getMessage());
        } finally {
            try {
                if(psmt !=null) psmt.close();
                if(conn != null) conn.close();
            } catch(SQLException e) {
                throw new DataAccessException("Database connection cannot be closed due to "
                + e.getMessage());
            } // end try
        } // end finally
        
        return recsUpdated;
    }

    // Test harness only -- remove in production version
    public static void main(String[] args) throws Exception {
        AbstractDBAccess db = new MsSqlServerDBAccess("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://bitsql.wctc.edu:1433;databaseName=JGL-EMPLOYEE",
                "advjava", "advjava");
        
//        int recs = db.updateRecordById("EMPLOYEE", "ID", new Integer(943), 
//                new String[] {"LASTNAME","FIRSTNAME","version","HIREDATE"}, 
//                new Object[] {"Jones", "Bobby",new Integer(3),"2004-02-02 12:11:10"});
        
        int recs = db.updateRecordById("EMPLOYEE", "LASTNAME", "Schwartz", 
                new String[] {"version","HIREDATE"}, 
                new Object[] {new Integer(2),"2/2/2000 11:11"});
        
//        int recs = db.deleteByCriteriaFiield("EMPLOYEE", "LASTNAME", "Schwartz");
        if(recs == 0) {
            System.out.println("update failed");
        } else {
            System.out.println("update succeeded ... " + recs + " record updated");
        }
        
//        List<Map> records = 
//                db.getOneToManyRecords("Department", "EMPLOYEE", "dept_id", "dept_id", new Long(3));
//
//        for(Map record : records) {
//            System.out.print(record.get("dept_name") + ", ");
//            System.out.print(record.get("FIRSTNAME") + " ");
//            System.out.println(record.get("LASTNAME"));
//        }

        
    }
}
