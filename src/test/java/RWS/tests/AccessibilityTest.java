package RWS.tests;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import RWS.PageObjects.LandingPage;
import RWS.TestComponents.BaseTest;

public class AccessibilityTest extends BaseTest {
	LandingPage landingPage;
	int counter=0;
	@Test(dataProvider = "getData")
	public  void Perceivable(String url ) throws IOException, InterruptedException {
		 landingPage=openLink();
		landingPage.openURL(url);
		counter++;
		landingPage.noAriaLabel(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\Accessibility_"+counter+".xlsx",url,counter);
		landingPage.verifyHyperlinks(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\Accessibility_"+counter+".xlsx",url,counter);
		landingPage.getAllMissingAltAttributes(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\Accessibility_"+counter+".xlsx");
////		landingPage.getAllMissingAltAttributes(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"getAllMissingAltAttributes.txt");
		landingPage.inputWithoutPlaceholder(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\Accessibility_"+counter+".xlsx");
		landingPage.getFramesWithoutTitle(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\Accessibility_"+counter+".xlsx");
		landingPage.getAllButtonsWithoutDescriptiveValue(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\Accessibility_"+counter+".xlsx",counter);
		
		
//		landingPage.getAllMissingAltAttributes(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"getAllMissingAltAttributes.txt");
//		landingPage.inputWithoutPlaceholder(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"inputWithoutPlaceholder.txt");
//		landingPage.getFramesWithoutTitle(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"getFramesWithoutTitle.txt");
//		landingPage.getAllButtonsWithoutDescriptiveValue(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"getAllButtonsWithoutDescriptiveValue.txt");
		
//		LandingPage.mergeTextFiles(".\\Reports\\"+LandingPage.getCurrentMethodName(), ".\\Reports\\"+LandingPage.getCurrentMethodName()+".txt");

		
		
	}
	
	
	
	
	
//	@Test(dataProvider = "getData")
//	public  void Operable(String url,String linkName) throws IOException, InterruptedException {
//		 landingPage=openLink();
//		landingPage.openURL(url);
//	
////		landingPage.characterKey(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"CharacterKeyFunctionality.txt");
//		landingPage.checkKeyboardFunctionality(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"KeyboardFunctionality.txt");
//		LandingPage.mergeTextFiles(".\\Reports\\"+LandingPage.getCurrentMethodName(), ".\\Reports\\"+LandingPage.getCurrentMethodName()+".txt");
////		landingPage.checkKeyboardFunctionality1(".\\Reports\\"+LandingPage.getCurrentMethodName()+"\\"+linkName+"KeyboardFunctionality.txt");
//		
//	}
	
	
	
	
	@DataProvider
	private Object[][] getData() {
		
		return new Object[][] {{"https://www.heart.org/"}};

	}


	@DataProvider
	private Object[][] getData1() {
		
		return new Object[][] {{"https://cpr.heart.org/en/course-catalog-search"},{"https://www.heart.org/en/about-us/heart-attack-and-stroke-symptoms"},{"https://cpr.heart.org/en/"},{"https://cpr.heart.org/en/cpr-courses-and-kits"},{"https://cpr.heart.org/en/cpr-training-supplies/aed-trainer"}};

	}

}
