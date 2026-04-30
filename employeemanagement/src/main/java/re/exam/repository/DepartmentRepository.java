package re.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.exam.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
