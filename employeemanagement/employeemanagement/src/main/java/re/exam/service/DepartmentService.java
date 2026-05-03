package re.exam.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import re.exam.entity.Department;
import re.exam.entity.Employee;
import re.exam.repository.DepartmentRepository;
import re.exam.repository.EmployeeRepository;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public int deleteDepartmentAndDetachEmployees(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        List<Employee> employees = employeeRepository.findByDepartment(department);

        for (Employee e : employees) {
            e.setDepartment(null);
        }
        employeeRepository.saveAll(employees);

        departmentRepository.delete(department);

        return employees.size();
    }
}
