package re.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.exam.service.DepartmentService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable("id") Long id, Model model) {
        int affectedEmployees = departmentService.deleteDepartmentAndDetachEmployees(id);
        model.addAttribute("message", "Đã xóa phòng ban và cập nhật trạng thái cho " + affectedEmployees + " nhân viên");
        return "redirect:/employees";
    }
}
