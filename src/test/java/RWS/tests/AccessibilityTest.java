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
	public  void Accessibility(String url,String linkName) throws IOException, InterruptedException {
		 landingPage=openLink();
		landingPage.openURL(url);
		landingPage.getAllMissingAltAttributes(".\\Reports\\"+linkName+"getAllMissingAltAttributes.txt");
		landingPage.inputWithoutPlaceholder(".\\Reports\\"+linkName+"inputWithoutPlaceholder.txt");
		landingPage.getFramesWithoutTitle(".\\Reports\\"+linkName+"getFramesWithoutTitle.txt");
		landingPage.getAllButtonsWithoutDescriptiveValue(".\\Reports\\"+linkName+"getAllButtonsWithoutDescriptiveValue.txt");
		
		landingPage.checkKeyboardFunctionality();
		
		
	}
	
	
	
	
	
	
	@DataProvider
	private Object[][] getData() {
		
		return new Object[][] {{"https://www.heart.org/","HomePage"},{"https://www.heart.org/en/about-us/heart-attack-and-stroke-symptoms","heart-attack-and-stroke-symptoms"}};

	}



}
