package re.exam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re.exam.entity.Employee;
import re.exam.repository.EmployeeRepository;
import re.exam.spec.EmployeeSpecifications;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> search(String name,
                                 Long departmentId,
                                 Integer minAge,
                                 Integer maxAge,
                                 Pageable pageable) {
        return employeeRepository.findAll(
                EmployeeSpecifications.filter(name, departmentId, minAge, maxAge),
                pageable
        );
    }
}
