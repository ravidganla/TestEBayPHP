package general.forecast.tests;		

import static io.restassured.RestAssured.given;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;		

public class Forecast {				
    
	Response event;
	String holidayCity;
	
	@Before
    public void setUp(){
        RestAssured.baseURI = "http://api.openweathermap.org";
        RestAssured.basePath = "data/2.5/weather";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }	
	
    @Given("^I like to holiday in \"(.*)\"$")			
    public void setDestination(String pCity){
    	holidayCity = pCity;
    }		
	
    @Given("^I only like to holiday on \"(.*)\"$")			
    public void setDate(String pDay){
    	DateTimeFormatter formatterOutput = DateTimeFormat.forPattern("EEEEEEEEE").withLocale(Locale.US);
    	Assert.assertTrue("Today is not " + pDay + ". Plan another day", pDay.equals(formatterOutput.print((new Date()).getTime())));
    }		
	
	@SuppressWarnings("deprecation")
	@When("I look up the weather forecast")
    public void requestWeatherForecast(){
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("q", holidayCity);
    	parameters.put("units", "metric");
    	parameters.put("APPID", "56a4ffddceff2d167e9c03c0702f1e89");

		event = given().parameters(parameters).when().get();
		event.prettyPeek();
    }		
	
    @Then("^I receive the weather forecast$")					
    public void validateForecast() throws Throwable {
    	Assert.assertTrue("No forecast returned", event.statusCode()==200);
    }		
    
    
    @Then("^the temperature is warmer than (\\d+) degrees$")					
    public void validateTemperature(int pTemperature) throws Throwable {
    	Assert.assertTrue("The temperature is cooler than " + pTemperature, event.jsonPath().getFloat("main.temp_min") > pTemperature);
    }

}