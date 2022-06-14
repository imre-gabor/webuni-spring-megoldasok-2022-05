package hu.webuni.hr.minta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.minta.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findBySalaryGreaterThan(Integer minSalary);

	Page<Employee> findBySalaryGreaterThan(Integer minSalary, Pageable pageable);

	List<Employee> findByPositionName(String title);

	List<Employee> findByNameStartingWithIgnoreCase(String name);

	
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);

	
	@Modifying
	@Transactional
	//1. megoldás: nem működik (hibernate cross join)
//	@Query("UPDATE Employee e "
//			+ "SET e.salary = :minSalary "
//			+ "WHERE e.position.name=:position "
//			+ "AND e.salary < :minSalary "
//			+ "AND e.company.id=:companyId")
	
	@Query("UPDATE Employee e "
			+ "SET e.salary = :minSalary "
			+ "WHERE e.employeeId IN "
			+ "(SELECT e2.employeeId "
			+ "FROM Employee e2 "
			+ "WHERE e2.position.name=:position "
			+ "AND e2.salary < :minSalary "
			+ "AND e2.company.id=:companyId"
			+ ")")
	int updateSalaries(String position, int minSalary, long companyId);
}
