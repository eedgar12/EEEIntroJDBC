package lab1;

import java.util.List;

/**
 * An example of a Strategy Pattern Interface that allows creation of 
 * different varieties of Employee DAO implementations. For example, we
 * can create a 'Mock' implementation for testing, where no database
 * connection is needed.
 * 
 * @author jlombardo
 */
public interface IEmployeeDAO {

    // Copy the raw data into a List of Employee entity objects
    List<Employee> getAllEmployees() throws DataAccessException;

    AbstractDBAccess getDb();

    // Copy the raw data into a List of data transfer objects
    List<EmployeeDeptDTO> getEmployeeByDeptId(String id) throws DataAccessException;

    void setDb(AbstractDBAccess db);
    
}
