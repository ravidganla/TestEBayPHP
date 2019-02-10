package general.phpTravels.tests;		

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;		

public class BookFlightTest {				
	
	private WebDriver driver;
    private Properties allProperties = new Properties();
	private By cityName; 
	private By departureDate; 
	private By flightLink; 
	private By searchButton; 
	private By bookConfirmHeader; 
	WebDriverWait wait;
    
	@Before
    public void setUp(Scenario scenario){
		allProperties = getProperties("ObjectProperties.properties");
		cityName = By.xpath(allProperties.getProperty("Common.City"));
		flightLink = By.xpath(allProperties.getProperty("Flight.Tab"));
		departureDate = By.name(allProperties.getProperty("Flight.Departure"));
		searchButton = By.xpath(allProperties.getProperty("Flight.Search"));
		bookConfirmHeader = By.xpath(allProperties.getProperty("Common.Confirmation"));
    }	

    @Given("^Open \"(.*)\" site Flights module on \"(.*)\" browser$")				
    public void open_the_browser_and_launch_the_application(String pSite, String pBrowser){
    	switch(pBrowser.toLowerCase()) {
    		case "chrome" :
				System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
				driver= new ChromeDriver();
				break; 
    	   
    		case "ie" :
	   	        System.setProperty("webdriver.ie.driver", "src/test/resources/drivers/IEDriverServer.exe");
	   	        driver= new InternetExplorerDriver();
	   	        break; 
	   	        
    		default : 
    	        System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
    	        driver= new FirefoxDriver();
    	}
        driver.manage().window().maximize();			
		wait = (WebDriverWait) new WebDriverWait(driver, 30).ignoring(NoSuchElementException.class);
        driver.get(pSite);
	    driver.findElement(flightLink).click();
    }		

    @When("^Book ticket from \"(.*)\" to \"(.*)\" on date \"(.*)\"$")					
    public void enterTicketDetails(String pFrom, String pTo, String pDate) throws InterruptedException{
		driver.findElement(departureDate).click();
		String shiftTab = Keys.chord(Keys.SHIFT, Keys.TAB);
		driver.findElement(departureDate).sendKeys(shiftTab);
		driver.findElement(cityName).clear();
		Actions actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(cityName)).sendKeys(pTo).build().perform();
		Thread.sleep(2000);
		
		actionElement = new Actions(driver);
		driver.findElement(departureDate).sendKeys(shiftTab);
		driver.findElement(cityName).clear();
		Thread.sleep(2000);
		driver.findElement(cityName).sendKeys(pFrom);
		Thread.sleep(3000);
		driver.findElement(cityName).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		
	    driver.findElement(departureDate).sendKeys(pDate);
		Thread.sleep(3000);
		actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(searchButton)).click().build().perform();
		Thread.sleep(3000);
	    driver.findElement(searchButton).click();
	    Thread.sleep(3000);
		
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//table[@id='load_data']/tbody/tr[1]/td")).findElement(By.xpath("//*[@id='bookbtn']")))).click();
	    Thread.sleep(3000);
    }		
    
    @Then("^Get Flight Booking Confirmation$")
    public void validateFlightBooking() {		
    	Assert.assertTrue(driver.findElement(bookConfirmHeader).getText().equals("BOOKING SUMMARY"));
    	driver.quit();   	}		
  
	private Properties getProperties(String pPropertiesFile) {
		Properties methodProperties = new Properties();
	    InputStream stream = null;
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    stream = loader.getResourceAsStream(pPropertiesFile);
	    
	    try {
	    	methodProperties.load(stream);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return methodProperties;
	}

}