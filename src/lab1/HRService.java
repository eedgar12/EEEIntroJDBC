package lab1;

import java.util.List;

/**
 * This is a service class (an implementation of the Facade Design Pattern) 
 * for various Human Resource related tasks. It is a high-level class and 
 * the main connection point to a data store from a program. Notice how HR
 * tasks span both employee and department data, and notice how the method
 * names use problem domain terminology.
 * 
 * @author Instlogin
 */
public class HRService {
    private EmployeeDAO empDao;
    private DepartmentDAO deptDao;
    
    public List<Employee> getAllEmployees() throws DataAccessException {
        return empDao.getAllEmployees();
    }
    
    public List<Department> getAllDepartments() throws DataAccessException {
        return deptDao.getAllDepartments();
    }
    
    public List<EmployeeDeptDTO> getEmployeesByDeptId(String deptId) throws DataAccessException {
        return empDao.getEmployeeByDeptId(deptId);
    }
    // Test harness only -- remove in production version
    public static void main(String[] args) throws Exception {
        HRService srv = new HRService();
        
        AbstractDBAccess db = new MsSqlServerDBAccess("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://bitsql.wctc.edu:1433;databaseName=JGL-EMPLOYEE",
                "advjava", "advjava");

        srv.empDao = new EmployeeDAO(db);

        List<EmployeeDeptDTO> records = 
                srv.getEmployeesByDeptId("3");

        for(EmployeeDeptDTO record : records) {
            System.out.print(record.getDeptName() + ", ");
            System.out.print(record.getFirstName() + " ");
            System.out.println(record.getLastName());
        }

    }
    
}
