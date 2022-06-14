package hu.webuni.hr.minta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.minta.model.Employee;
import hu.webuni.hr.minta.model.PositionDetailsByCompany;
import hu.webuni.hr.minta.repository.EmployeeRepository;
import hu.webuni.hr.minta.repository.PositionDetailsByCompanyRepository;
import hu.webuni.hr.minta.repository.PositionRepository;

@Service
public class SalaryService {

	private EmployeeService employeeService;
	private PositionRepository positionRepository;
	private PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	private EmployeeRepository employeeRepository;
	
	public SalaryService(EmployeeService employeeService, PositionRepository positionRepository,
			PositionDetailsByCompanyRepository positionDetailsByCompanyRepository,
			EmployeeRepository employeeRepository) {
		this.employeeService = employeeService;
		this.positionRepository = positionRepository;
		this.positionDetailsByCompanyRepository = positionDetailsByCompanyRepository;
		this.employeeRepository = employeeRepository;
	}

	public void setNewSalary(Employee employee) {
		int newSalary = employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) / 100;
		employee.setSalary(newSalary);
	}
	
	
	@Transactional
	public void raiseMinSalary(long companyId, String positionName, int minSalary) {
		PositionDetailsByCompany pd = positionDetailsByCompanyRepository.findByPositionNameAndCompanyId(positionName, companyId)
			.get();
		
		pd.setMinSalary(minSalary);
		
		//1. megoldás, nem hatékony, mert annyi SQL UPDATE utasítás lesz, ahány employee-nek átállítottam a fizetését
//		pd.getCompany().getEmployees().forEach(e ->{
//			if(e.getPosition().getName().equals(positionName) && e.getSalary() < minSalary)
//				e.setSalary(minSalary);
//		});
		
		//2. megoldás
		employeeRepository.updateSalaries(positionName, minSalary, companyId);
	}

}
