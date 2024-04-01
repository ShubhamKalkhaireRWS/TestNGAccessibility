package RWS.tests;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import RWS.PageObjects.LandingPage;
import RWS.TestComponents.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AccessibilityTest extends BaseTest {
	LandingPage landingPage;
	@Test(dataProvider = "getData")
	public  void Accessibility(String url) throws IOException, InterruptedException {
		 landingPage=openLink();
		landingPage.openURL(url);
		landingPage.getAllMissingAltAttributes(".\\Reports\\ResultFiles\\getAllMissingAltAttributes.txt");
		landingPage.inputWithoutPlaceholder(".\\Reports\\ResultFiles\\inputWithoutPlaceholder.txt");
		landingPage.getFramesWithoutTitle(".\\Reports\\ResultFiles\\getFramesWithoutTitle.txt");
		landingPage.getAllButtonsWithoutDescriptiveValue(".\\Reports\\ResultFiles\\getAllButtonsWithoutDescriptiveValue.txt");
		
		landingPage.checkKeyboardFunctionality();
		
		
	}
	
	
	
	
	
	
	@DataProvider
	private Object[][] getData() {
		
		return new Object[][] {{"https://www.heart.org/"},{"https://www.heart.org/en/about-us/heart-attack-and-stroke-symptoms"}};

	}



}
