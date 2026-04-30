package re.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.exam.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

