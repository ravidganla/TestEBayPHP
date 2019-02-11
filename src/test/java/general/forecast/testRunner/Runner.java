package general.forecast.testRunner;		

import cucumber.api.CucumberOptions; 
import org.junit.runner.RunWith;		
import cucumber.api.junit.Cucumber;		

@RunWith(Cucumber.class)				
@CucumberOptions(
		features="src\\test\\java\\general\\forecast\\features",
		glue= {"general.forecast.tests"},
		plugin= {"html:target/cucumber-html-report"})						
public class Runner 				
{		

}