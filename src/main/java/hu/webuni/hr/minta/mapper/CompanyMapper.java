package hu.webuni.hr.minta.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.minta.dto.CompanyDto;
import hu.webuni.hr.minta.dto.EmployeeDto;
import hu.webuni.hr.minta.model.Company;
import hu.webuni.hr.minta.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	CompanyDto companyToDto(Company company);
	List<CompanyDto> companiesToDtos(List<Company> company);

	@Named("summary")
	@Mapping(target = "employees", ignore = true)
	CompanyDto companyToDtoWithNoEmployees(Company company);

	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToDtosWithNoEmployees(List<Company> company);

	
	Company dtoToCompany(CompanyDto companyDto);
	
	
	
	@Mapping(target = "id", source = "employeeId")
	@Mapping(target = "title", source = "jobTitle")
	@Mapping(target = "entryDate", source = "dateOfStartWork")
	EmployeeDto employeeToDto(Employee employee);

	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employees);
}
