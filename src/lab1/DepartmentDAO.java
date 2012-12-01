package lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An example of a Data Access Object (DAO) for Department information. Notice 
 * how the method names use problem domain terminology as opposed to more 
 * technical, db-realted terminology. For example, we say "getAllDepartments" 
 * vs. "getAllRecords".
 * 
 * @author Instlogin
 */
public class DepartmentDAO implements IDepartmentDAO {
    private AbstractDBAccess db;

    public DepartmentDAO() {
    }

    public DepartmentDAO(AbstractDBAccess db) {
        this.db = db;
    }

    // Copy the raw data into a List of Department entity objects
    @Override
    public List<Department> getAllDepartments() throws DataAccessException {
        List<Map> rawData = new ArrayList<Map>();
        List<Department> records = new ArrayList<Department>();
        rawData = db.getAllRecords("Department");
        Department dept = null;
        
        // Translate List<Map> into List<Employee>
        for(Map m : rawData) {
            dept = new Department();
            String dept_id = m.get("dept_id").toString();
            dept.setDeptId(Long.parseLong(dept_id));
            String deptName = m.get("dept_name").toString();
            dept.setDeptName(deptName);
            
            records.add(dept);
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
    
    
}
