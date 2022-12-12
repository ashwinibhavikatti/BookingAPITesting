Feature: Regression Test Cases for Ticket Booking Application


  Scenario: Get auth token for bookings service - valid credentials
    Given The end point "/auth"
    And The User "admin" and password "password123"
    When The Token retrieved
    Then Say "Successfully token retrieved"


  Scenario: Get auth token for bookings service - invalid credentials
    Given The end point "/auth"
    And The User "admin_user" and password "welcome123"
    When Failed to retrieve token
    Then Say "Token retrieval failed due to invalid credentials"
    

  Scenario: Get all bookings
    Given The end point "/booking"
    When Retrieve the all bookings
    Then Say "Successfully retrieved"
 
  
  Scenario: Create booking with valid deatils
    Given The end point "/booking"
    When Send booking deatails to "Create"
    And with payload start
    And with firstname "<firstname>"
    And with lastname "<lastname>"
    And with totalprice <totalprice>
    And with depositpaid "<depositpaid>"
    And with checkin "<checkin>" and checkout "<checkout>"
    And with additionalneeds "<additionalneeds>"
    And with payload end
    And Request to create
    Then Say "Created Successfully" 

		Examples: Container for booking creation details
		|firstname	|lastname	|totalprice	|depositpaid	|checkin		|checkout		|additionalneeds	|
		|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|
		|Prasa			|BBB				|800			|false					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|

 
  Scenario: Try to get a specific booking with valid bookig Id
  	Given The end point "/booking"
    When Send booking deatails to "Create"
    And with payload start
    And with firstname "<firstname>"
    And with lastname "<lastname>"
    And with totalprice <totalprice>
    And with depositpaid "<depositpaid>"
    And with checkin "<checkin>" and checkout "<checkout>"
    And with additionalneeds "<additionalneeds>"
    And with payload end
    And Request to create
    Given The end point "/booking"
    And The booking id "Recently_Created"
    When Retrieve booking details by valid BookingId
    Then Say "Successfully retrieved booking details"
    
		Examples: Container for booking creation details
		|firstname	|lastname	|totalprice	|depositpaid	|checkin		|checkout		|additionalneeds	|
		|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|

  Scenario: Try to get a specific booking with invalid bookig Id
    Given The end point "/booking"
    And The booking id "wrongID"
    When Retrieve booking details by invalid BookingId
    Then Say "Invalid booking ID, Could not find the details"
      
  
 	Scenario: Update booking with valid deatils using PUT
 		Given The end point "/booking"
    When Send booking deatails to "Create"
    And with payload start
    And with firstname "<firstname>"
    And with lastname "<lastname>"
    And with totalprice <totalprice>
    And with depositpaid "<depositpaid>"
    And with checkin "<checkin>" and checkout "<checkout>"
    And with additionalneeds "<additionalneeds>"
    And with payload end
    And Request to create
    Given The end point "/auth"
    And The User "admin" and password "password123"
    When The Token retrieved
    Given The end point "/booking"
    And The booking id "<bookingId1>"
    When with payload start
    And with firstname "<firstname1>"
    And with lastname "<lastname1>"
    And with totalprice <totalprice1>
    And with depositpaid "<depositpaid1>"
    And with checkin "<checkin1>" and checkout "<checkout1>"
    And with additionalneeds "<additionalneeds1>"
    And with payload end
    And Request to update whole booking
    Then Say "updated Successfully"
    
		Examples: Container for booking details
		|firstname	|lastname	|totalprice	|depositpaid	|checkin		|checkout		|additionalneeds	|bookingId1 |firstname1	|lastname1	|totalprice1	|depositpaid1	|checkin1		|checkout1		|additionalneeds1	|
		|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|Recently_Created|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|


 Scenario: Update booking with valid deatils using PATCH
 		Given The end point "/booking"
    When Send booking deatails to "Create"
    And with payload start
    And with firstname "<firstname>"
    And with lastname "<lastname>"
    And with totalprice <totalprice>
    And with depositpaid "<depositpaid>"
    And with checkin "<checkin>" and checkout "<checkout>"
    And with additionalneeds "<additionalneeds>"
    And with payload end
    And Request to create
    Given The end point "/auth"
    And The User "admin" and password "password123"
    When The Token retrieved
    Given The end point "/booking"
    And The booking id "<bookingId1>"
    When with payload start
    And with firstname "<firstname1>"
    And with lastname "<lastname1>"
    And with totalprice <totalprice1>
    And with payload end
    And Request to update partial booking details
    Then Say "updated Successfully"
    
		Examples: Container for booking details
		|firstname	|lastname	|totalprice	|depositpaid	|checkin		|checkout		|additionalneeds	|bookingId1 |firstname1	|lastname1	|totalprice1	|depositpaid1	|checkin1		|checkout1		|additionalneeds1	|
		|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|Recently_Created|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|

 Scenario: Delete booking with valid booking ID
 		Given The end point "/booking"
    When Send booking deatails to "Create"
    And with payload start
    And with firstname "<firstname>"
    And with lastname "<lastname>"
    And with totalprice <totalprice>
    And with depositpaid "<depositpaid>"
    And with checkin "<checkin>" and checkout "<checkout>"
    And with additionalneeds "<additionalneeds>"
    And with payload end
    And Request to create
    Given The end point "/auth"
    And The User "admin" and password "password123"
    When The Token retrieved
    Given The end point "/booking"
    And The booking id "Recently_Created"
    When Delete the valid booking
    Then Say "Successfully Deleted"
    
		Examples: Container for booking creation details
		|firstname	|lastname	|totalprice	|depositpaid	|checkin		|checkout		|additionalneeds	|
		|ashu				|bb				|300				|true					|2022-12-30	|2022-12-31	|Breakfast,lunch,dinner|
 		
    
  
 Scenario: Delete booking with invalid booking ID
 		Given The end point "/auth"
    And The User "admin" and password "password123"
    When The Token retrieved
    Given The end point "/booking"
    And The booking id "wrongId"
    When Delete the invalid booking
    Then Say "Could not find to delete"
    