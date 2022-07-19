package hu.webuni.hr.minta.security;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.hr.minta.config.HrConfigProperties;
import hu.webuni.hr.minta.config.HrConfigProperties.Jwt;
import hu.webuni.hr.minta.model.Employee;
import hu.webuni.hr.minta.model.HrUser;

@Service
public class JwtService {

	private static final String MANAGED_EMPLOYEES = "managedEmployees";
	private static final String USERNAME = "username";
	private static final String MANAGER = "manager";
	private static final String ID = "id";
	private static final String FULLNAME = "fullname";
	private static final String AUTH = "auth";
	
	@Autowired
	private HrConfigProperties config;
	
	private Algorithm alg;
	private String issuer;
	
	@PostConstruct
	public void init() {
		Jwt jwt = config.getJwt();
		issuer = jwt.getIssuer();
		try {
			alg = (Algorithm) Algorithm.class.getMethod(jwt.getAlg(), String.class)
				.invoke(Algorithm.class, jwt.getSecret());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public String creatJwtToken(UserDetails principal) {
		Employee employee = ((HrUser)principal).getEmployee();
		Employee manager = employee.getManager();
		Collection<Employee> managedEmployees = employee.getManagedEmployees();
		
		Builder jwtBuilder = JWT.create()
			.withSubject(principal.getUsername())
			.withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withClaim(FULLNAME, employee.getName())
			.withClaim(ID, employee.getEmployeeId());

		if(manager != null) {
			jwtBuilder.withClaim(MANAGER, createEmployeeData(manager));
		}
		
		
		if(managedEmployees != null && !managedEmployees.isEmpty()) {
			jwtBuilder.withClaim(MANAGED_EMPLOYEES, 
					managedEmployees.stream().map(this::createEmployeeData).collect(Collectors.toList()));
		}
		
		return jwtBuilder
			.withExpiresAt(new Date(System.currentTimeMillis() + config.getJwt().getExpiry().toMillis()))
			.withIssuer(issuer)
			.sign(alg);
		
	}

	private Map<String, Object> createEmployeeData(Employee employee) {
		return Map.of(
				ID, employee.getEmployeeId(),
				USERNAME, employee.getUsername()
				);
	
	}

	public UserDetails parseJwt(String jwtToken) {
		
		DecodedJWT decodedJwt = JWT.require(alg)
			.withIssuer(issuer)
			.build()
			.verify(jwtToken);
		
		Employee employee = new Employee();

		employee.setEmployeeId(decodedJwt.getClaim(ID).asLong());
		employee.setUsername(decodedJwt.getSubject());
		employee.setName(decodedJwt.getClaim(FULLNAME).asString());
		
		Claim managerClaim = decodedJwt.getClaim(MANAGER);
		if(managerClaim != null) {
			Map<String, Object> managerData = managerClaim.asMap();
			employee.setManager(parseEmployee(managerData));
		}
		
		
		Claim managedEmployeesClaim = decodedJwt.getClaim(MANAGED_EMPLOYEES);
		if(managedEmployeesClaim != null) {
			employee.setManagedEmployees(new ArrayList<>());
			List<HashMap> managedEmployees = managedEmployeesClaim.asList(HashMap.class);
			if(managedEmployees != null) {
				for (HashMap employeeMap : managedEmployees) {
					Employee managedEmployee = parseEmployee(employeeMap);
					if(managedEmployee != null)
						employee.getManagedEmployees().add(managedEmployee);
				}
			}
		}
		
		return new HrUser(decodedJwt.getSubject(), "dummy", 
				decodedJwt.getClaim(AUTH).asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
				employee
				);
		
	}

	private Employee parseEmployee(Map<String, Object> employeeData) {

		if(employeeData != null) {
			Employee employee = new Employee();
			employee.setEmployeeId(((Integer)employeeData.get(ID)).longValue());
			employee.setUsername((String) employeeData.get(USERNAME));
			
			return employee;
		}
		return null;
	}

}
