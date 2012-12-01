package lab1;

import java.util.List;

/**
 * An example of a Strategy Pattern Interface that allows creation of 
 * different varieties of Department DAO implementations. For example, we
 * can create a 'Mock' implementation for testing, where no database
 * connection is needed.
 * 
 * @author jlombardo
 */
public interface IDepartmentDAO {

    // Copy the raw data into a List of Department entity objects
    List<Department> getAllDepartments() throws DataAccessException;

    AbstractDBAccess getDb();

    void setDb(AbstractDBAccess db);
    
}
