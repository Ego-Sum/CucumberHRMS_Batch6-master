package com.hrms.API.steps.practice;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.*;
import org.junit.runners.MethodSorters;

//import org.apache.hc.core5.http.ContentType;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)


public class HardcodedExamples {
	
	static String baseURI = RestAssured.baseURI = "http://18.232.148.34/syntaxapi/api";
	String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTUxNzAyMDUsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTU5NTIxMzQwNSwidXNlcklkIjoiNjQ1In0.HqyLH-Q4fyUxowIns_ZnMQouZE356CfmiZbVHpucVFs";
	static String employeeID;
	
	public void sampleTestNotes () {
		

		RestAssured.baseURI = "http://18.232.148.34/syntaxapi/api";
		
		String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTUwOTYwOTksImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTU5NTEzOTI5OSwidXNlcklkIjoiOTI0In0.k2d3BcXDmAVkvkJMCn-JR-ZaHtSO-EFRWuO2KpU69gM";
		
		RequestSpecification getOneEmployeeRequest =  given().header("Content-Type", "application/json").header("Authorization", token).queryParam("employee_id", "16004A").log().all();
		
		Response getOneEmployeeResponse = getOneEmployeeRequest.when().log().all().get("/getOneEmployee.php");
		
		getOneEmployeeResponse.prettyPrint();
		//String response = getOneEmployeeResponse.body().asString();
		//System.out.println(response);
		
		getOneEmployeeResponse.then().assertThat().statusCode(200);
		
		
		
	}

	
	@Test
	public void aPOSTcreateEmployee() {
		
		
		RequestSpecification createEployeeRequest = given().header("Content-Type", "application/json").header("Authorization", token).body("{\r\n" + 
				"  \"emp_firstname\": \"syntaxFirstName\",\r\n" + 
				"  \"emp_lastname\": \"syntaxLastName\",\r\n" + 
				"  \"emp_middle_name\": \"syntaxMiddleName\",\r\n" + 
				"  \"emp_gender\": \"F\",\r\n" + 
				"  \"emp_birthday\": \"2000-07-11\",\r\n" + 
				"  \"emp_status\": \"Employee\",\r\n" + 
				"  \"emp_job_title\": \"Cloud Architect\"\r\n" + 
				"}");
		
		Response createEmployeeResponse = createEployeeRequest.when().post("/createEmployee.php");
		
		createEmployeeResponse.prettyPrint();
		
		employeeID = createEmployeeResponse.jsonPath().getString("Employee[0].employee_id");
		
		System.out.println(employeeID);
		
		createEmployeeResponse.then().assertThat().statusCode(201);
		
		createEmployeeResponse.then().assertThat().body("Message", equalTo("Entry Created"));
		
		createEmployeeResponse.then().assertThat().body("Employee[0].emp_firstname", equalTo("syntaxFirstName"));
		
		createEmployeeResponse.then().header("Server","Apache/2.4.39 (Win64) PHP/7.2.18");
		
		createEmployeeResponse.then().assertThat().header("Content-Type", "application/json");
		
		
		
		
		
		
		
		
	
	}

