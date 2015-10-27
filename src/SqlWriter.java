import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlWriter {

	int locationId, employeeId, servicesId, toppingsId, crustsId, managerId, managerEmployeeId;
	int locationServiceId, locationManageId, locationToppingsId, locationCrustsId, locationEmployeeId;
	String location, employee, services, toppings, crusts, manager, managerEmployee;
	String locationEmployee, locationServices, locationToppings, locationCrusts, locationManager;
	List<Integer> managerList, employeeList, toppingList, crustList, locationList, serviceList;
	
	public static void main(String[] args) throws IOException {
		SqlWriter sql = new SqlWriter();
		String line;
		BufferedReader br = new BufferedReader(new FileReader("./schema.txt"));
		sql.location = "";
		sql.employee = "";
		sql.services = "";
		sql.toppings = "";
		sql.crusts = "";
		sql.manager = "";
		sql.managerEmployee = "";
		boolean emp = false;
		while((line = br.readLine()) != null){
			if(line.contains("Branch No")){
				if(emp){
					sql.createManagerEmployee();
					sql.locationTables();
					emp = false;
				}
				emp = false;
				sql.managerList = new ArrayList<Integer>();
				sql.employeeList = new ArrayList<Integer>();
				sql.toppingList = new ArrayList<Integer>();
				sql.crustList = new ArrayList<Integer>();
				sql.locationList = new ArrayList<Integer>();
				sql.serviceList = new ArrayList<Integer>();
			}
			if(emp){
				if(!line.trim().isEmpty())
					sql.employees(line);
			}
			else{
				if(line.contains("Location:")){
					sql.location(line);
				}
				
				else if(line.contains("Manager:")){
					sql.manager(line);
				}
				
				else if(line.contains("Crusts:")){
					sql.crusts(line);
				}
				
				else if(line.contains("Toppings:")){
					sql.toppings(line);
				}
				
				else if(line.contains("Services:")){
					sql.services(line);
				}
				
				else if(line.contains("Employees:")){
					emp = true;
				}
			}
		}
		br.close();
		FileWriter writer = new FileWriter("locationInserts.sql");
		writer.write(sql.location);
		writer.close();
		writer = new FileWriter("employeeInserts.sql");
		writer.write(sql.employee);
		writer.close();
		writer = new FileWriter("managerInserts.sql");
		writer.write(sql.manager);
		writer.close();
		writer = new FileWriter("servicesInserts.sql");
		writer.write(sql.services);
		writer.close();
		writer = new FileWriter("toppingsInserts.sql");
		writer.write(sql.toppings);
		writer.close();
		writer = new FileWriter("crustsInserts.sql");
		writer.write(sql.crusts);
		writer.close();
		writer = new FileWriter("locationServicesInserts.sql");
		writer.write(sql.locationServices);
		writer.close();
		writer = new FileWriter("locationToppingsInserts.sql");
		writer.write(sql.locationToppings);
		writer.close();
		writer = new FileWriter("locationCrustsInserts.sql");
		writer.write(sql.locationCrusts);
		writer.close();
		writer = new FileWriter("locationManagerInserts.sql");
		writer.write(sql.locationManager);
		writer.close();
		writer = new FileWriter("locationEmployeeInserts.sql");
		writer.write(sql.locationEmployee);
		writer.close();
	}

	public void location(String line){
		line = line.replace("Location: ", "");
		if(!location.contains(", " + line.trim())){
			locationId++;
			locationList.add(locationId);
			location += "\n\rinsert into LOCATION(LOCATION_ID, CITY) VALUES (" + (locationId) + ", " + line.trim() + ");";
		}
	}
	
	public void manager(String line){
		line = line.replace("Manager: ", "");
		String lastName = line.substring(0, line.indexOf(","));
		String firstName = line.replace(lastName + " ", "");
		if(!manager.contains(", " + firstName.trim() + ", " + lastName.trim() + ")")){
			managerId++;
			managerList.add(managerId);
			manager += "\n\rinsert into MANAGER(MANAGER_ID, FIRST_NAME, LAST_NAME) VALUES (" + (managerId) + ", \"" + firstName.trim() + "\", \"" + lastName.trim() + "\");";
		}
	}
	
	public void crusts(String line){
		line = line.replace("Crusts: ", "");
		List<String> crustList = Arrays.asList(line.split(","));
		for(int i = 0; i<crustList.size(); i++){
			if(!crusts.contains(", " + crustList.get(i).trim())){
				crustsId++;
				this.crustList.add(crustsId);
				crusts += "\n\rinsert into CRUSTS(CRUSTS_ID, CRUST_TYPE) VALUES (" + (crustsId) + ", " + crustList.get(i).trim() + ");";
			}
		}
		
	}
	
	public void toppings(String line){
		line = line.replace("Toppings: ", "");
		List<String> toppingsList = Arrays.asList(line.split(","));
		for(int i = 0; i<toppingsList.size(); i++){
			if(!toppings.contains(", " + toppingsList.get(i).trim())){
				toppingsId++;
				toppingList.add(toppingsId);
				toppings += "\n\rinsert into TOPPINGS(TOPPINGS_ID, TOPPINGS_TYPE) VALUES (" + (toppingsId) + ", " + toppingsList.get(i).trim() + ");";
			}
		}
	}
	
	public void services(String line){
		line = line.replace("Services: ", "");
		List<String> servicesList = Arrays.asList(line.split(","));	
		for(int i = 0; i<servicesList.size(); i++){
			if(!services.contains(", " + servicesList.get(i).trim())){
				servicesId++;
				serviceList.add(servicesId);
				services += "\n\rinsert into Services(Services_ID, Services_TYPE) VALUES (" + (servicesId) + ", " + servicesList.get(i).trim() + ");";
			}
		}
	}
	
	public void employees(String line){
		String lastName = line.substring(0, line.indexOf(","));
		String firstName = line.replace(lastName + ", ", "");
		if(!employee.contains(", " + firstName.trim() + ", " + lastName.trim() + ")")){
			employeeId++;
			employeeList.add(employeeId);
			employee += "\n\rinsert into EMPLOYEES(EMPLOYEE_ID, FIRST_NAME, LAST_NAME) VALUES (" + (employeeId) + ", \"" + firstName.trim() + "\", \"" + lastName.trim() + "\");";
		}
	}
	
	public void createManagerEmployee(){
		for(Integer manager : managerList){
			for(Integer employee : employeeList){
				managerEmployeeId++;
				managerEmployee += "\n\rinsert into managerEmployee(managerEmployee_ID, MANAGER_ID, EMPLOYEE_ID) VALUES (" + (managerEmployeeId) + ", " + manager + ", " + employee + ");";
			}
		}
	}
	
	public void locationTables(){
		for(Integer id : crustList){
			locationCrustsId++;
			locationCrusts += "\n\rinsert into LocationCrusts(Location_ID, crust_ID) VALUES (" + locationId + ", " + id + ");";
		}
		for(Integer id : employeeList){
			locationEmployeeId++;
			locationEmployee += "\n\rinsert into LocationEmployee(Location_ID, employee_ID) VALUES (" + locationId + ", " + id + ");";
		}
		locationManager += "\n\rinsert into LocationManager(Location_ID, manager_ID) VALUES (" + locationId + ", " + managerId + ");";
		for(Integer id : toppingList){
			locationToppingsId++;
			locationToppings += "\n\rinsert into LocationToppings(Location_ID, topping_ID) VALUES (" + locationId + ", " + id + ");";
		}
		for(Integer id : serviceList){
			locationServiceId++;
			locationServices += "\n\rinsert into LocationService(Location_ID, service_ID) VALUES (" + locationId + ", " + id + ");";
		}
	}

}
