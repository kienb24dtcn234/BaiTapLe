package re.exam.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import re.exam.entity.Employee;

public class EmployeeSpecifications {

    public static Specification<Employee> filter(String name, Long departmentId, Integer minAge, Integer maxAge) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (departmentId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("department").get("id"), departmentId));
            }

            if (minAge != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("age"), minAge));
            }

            if (maxAge != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            return predicate;
        };
    }
}
