package re.exam.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import re.exam.entity.Department;
import re.exam.entity.Employee;
import re.exam.repository.DepartmentRepository;
import re.exam.repository.EmployeeRepository;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DataSeeder(DepartmentRepository departmentRepository,
                      EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0 || employeeRepository.count() > 0) {
            return;
        }

        Department it = Department.builder()
                .name("IT")
                .location("Ha Noi")
                .build();

        Department hr = Department.builder()
                .name("HR")
                .location("Ho Chi Minh")
                .build();

        departmentRepository.saveAll(List.of(it, hr));

        Employee e1 = Employee.builder()
                .name("Nguyen Van A")
                .age(25)
                .avatar(null)
                .status("ACTIVE")
                .department(it)
                .build();

        Employee e2 = Employee.builder()
                .name("Tran Thi B")
                .age(28)
                .avatar(null)
                .status("INACTIVE")
                .department(hr)
                .build();

        Employee e3 = Employee.builder()
                .name("Le Van C")
                .age(30)
                .avatar(null)
                .status("ACTIVE")
                .department(it)
                .build();

        employeeRepository.saveAll(List.of(e1, e2, e3));
    }
}
