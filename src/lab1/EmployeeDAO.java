package lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An example of a Data Access Object (DAO) for Employee information. Notice 
 * how the method names use problem domain terminology as opposed to more 
 * technical, db-realted terminology. For example, we say "getEmployeeByDeptId" 
 * vs. "getAllRecordsByForeginKey".
 * 
 * @author Instlogin
 */
public class EmployeeDAO implements IEmployeeDAO {
    private AbstractDBAccess db;

    public EmployeeDAO() {
    }
    
    public EmployeeDAO(AbstractDBAccess db) {
        this.db = db;
    }
    
    // Copy the raw data into a List of data transfer objects
    @Override
    public List<EmployeeDeptDTO> getEmployeeByDeptId(String id) throws DataAccessException {
        List<Map> rawData = new ArrayList<Map>();
        List<EmployeeDeptDTO> records = new ArrayList<EmployeeDeptDTO>();
        rawData = db.getOneToManyRecords("Department", "EMPLOYEE", 
                "dept_id", "dept_id", new Long(id));
        EmployeeDeptDTO dto = null;
        
        // Translate List<Map> into List<Employee>
        for(Map m : rawData) {
            dto = new EmployeeDeptDTO();
            String deptName = m.get("dept_name").toString();
            dto.setDeptName(deptName);
            String firstName = m.get("FIRSTNAME").toString();
            dto.setFirstName(firstName);
            String lastName = m.get("LASTNAME").toString();
            dto.setLastName(lastName);
            
            records.add(dto);
        }
        
        return records;
    }
    
    // Copy the raw data into a List of Employee entity objects
    @Override
    public List<Employee> getAllEmployees() throws DataAccessException {
        List<Map> rawData = new ArrayList<Map>();
        List<Employee> records = new ArrayList<Employee>();
        rawData = db.getAllRecords("EMPLOYEE");
        Employee employee = null;
        
        // Translate List<Map> into List<Employee>
        for(Map m : rawData) {
            employee = new Employee();
            String firstName = m.get("FIRSTNAME").toString();
            employee.setFirstName(firstName);
            String lastName = m.get("LASTNAME").toString();
            
            records.add(employee);
        }
        
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
        EmployeeDAO dao = new EmployeeDAO();
        
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
