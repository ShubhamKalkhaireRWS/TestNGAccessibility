package RWS.PageObjects;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage {
	WebDriver driver;
	String currentHandle;
	public LandingPage(WebDriver driver) {
		this.driver = driver;
	}

	public void openURL(String URL) throws InterruptedException {
		currentHandle = driver.getWindowHandle();
		driver.findElement(By.cssSelector("#txtUrls")).sendKeys(URL);
		driver.findElement(By.cssSelector("#openUrls")).click();
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		// Switch to the new tab
		Set<String> windowHandles = driver.getWindowHandles();
		for (String windowHandle : windowHandles) {
			if (!windowHandle.equals(driver.getWindowHandle())) {
				driver.switchTo().window(windowHandle);
			}
		}
	
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
		// Close the previous tab

		Thread.sleep(2000);
		if (URL.contentEquals("https://www.heart.org/")) {
			if (driver.findElement(By.xpath("(//button[@class='close c-modal--promotion__close btn btn-dark'])[1]"))
					.isDisplayed()) {
				driver.findElement(By.xpath("(//button[@class='close c-modal--promotion__close btn btn-dark'])[1]"))
						.click();
				driver.findElement(By.cssSelector(
						".onetrust-close-btn-handler.onetrust-close-btn-ui.banner-close-button.ot-close-icon")).click();
				Thread.sleep(3000);
				Assert.assertTrue(driver.getCurrentUrl().equals(URL));
			}

		} else {
			driver.findElement(By
					.cssSelector(".onetrust-close-btn-handler.onetrust-close-btn-ui.banner-close-button.ot-close-icon"))
					.click();
			Assert.assertTrue(driver.getCurrentUrl().equals(URL));
		}
		
	}

	public void goTo(String baseUrl) throws InterruptedException {
		driver.get(baseUrl);
		Thread.sleep(2000);
		if (baseUrl.contentEquals("https://www.heart.org/")) {
			if (driver.findElement(By.xpath("(//button[@class='close c-modal--promotion__close btn btn-dark'])[1]"))
					.isDisplayed()) {
				driver.findElement(By.xpath("(//button[@class='close c-modal--promotion__close btn btn-dark'])[1]"))
						.click();
				driver.findElement(By.cssSelector(
						".onetrust-close-btn-handler.onetrust-close-btn-ui.banner-close-button.ot-close-icon")).click();
				Thread.sleep(3000);
				Assert.assertTrue(driver.getCurrentUrl().equals(baseUrl));
			}
		} else {
			driver.findElement(By
					.cssSelector(".onetrust-close-btn-handler.onetrust-close-btn-ui.banner-close-button.ot-close-icon"))
					.click();
		}

		// Initialize Actions
	}

	public void getAllMissingAltAttributes(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			// Find all img elements on the page
			List<WebElement> imgElements = driver.findElements(By.tagName("img"));

			// Iterate through img elements and capture alt attribute values
			for (WebElement imgElement : imgElements) {
				String altAttributeValue = imgElement.getAttribute("alt");
				if (altAttributeValue == null || altAttributeValue.trim().isEmpty()) {
					writer.println("Img Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", imgElement));
					writer.println("----------------------------------------------");
				}
			}

			// Find all map elements on the page
			List<WebElement> mapElements = driver.findElements(By.tagName("map"));

			// Iterate through map elements and capture alt attribute values
			for (WebElement mapElement : mapElements) {
				String altAttributeValue = mapElement.getAttribute("alt");
				if (altAttributeValue == null || altAttributeValue.trim().isEmpty()) {
					writer.println("Map Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", mapElement));
					writer.println("----------------------------------------------");
				}
			}

			// Find all logo elements on the page (you may need to adjust this based on the
			// actual tag name)
			List<WebElement> logoElements = driver.findElements(By.tagName("logo"));

			// Iterate through logo elements and capture alt attribute values
			for (WebElement logoElement : logoElements) {
				String altAttributeValue = logoElement.getAttribute("alt");
				if (altAttributeValue == null || altAttributeValue.trim().isEmpty()) {
					writer.println("Logo Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", logoElement));
					writer.println("----------------------------------------------");
				}
			}

			System.out.println("Elements with empty alt attribute values saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void inputWithoutPlaceholder(String filePath) {
		// Find all input elements on the page
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			List<WebElement> inputElements = driver.findElements(By.tagName("input"));

			// Iterate through input elements
			for (WebElement inputElement : inputElements) {
				// Check if the input element is displayed and does not have a placeholder
				// attribute
				if (inputElement.isDisplayed() && inputElement.getAttribute("placeholder").isEmpty()) {
					// Print the entire input element without placeholder attribute

					writer.println("Iframe Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", inputElement));
					writer.println("----------------------------------------------");
				}

			}
			System.out.println("Input elements without title attribute values saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getFramesWithoutTitle(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			// Find all iframe elements on the page
			List<WebElement> iframeElements = driver.findElements(By.tagName("iframe"));

			// Iterate through iframe elements and capture title attribute values
			for (WebElement iframeElement : iframeElements) {
				String titleAttributeValue = iframeElement.getAttribute("title");
				if (titleAttributeValue == null || titleAttributeValue.trim().isEmpty()) {
					writer.println("Iframe Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", iframeElement));
					writer.println("----------------------------------------------");
				}
			}

			System.out.println("Iframe elements without title attribute values saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getAllButtonsWithoutDescriptiveValue(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			// Find all button elements on the page
			List<WebElement> buttonElements = driver.findElements(By.xpath("//button[@type='button']"));

			for (WebElement buttonElement : buttonElements) {

				// Retrieve text using JavaScript
				String buttonText = (String) ((JavascriptExecutor) driver)
						.executeScript("return arguments[0].textContent;", buttonElement);

				// Check if buttonText is not empty before printing
				if (buttonText.isEmpty()) {
					writer.println("Button Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", buttonElement));
					writer.println("----------------------------------------------");
				}
			}

			System.out.println("Visible text values for non-empty button elements saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkKeyboardFunctionality() throws InterruptedException {
		String baseURL = driver.getCurrentUrl();
		List<WebElement> hrefs = driver.findElements(By.tagName("a"));

		int min = 0;
		int max = hrefs.size(); // Change this value to your desired maximum limit

		// Create an instance of Random class
		Random random = new Random();

		// Generate a random number within the specified range
		int randomNumber = random.nextInt(max - min + 1) + min;
		// Initialize Actions class
		Actions actions = new Actions(driver);

		// Simulate pressing the tab button multiple times
		for (int i = 0; i < randomNumber; i++) {
			actions.sendKeys("\t").perform();

		}
		
		// Get the currently focused element
		WebElement focusedElement = driver.switchTo().activeElement();

		String linkOfFocusedElement = focusedElement.getAttribute("href");
		if (!(linkOfFocusedElement == null)) {
			if (!linkOfFocusedElement.isEmpty()) {
				Thread.sleep(2000);
				focusedElement.sendKeys(Keys.ENTER);
				WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
				wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
				
				if (driver.getCurrentUrl().equals(baseURL)) {
					
					Thread.sleep(3000);
					if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
						Assert.assertTrue(true);
					} else {
						wait.until(ExpectedConditions.numberOfWindowsToBe(3));
						// Switch to the new tab
						Set<String> windowHandles = driver.getWindowHandles();
						for (String windowHandle : windowHandles) {
							if(!windowHandle.equals(currentHandle)) {
							if (!windowHandle.equals(driver.getWindowHandle())) {
								driver.switchTo().window(windowHandle);
								break;
							}
						}
						}
					}
				}

				if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
					Assert.assertTrue(true);
				} else {
					System.out.println("Actual"+driver.getCurrentUrl());
					System.out.println("Expected"+linkOfFocusedElement);
					Assert.assertEquals(driver.getCurrentUrl(), linkOfFocusedElement);
				}

			}
		}
	}

	public void brokenLinks() throws MalformedURLException, IOException {

		List<WebElement> links = driver.findElements(By.tagName("a"));

		for (WebElement link : links) {
			String url = link.getAttribute("href");
			if (url != null && !url.isEmpty()) {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("HEAD");
				conn.connect();
				int respCode = conn.getResponseCode();
				if (respCode > 400) {
					System.out.println(
							(String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML;", link)
									+ " Code " + respCode);
				}
			} else {
				System.out.println("Empty Links "
						+ (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", link));
			}

		}
	}

	public void reflow() throws InterruptedException {
		zoomPage(driver, 4.0);
		Thread.sleep(2000);
		// Check if the webpage is horizontally scrollable
		boolean isScrollable = isHorizontallyScrollable(driver);

		// Print the result
		if (isScrollable) {
			System.out.println("The webpage is horizontally scrollable.");
		} else {
			System.out.println("The webpage is not horizontally scrollable.");
		}
	}

	public boolean isHorizontallyScrollable(WebDriver driver) {
		// Execute JavaScript to determine if the document is horizontally scrollable
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isScrollable = (boolean) js
				.executeScript("return document.body.scrollWidth > document.body.clientWidth");
		return isScrollable;
	}

	public void zoomPage(WebDriver driver, double zoomFactor) {
		// Execute JavaScript to set the browser's zoom level
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.style.zoom = '" + zoomFactor + "'");
	}
}
