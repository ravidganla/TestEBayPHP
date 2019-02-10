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

public class BookTourTest {				
	
	private WebDriver driver;
    private Properties allProperties = new Properties();
	private By tourName; 
	private By toursLink; 
	private By tourStartDate; 
	private By searchButton; 
	private By itemsList; 
	private By itemsDetails; 
	private By bookTourButton; 
	private By bookConfirmHeader; 
	WebDriverWait wait;
    
	@Before
    public void setUp(Scenario scenario){
		allProperties = getProperties("ObjectProperties.properties");
		tourName = By.xpath(allProperties.getProperty("Common.City"));
		toursLink = By.xpath(allProperties.getProperty("Tours.Tab"));
		tourStartDate = By.name(allProperties.getProperty("Tours.Departure"));
		searchButton = By.xpath(allProperties.getProperty("Tours.Search"));
		itemsList = By.xpath(allProperties.getProperty("CommonList.Items"));
		itemsDetails = By.xpath(allProperties.getProperty("CommonList.Details"));
		bookTourButton = By.xpath(allProperties.getProperty("Common.BookRoom"));
		bookConfirmHeader = By.xpath(allProperties.getProperty("Common.Confirmation"));
    }
	
	
	@Given("^Open \"(.*)\" site Tours module on \"(.*)\" browser$")				
	public void open_the_browser_and_launch_the_application(String pSite, String pBrowser){
		System.out.println("pBrowser: " + pBrowser);
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
	    driver.findElement(toursLink).click();
    }		

    @When("^Book \"(.*)\" tour on date \"(.*)\"$")					
    public void enterTourDetailsAndBook(String pTour, String pDate) throws InterruptedException{
		driver.findElement(tourStartDate).click();
		String shiftTab = Keys.chord(Keys.SHIFT, Keys.TAB);
		driver.findElement(tourStartDate).sendKeys(shiftTab);
		driver.findElement(tourName).clear();
		Actions actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(tourName)).sendKeys(pTour).build().perform();
		Thread.sleep(2000);
		actionElement.moveToElement(driver.findElement(tourName)).sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(2000);

	    driver.findElement(tourStartDate).clear();;
	    driver.findElement(tourStartDate).sendKeys(pDate);
		Thread.sleep(2000);
	    driver.findElement(searchButton).click();
	    
	    driver.findElement(itemsList).findElement(itemsDetails).click();
	    
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)");

		Thread.sleep(1000);
	    actionElement = new Actions(driver);
		actionElement.moveToElement(driver.findElement(bookTourButton)).click().build().perform();
		Thread.sleep(1000);
    }		
    
    
    @Then("^Get Tour Booking Confirmation$")
    public void confirmTourBooking() throws Throwable {
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