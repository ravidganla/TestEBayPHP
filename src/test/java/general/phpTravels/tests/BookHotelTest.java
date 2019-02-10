package general.phpTravels.tests;		

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;		

public class BookHotelTest {				
	
	private WebDriver driver;
    private Properties allProperties = new Properties();
	private By cityName; 
	private By checkInDate; 
	private By checkOutDate; 
	private By searchButton; 
	private By hotelsList; 
	private By hotelDetails; 
	private By hotelRoomDetails; 
	private By roomSelectOption; 
	private By bookRoomButton; 
	private By bookConfirmHeader; 
	WebDriverWait wait;
    
	@Before
    public void setUp(Scenario scenario){
		allProperties = getProperties("ObjectProperties.properties");
		cityName = By.xpath(allProperties.getProperty("Common.City"));
		checkInDate = By.name(allProperties.getProperty("Hotel.CheckIn"));
		checkOutDate = By.name(allProperties.getProperty("Hotel.CheckOut"));
		searchButton = By.xpath(allProperties.getProperty("Hotel.Search"));
		hotelsList = By.xpath(allProperties.getProperty("CommonList.Items"));
		hotelDetails = By.xpath(allProperties.getProperty("CommonList.Details"));
		hotelRoomDetails = By.xpath(allProperties.getProperty("HotelItem.Room"));
		roomSelectOption = By.xpath(allProperties.getProperty("HotelItem.RoomSelect"));
		bookRoomButton = By.xpath(allProperties.getProperty("Common.BookRoom"));
		bookConfirmHeader = By.xpath(allProperties.getProperty("Common.Confirmation"));
    }	
	
	
    @Given("^Open \"(.*)\" site Hotels module on \"(.*)\" browser$")				
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
		wait = new WebDriverWait(driver, 10);
        driver.get(pSite);
    }		

    @When("^Book a hotel in \"(.*)\" city and between days \"(.*)\" and \"(.*)\"$")					
    public void bookHotelWithDetails(String pCity, String pStartDate, String pEndDate) throws InterruptedException{
		driver.findElement(checkInDate).click();
		String shiftTab = Keys.chord(Keys.SHIFT, Keys.TAB);
		driver.findElement(checkInDate).sendKeys(shiftTab);
		driver.findElement(cityName).clear();
		Actions actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(cityName)).sendKeys(pCity).build().perform();
		Thread.sleep(2000);
		actionElement.moveToElement(driver.findElement(cityName)).sendKeys(Keys.ENTER).build().perform();

	    driver.findElement(checkInDate).sendKeys(pStartDate);
	    driver.findElement(checkOutDate).sendKeys(pEndDate);
	    driver.findElement(searchButton).click();
	    
	    driver.findElement(hotelsList).findElement(hotelDetails).click();
	    
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)");

	    actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(hotelRoomDetails).findElement(roomSelectOption)).click().build().perform();
		Thread.sleep(500);
        js.executeScript("window.scrollBy(0,1000)");
		Thread.sleep(1000);
	    actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(bookRoomButton)).click().build().perform();
		Thread.sleep(1000);
    }		
    
    
    @Then("^Get Booking Confirmation$")
    public void confirmHotelBooking() throws Throwable {
		Assert.assertTrue(driver.findElement(bookConfirmHeader).getText().equals("BOOKING SUMMARY"));
		driver.quit();   }		
 
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