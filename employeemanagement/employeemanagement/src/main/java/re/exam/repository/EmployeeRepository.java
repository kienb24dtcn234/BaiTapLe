package re.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import re.exam.entity.Department;
import re.exam.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    List<Employee> findByDepartment(Department department);
}
