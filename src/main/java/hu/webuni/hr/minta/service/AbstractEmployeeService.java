package hu.webuni.hr.minta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import hu.webuni.hr.minta.model.Employee;
import hu.webuni.hr.minta.model.Position;
import hu.webuni.hr.minta.repository.EmployeeRepository;
import hu.webuni.hr.minta.repository.PositionRepository;

@Service
public abstract class AbstractEmployeeService implements EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Override
	public Employee save(Employee employee) {
		employee.setEmployeeId(null);
		Position position = employee.getPosition();
		if(position != null) {
			String positionName = position.getName();
			if(!ObjectUtils.isEmpty(positionName)) {
				Position positionInDb = null;
				Optional<Position> foundPosition = positionRepository.findByName(positionName);
				if(foundPosition.isPresent())
					positionInDb = foundPosition.get();
				else {
					positionInDb = positionRepository.save(position);
				}
				employee.setPosition(positionInDb);
			} else {
				employee.setPosition(null);
			}
		}
		return employeeRepository.save(employee);
	}

	@Override
	public Employee update(Employee employee) {
		if(!employeeRepository.existsById(employee.getEmployeeId()))
			return null;
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> findById(long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
}
