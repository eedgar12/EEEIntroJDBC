package lab1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An example of a Mock Data Access Object (DAO) for Employee information. The
 * purpose of this class is to provide an implementation for testing
 * purposes that doesn't access the database. Rather we fake this access,
 * which makes testing easier and faster because the db connection is not
 * used or needed.
 * 
 * @author Instlogin
 */
public class MockEmployeeDAO implements IEmployeeDAO {
    private AbstractDBAccess db;

    public MockEmployeeDAO() {
    }
    
    public MockEmployeeDAO(AbstractDBAccess db) {
        this.db = db;
    }
    
    // Copy the raw data into a List of data transfer objects
    @Override
    public List<EmployeeDeptDTO> getEmployeeByDeptId(String id) throws DataAccessException {
        List<EmployeeDeptDTO> records = new ArrayList<EmployeeDeptDTO>();
        
        if(id.equals("1")) {
            EmployeeDeptDTO mockDto = 
                    new EmployeeDeptDTO("Jones", "Bob", "Marketing");
            records.add(mockDto);
        }
        
        return records;
    }
    
    // Copy the raw data into a List of Employee entity objects
    @Override
    public List<Employee> getAllEmployees() throws DataAccessException {
        List<Employee> records = new ArrayList<Employee>();
        Employee emp = new Employee(1L, "Smith", "Dave", "dsmith@isp.com", new Date(), 1);
        records.add(emp);
        
        return records;
    }

    @Override
    public AbstractDBAccess getDb() {
        return db;
    }

    @Override
    public void setDb(AbstractDBAccess db) {
        this.db = db;
    }
    
    // Test harness only -- remove in production version
    public static void main(String[] args) throws Exception {
        MockEmployeeDAO dao = new MockEmployeeDAO();
        
        dao.db = new MsSqlServerDBAccess("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://bitsql.wctc.edu:1433;databaseName=JGL-EMPLOYEE",
                "advjava", "advjava");

        List<EmployeeDeptDTO> records = 
                dao.getEmployeeByDeptId("1");

        for(EmployeeDeptDTO record : records) {
            System.out.print(record.getDeptName() + ", ");
            System.out.print(record.getFirstName() + " ");
            System.out.println(record.getLastName());
        }

    }
    
}
