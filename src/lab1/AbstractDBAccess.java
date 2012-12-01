package lab1;

import java.util.List;
import java.util.Map;

/**
 * This class is an example of an abstraction that could represent various 
 * Strategy Pattern implementations of vendor-specific (database vendor),
 * low-level JDBC code. By creating different implementations for, say,
 * Oracle, MS-SQL Server and MySql databases, these become interchangeable 
 * variations that make switching database easy without breaking code.
 * 
 * @author Instlogin
 */
public interface AbstractDBAccess {

    public abstract List<Map> getAllRecords(String tableName) 
            throws DataAccessException;
    
    public abstract List<Map> getOneToManyRecords(String oneTable, 
          String manyTable, String pk, String fk, Long targetKey) 
            throws DataAccessException;
    
    public abstract int updateRecordById(String table, String criteriaField, 
            Object criteriaValue, String[] fieldNames, Object[] fieldValues) 
            throws DataAccessException;
    
    public abstract int deleteById(String table, String pkName, Object pkValue) 
            throws DataAccessException;
    
    public abstract int deleteByCriteriaFiield(String table, String fieldName, Object fieldValue) 
            throws DataAccessException;
}

