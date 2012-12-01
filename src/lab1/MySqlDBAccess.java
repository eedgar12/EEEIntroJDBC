package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class acts as a Strategy implementation of AbstractDBAccess, and 
 * represents the MySql low-level JDBC code. 
 * 
 * @author Instlogin
 */
public class MySqlDBAccess implements AbstractDBAccess {
    private Connection conn;
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private Statement stmt;
    private ResultSet rs;
    private String sql;

    public MySqlDBAccess(String driverClassName,
            String url, String userName, String password) throws Exception {

        this.driverClassName = driverClassName;
        this.url = url;
        this.userName = userName;
        this.password = password;
        Class.forName (driverClassName);
        
    }

    @Override
    public List<Map> getAllRecords(String tableName) throws DataAccessException {
        if(tableName == null || tableName.length() == 0) {
            throw new IllegalArgumentException("Tablename is required");
        }
        
        sql = "SELECT * FROM " + tableName + " LIMIT 1,1000";
        
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
            } catch(SQLException e) {
                throw new DataAccessException("Database connection cannot be closed due to "
                + e.getMessage());
            } // end try
        } // end finally

        return list; // will  be null if none found
    }

 
    public List<Map> getOneToManyRecords(String oneTable, String manyTable, String pk, String fk, Long targetKey) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int updateRecordById(String table, String criteriaField, Object criteriaValue, String[] fieldNames, Object[] fieldValues) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int deleteById(String table, String pkName, Object pkValue) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int deleteByCriteriaFiield(String table, String fieldName, Object fieldValue) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
