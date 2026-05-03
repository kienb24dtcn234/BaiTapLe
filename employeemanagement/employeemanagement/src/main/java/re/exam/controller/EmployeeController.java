package re.exam.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re.exam.entity.Department;
import re.exam.entity.Employee;
import re.exam.repository.DepartmentRepository;
import re.exam.service.EmployeeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public EmployeeController(EmployeeService employeeService,
                              DepartmentRepository departmentRepository) {
        this.employeeService = employeeService;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public String listEmployees(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortField", defaultValue = "name") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "departmentId", required = false) Long departmentId,
            @RequestParam(name = "minAge", required = false) Integer minAge,
            @RequestParam(name = "maxAge", required = false) Integer maxAge
    ) {

        if (page < 0) {
            page = 0;
        }

        Sort sort = Sort.by(sortField);
        sort = "desc".equalsIgnoreCase(sortDir) ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String nameFilter = keyword != null ? keyword : "";

        Page<Employee> employeePage = employeeService.search(
                nameFilter, departmentId, minAge, maxAge, pageable
        );

        int totalPages = employeePage.getTotalPages();
        if (page >= totalPages && totalPages > 0) {
            page = totalPages - 1;
            pageable = PageRequest.of(page, size, sort);
            employeePage = employeeService.search(nameFilter, departmentId, minAge, maxAge, pageable);
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");

        model.addAttribute("keyword", nameFilter);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        model.addAttribute("size", size);

        model.addAttribute("departments", departmentRepository.findAll());

        return "employees";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
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

        employeeService.search("", null, null, null, PageRequest.of(0, 1));
        return "redirect:/employees";
    }
}
