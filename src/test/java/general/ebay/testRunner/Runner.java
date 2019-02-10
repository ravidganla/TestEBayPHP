package general.ebay.testRunner;		

import cucumber.api.CucumberOptions; 
import org.junit.runner.RunWith;		
import cucumber.api.junit.Cucumber;		

@RunWith(Cucumber.class)				
@CucumberOptions(
		features="src\\test\\java\\general\\ebay\\features",
		glue= {"general.ebay.tests"},
		plugin= {"html:target/cucumber-html-report"})						
public class Runner 				
{		

}