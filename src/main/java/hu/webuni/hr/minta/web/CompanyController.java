package hu.webuni.hr.minta.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.minta.dto.CompanyDto;
import hu.webuni.hr.minta.dto.EmployeeDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	private Map<Long, CompanyDto> companies = new HashMap<>();
	
	
	//1. megoldás
	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full){
		return null;

//		if(isFull(full))
//			return new ArrayList<>(companies.values());
//		else
//			return companies.values()
//				.stream()
//				.map(this::mapCompanyWithoutEmployees)
//				.collect(Collectors.toList());
	}
	
	//2. megoldás
//	@GetMapping(params = "full=true")
//	public List<CompanyDto> getAllWithEmployees(){
//		return new ArrayList<>(companies.values());
//	}
	
//	@GetMapping
//	@JsonView(Views.BaseData.class)
//	public List<CompanyDto> getAllWithoutEmployees(@RequestParam(required = false) Boolean full){
//		return new ArrayList<>(companies.values());
//	}

	private boolean isFull(Boolean full) {
		return full != null && full;
	}

	private CompanyDto mapCompanyWithoutEmployees(CompanyDto c) {
		return new CompanyDto(c.getId(),c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
	}
	
	@GetMapping("/{id}")
	public CompanyDto getById(@PathVariable long id, @RequestParam(required = false) Boolean full) {
		return null;
//		CompanyDto companyDto = companies.get(id);
//		if(isFull(full))
//			return companyDto;
//		else
//			return mapCompanyWithoutEmployees(companyDto);
	}
	
	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
		return null;
//      return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		return null;
//      companyDto.setId(id);
//      Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
//      if (updatedCompany == null) {
//          return ResponseEntity.notFound().build();
//      }
//
//      return ResponseEntity.ok(companyMapper.companyToDto(updatedCompany));
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
//      companyService.delete(id);
	}
	
	
	@PostMapping("/{id}/employees")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		return null;
		
//		CompanyDto company = findByIdOrThrowNotFound(id);
//		
//		company.getEmployees().add(employeeDto);
//		return company;
	}

	private CompanyDto findByIdOrThrowNotFound(long id) {
		CompanyDto company = companies.get(id);
		if(company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return company;
	}
	
	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		return null;
//		CompanyDto company = findByIdOrThrowNotFound(id);
//		company.getEmployees().removeIf(emp -> emp.getId() == employeeId);
//		return company;
	}
	
	@PutMapping("/{id}/employees")
	public CompanyDto replaceEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		return null;
//		CompanyDto company = findByIdOrThrowNotFound(id);
//		company.setEmployees(employees);
//		return company;
	}
	
}
