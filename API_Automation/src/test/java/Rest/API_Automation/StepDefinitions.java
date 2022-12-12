package Rest.API_Automation;


import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;

public class StepDefinitions {
	private final String BASE_URL = "https://restful-booker.herokuapp.com";
    private Response oResponse;
    private String sToken;
    private URI sEndPoint;
    private String sPayload = "";
    private String sBookingId;
    private String sLastCreatedBookingId = "";
    
    @Given("The end point {string}")
    public void setEndPoint(String sUrl) throws URISyntaxException {
    	RestAssured.baseURI = BASE_URL;
    	sEndPoint = new URI(sUrl);
    }
    
    @Given("The User {string} and password {string}")
    public void setLoginPayload(String sUserName, String sPwd) {
    	 sPayload = "{\"username\" : \""+sUserName+"\",\"password\" : \""+sPwd+"\"}";
    }
    
    @When("The Token retrieved")
    public void getToken_validCredentials(){
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.when().post(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Invalid End-Point", oResponse.getStatusCode(), 404);
    	Assert.assertNotEquals("Bad request", oResponse.getStatusCode(), 400);
    	sToken = oResponse.jsonPath().getString("token");
    	Assert.assertNotNull("Invalid User", sToken);
    	
    }
    
    //
    
    @Then("Say {string}")
    public void saySuccess(String sMessage) {
    	System.out.println(sMessage);  
    }
    
    @When("Failed to retrieve token")
    public void getToken_invalidCredentials() {
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.when().post(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Invalid End-Point", oResponse.getStatusCode(), 404);
    	Assert.assertNotEquals("Bad request", oResponse.getStatusCode(), 400);
    	Assert.assertNull("Valid credentials", oResponse.jsonPath().getString("token"));
    }
    
    @When("Retrieve the all bookings")
    public void getAllBookings() {
    	oResponse = RestAssured.given()
    			.get(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Invalid End-Point", oResponse.getStatusCode(), 404);
    	int iResponseSize = Integer.parseInt(oResponse.jsonPath().getString("data.size()"));
    	Assert.assertTrue("No Bookings found",iResponseSize > 0 );
    }
    
    @Given("The booking id {string}")
    public void setEndpointForBookingDetailsById(String sId) throws URISyntaxException{
    	
    	if(sId.contentEquals("Recently_Created")) {
    		sEndPoint= new URI(sEndPoint.toString()+"/"+sLastCreatedBookingId);
    	}
    	else {
    		sBookingId = sId;
    		sEndPoint= new URI(sEndPoint.toString()+"/"+sBookingId);
    	}
  
    }
    
    @When("Retrieve booking details by valid BookingId")
    public void getBookingDetailsByValidId() {
    	oResponse = RestAssured.given()
    			.get(sEndPoint)
    			.then().extract().response();
    	Assert.assertTrue("Booking Id:"+sBookingId+" is invalid/Invalid End-Point", oResponse.getStatusCode()== 200);
    	Assert.assertNotNull("Booking Id:"+sBookingId+" is invalid/Invalid End-Point", oResponse.jsonPath().getString("firstname"));
}
    @When("Retrieve booking details by invalid BookingId")
    public void getBookingDetailsByInvalidId() {
    	oResponse = RestAssured.given()
    			.get(sEndPoint)
    			.then().extract().response();
    	Assert.assertTrue("Booking Id:"+sBookingId+" is valid", oResponse.getStatusCode() == 404);
    }

    @When("Send booking deatails to {string}")
    public void post_for(String sCallTo) {
    	sPayload = "";
    }
    
    @When("with payload start")
    public void payload_start() {
    	sPayload = "";
    	sPayload = sPayload +"{";
    }
    
    @When("with payload end")
    public void payload_end() {
    	sPayload = sPayload +"}";
    }
    
    @When("with firstname {string}")
    public void add_firstname_to_payload(String firstname) {
    		sPayload = sPayload + "    \"firstname\" : \""+firstname+"\"";
    }
    
    @When("with lastname {string}")
    public void add_lastname_to_payload(String lastname) {
    		sPayload = sPayload + ",    \"lastname\" : \""+lastname+"\"";
    }
    
    @When("with totalprice {int}")
    public void add_totalprice_to_payload(Integer totalprice) {
    		sPayload = sPayload + ",    \"totalprice\" : "+totalprice+"";
    }
    
    @When("with depositpaid {string}")
    public void add_depositpaid_to_payload(String depositpaid) {
    		sPayload = sPayload + ",    \"depositpaid\" : "+depositpaid+"";
    }
    
    @When("with checkin {string} and checkout {string}")
    public void add_bookingdates_to_payload(String checkin, String checkout) {
    		sPayload = sPayload + ",   \"bookingdates\" : {";
    		sPayload = sPayload + "    \"checkin\" : \""+checkin+"\",";
    		sPayload = sPayload + "    \"checkout\" : \""+checkout+"\"";
    		sPayload = sPayload + "    }";
    }
    
    @When("with additionalneeds {string}")
    public void add_additionalneeds_to_payload(String additionalneeds) {
    		sPayload = sPayload + ",    \"additionalneeds\" : \""+additionalneeds+"\"";
    }
    
    @When("Request to create")
    public void post_to_create_booking() {
    	
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.when().post(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Internal Server Error", oResponse.getStatusCode(), 500);
    	Assert.assertNotEquals("Bad Request", oResponse.getStatusCode(), 400);
    	Assert.assertEquals("Could not create booking. Error code:"+oResponse.getStatusCode(), oResponse.getStatusCode(), 200);
    	sLastCreatedBookingId = oResponse.jsonPath().getString("bookingid");
    	
    }
    
    @When("Request to update whole booking")
    public void put_to_update_whole_booking() {
    	
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.header("cookie", "token="+sToken)
    			.when().put(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Internal Server Error", oResponse.getStatusCode(), 500);
    	Assert.assertNotEquals("Bad Request", oResponse.getStatusCode(), 400);
    	Assert.assertEquals("Could not update booking. Error code:"+oResponse.getStatusCode(), oResponse.getStatusCode(), 200);
    }
    
    @When("Request to update partial booking details")
    public void patch_to_update_partial_booking() {
    	
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.header("cookie", "token="+sToken)
    			.when().patch(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Internal Server Error", oResponse.getStatusCode(), 500);
    	Assert.assertNotEquals("Bad Request", oResponse.getStatusCode(), 400);
    	Assert.assertEquals("Could not update booking. Error code:"+oResponse.getStatusCode(), oResponse.getStatusCode(), 200);
    }
    
    @When("Delete the valid booking")
    public void delete_valid_booking() {
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.header("cookie", "token="+sToken)
    			.when().delete(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Internal Server Error", oResponse.getStatusCode(), 500);
    	Assert.assertNotEquals("Bad Request", oResponse.getStatusCode(), 400);
    	Assert.assertEquals("Could not delete booking. Error code:"+oResponse.getStatusCode(), oResponse.getStatusCode(), 201);
    }
    
    @When("Delete the invalid booking")
    public void delete_invalid_booking() {
    	oResponse = RestAssured.given()
    			.contentType(ContentType.JSON)
    			.body(sPayload)
    			.header("cookie", "token="+sToken)
    			.when().delete(sEndPoint)
    			.then().extract().response();
    	Assert.assertNotEquals("Booking not found. Error code:"+oResponse.getStatusCode(), oResponse.getStatusCode(), 201);
    }
}