	@Test
	public void bGETcreateEmployee() {

		
		RequestSpecification getCreatedEmployeeRequeast = given().header("Content-Type", "application/json").header("Authorization", token).queryParam("employee_id", employeeID).log().all();
		
		Response getCreatedEmployeeResponse = getCreatedEmployeeRequeast.when().get("/getOneEmployee.php");
		
		String response = getCreatedEmployeeResponse.prettyPrint();
		
		String empID = getCreatedEmployeeResponse.body().jsonPath().getString("employee[0].employee_id");
		
		boolean verifyingEmployeeIDsMatch = empID.equalsIgnoreCase(employeeID);
		
		Assert.assertTrue(verifyingEmployeeIDsMatch);
		
		getCreatedEmployeeResponse.then().assertThat().statusCode(200);
		
		JsonPath js = new JsonPath(response);
		
		String emplID = js.getString("employee[0].employee_id");
		String firstName = js.getString("employee[0].emp_firstname");
		String middleName = js.getString("employee[0].emp_middle_name");
		String lastName = js.getString("employee[0].emp_lastname");
		String emp_bday = js.getString("employee[0].emp_birthday");
		String gender = js.getString("employee[0].emp_gender");
		String jobTitle = js.getString("employee[0].emp_job_title");
		String empStatus = js.getString("employee[0].emp_status");
		
		System.out.println(emplID);
		System.out.println(firstName);
		System.out.println(middleName);
		System.out.println(lastName);
		System.out.println(emp_bday);
		System.out.println(gender);
		System.out.println(jobTitle);
		System.out.println(empStatus);
		
		Assert.assertTrue(emplID.contentEquals(employeeID));
		Assert.assertEquals(employeeID, emplID );
		
		
		Assert.assertTrue(firstName.contentEquals("syntaxFirstName"));
		
		Assert.assertTrue(middleName.contentEquals("syntaxMiddleName"));
		
		Assert.assertTrue(lastName.contentEquals("syntaxLastName"));
		
		Assert.assertTrue(emp_bday.contentEquals("2000-07-11"));
		
		Assert.assertTrue(gender.contentEquals("Female"));
		
		Assert.assertTrue(jobTitle.contentEquals("Cloud Architect"));
		
		Assert.assertTrue(empStatus.contentEquals("Employee"));
		
		
		
		

		
		
		
		
		
		
		
	}
     
	@Test
    public void cGetallEmployee() {
		
		RequestSpecification getAllEmployeeRequest = given().header("Content-Type", "application/json").header("Authorization", token);
		
		Response getAllEmployeeResponse = getAllEmployeeRequest.when().get("/getAllEmployees.php");
		
		//getAllEmployeeResponse.prettyPrint();
		
		String allEmployees = getAllEmployeeResponse.body().asString();
		
		//The below will pass but incorrect
		
		//allEmployees.contains(employeeID);
		
		// --------DO RESEARCH ------
		
	    //	allEmployees.matches(employeeID);
		
		JsonPath js = new JsonPath(allEmployees);
		
		int sizeOfList = js.getInt("Employees.size()");
		
		System.out.println("----------");
		System.out.println(sizeOfList);
		
		for(int i = 0; i<sizeOfList; i++) {
			
			
			String allEmployeeIDs = js.getString("Employees["+ i +"].employee_id");
			
			//System.out.println(allEmployeeIDs);
			
			if (allEmployeeIDs.contentEquals(employeeID)) {
				
				System.out.println("Employee ID: " + employeeID + " is present in body" );
				
				String employeeFirstName = js.getString("Employees["+ i +"].emp_firstname");
				
				System.out.println(employeeFirstName);
				
				break;
						
				
			}
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}

	@Test
	public void dPUTupdateCreatedEmployee() {
		
		
	RequestSpecification updateCreateEmployeeRequest = given().header("Content-Type", "application/json").header("Authorization", token).body("{\r\n" + 
				"  \"employee_id\": \""+employeeID+"\",\r\n" + 
				"  \"emp_firstname\": \"syntaxUpdatedFirstName\",\r\n" + 
				"  \"emp_lastname\": \"syntaxUpdatedLastName\",\r\n" + 
				"  \"emp_middle_name\": \"syntaxUpdatedMiddleName\",\r\n" + 
				"  \"emp_gender\": \"F\",\r\n" + 
				"  \"emp_birthday\": \"2000-07-11\",\r\n" + 
				"  \"emp_status\": \"Employee\",\r\n" + 
				"  \"emp_job_title\": \"Cloud Consultant\"\r\n" + 
				"}");
		
	updateCreateEmployeeRequest //Response updateCreateEmployeeResponse = updateCreatedEmplo
	
		
	}
}
