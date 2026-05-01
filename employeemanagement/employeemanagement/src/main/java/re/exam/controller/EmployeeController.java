package re.exam.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re.exam.entity.Department;
import re.exam.entity.Employee;
import re.exam.repository.DepartmentRepository;
import re.exam.repository.EmployeeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public EmployeeController(EmployeeRepository employeeRepository,
                              DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employees";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);

        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);

        return "employee-form";
    }

    @PostMapping
    public String createEmployee(@ModelAttribute("employee") Employee employee,
                                 @RequestParam("avatarFile") MultipartFile avatarFile) throws IOException {

        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department department = departmentRepository
                    .findById(employee.getDepartment().getId())
                    .orElse(null);
            employee.setDepartment(department);
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String originalFilename = avatarFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID().toString() + fileExtension;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFileName);
            avatarFile.transferTo(filePath.toFile());

            employee.setAvatar(newFileName);
        }

        employeeRepository.save(employee);

        return "redirect:/employees";
    }
}
