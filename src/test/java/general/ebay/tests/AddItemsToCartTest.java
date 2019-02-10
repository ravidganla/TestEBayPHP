package general.ebay.tests;		

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;		
import cucumber.api.java.en.Then;		
import cucumber.api.java.en.When;		

public class AddItemsToCartTest {				
	
	private WebDriver driver;
	private Properties allProperties = new Properties();
	private By mainSearchText; 
	private By mainSearchButton; 
	private By allItems; 
	private By itemText; 
	private By addCartButton; 
	private By gotoCartButton; 
	private By removeItemLink; 
	private By itemCountHeader; 
	private By billSummaryCount; 
	private By checkoutButton; 
	private By checkoutHeader; 
	WebDriverWait wait;
    
	@Before
    public void setUp(Scenario scenario){
		allProperties = getProperties("ObjectProperties.properties");
		mainSearchText = By.id(allProperties.getProperty("MainHeader.SearchText"));
		mainSearchButton = By.id(allProperties.getProperty("MainHeader.SearchButton"));
		allItems = By.xpath(allProperties.getProperty("ShoppingList.TotalItems"));
		itemText = By.xpath(allProperties.getProperty("ShoppingList.ItemText"));
		addCartButton = By.id(allProperties.getProperty("ItemPage.AddToCart"));
		gotoCartButton = By.xpath(allProperties.getProperty("ConfirmPopup.GoToCart"));
		removeItemLink = By.xpath(allProperties.getProperty("Cart.RemoveLink"));
		itemCountHeader = By.xpath(allProperties.getProperty("Cart.HeaderText"));
		billSummaryCount = By.xpath(allProperties.getProperty("Cart.BillSummary"));
		checkoutButton = By.xpath(allProperties.getProperty("Cart.CheckoutButton"));
		checkoutHeader = By.id(allProperties.getProperty("Checkout.HeaderText"));
    }	
	
	@After
    public void cleanUp() throws InterruptedException{
		driver.navigate().back();
	    Actions actionElement = new Actions(driver);
	    actionElement.moveToElement(driver.findElement(removeItemLink)).click().build().perform();;
		Thread.sleep(1000);
	    actionElement = new Actions(driver);
	    actionElement.moveToElement(driver.findElement(removeItemLink)).click().build().perform();;
	    driver.quit();
	}	
	
    @Given("^Open \"(.*)\" website on \"(.*)\" browser$")				
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

    @When("^Enter \"(.*)\" by \"(.*)\" and \"(.*)\" by \"(.*)\" items to purchase$")					
    public void enterPurchaseItems(String pBook1, String pAuthor1, String pBook2, String pAuthor2) throws InterruptedException{
       driver.findElement(mainSearchText).sendKeys(pBook1 + " " + pAuthor1);
       driver.findElement(mainSearchButton).click();
       
       List<WebElement> totalItems = driver.findElements(allItems);
       for (WebElement eachItem: totalItems) {
    	   String itemTextString = eachItem.findElement(itemText).getText().toLowerCase();
    	   if(itemTextString.contains(pBook1) && itemTextString.contains(pAuthor1)) {
    		   eachItem.findElement(itemText).click();
    		   driver.findElement(addCartButton).click();
    		   Thread.sleep(1000);
    		   wait.until(ExpectedConditions.elementToBeClickable(gotoCartButton)).click();;
    		   break;
    	   }
       }

       driver.findElement(mainSearchText).sendKeys(pBook2 + " " + pAuthor2);
       driver.findElement(mainSearchButton).click();
       
       totalItems = driver.findElements(allItems);
       for (WebElement eachItem: totalItems) {
		   String itemTextString = eachItem.findElement(itemText).getText().toLowerCase();
		   if(itemTextString.contains(pBook2) && itemTextString.contains(pAuthor2)) {
			   eachItem.findElement(itemText).click();
			   driver.findElement(addCartButton).click();
			   Thread.sleep(1000);
			   wait.until(ExpectedConditions.elementToBeClickable(gotoCartButton)).click();;
			   break;
		   }
       }
    }		
    
    
    @Then("^Verify \"(.*)\" Items added to the cart$")					
    public void validateItemsOnCart(String pTotalItems) throws Throwable {    		
		Assert.assertTrue(driver.findElement(itemCountHeader).getText().equals("Shopping cart ("+ pTotalItems +" items)"));
		Assert.assertTrue(driver.findElement(billSummaryCount).getText().equals("Items (" + pTotalItems + ")"));
    }		
    
    
    @Then("^Go to checkout$")					
    public void validateCheckOut() throws Throwable {    		
    	driver.findElement(checkoutButton).click();
    	Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(checkoutHeader)).getText().equals("G'day"));
    }
    
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