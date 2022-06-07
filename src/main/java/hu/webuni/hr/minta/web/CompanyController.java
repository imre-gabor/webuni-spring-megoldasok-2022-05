package hu.webuni.hr.minta.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import hu.webuni.hr.minta.mapper.CompanyMapper;
import hu.webuni.hr.minta.model.Company;
import hu.webuni.hr.minta.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	CompanyMapper companyMapper;
	

	//1. megoldás
	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full){
		List<Company> companies = companyService.findAll();

		if(isFull(full))
			return companyMapper.companiesToDtos(companies);
		else
			return companyMapper.companiesToDtosWithNoEmployees(companies);
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
		Company company = companyService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if(isFull(full))
			return companyMapper.companyToDto(company);
		else
			return companyMapper.companyToDtoWithNoEmployees(company);
	}
	
	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
      return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		companyDto.setId(id);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
		if (updatedCompany == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(companyMapper.companyToDto(updatedCompany));
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companyService.delete(id);
	}
	
	
	@PostMapping("/{id}/employees")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {

		return companyMapper.companyToDto(companyService.addEmployee(id, companyMapper.dtoToEmployee(employeeDto)));
	}

	
	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		return companyMapper.companyToDto(companyService.deleteEmployee(id, employeeId));
	}
	
	@PutMapping("/{id}/employees")
	public CompanyDto replaceEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		return companyMapper.companyToDto(companyService.replaceEmployees(id, companyMapper.dtosToEmployees(employees)));
	}
	
}
