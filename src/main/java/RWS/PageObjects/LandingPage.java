package RWS.PageObjects;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class LandingPage {

	
	WebDriver driver;
	String currentHandle;
	XSSFWorkbook workbook = new XSSFWorkbook();
	// Create a new sheet in the workbook
	XSSFSheet sheet;

	
	
     
	 
	int rowNum;

	public LandingPage(WebDriver driver) {
		this.driver = driver;
	}

	public void openURL(String URL) throws InterruptedException {
		currentHandle = driver.getWindowHandle();
		driver.findElement(By.cssSelector("#txtUrls")).sendKeys(URL);
		driver.findElement(By.cssSelector("#openUrls")).click();
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(50));
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

	public void noAriaLabel(String filePath, 
			String url,int b) throws IOException {
		// Initialize a new Excel workbook
		PrintWriter writer = new PrintWriter(new FileWriter(filePath));
		sheet= workbook.createSheet("Accessibility"+b);
		// Find all buttons on the page
		List<WebElement> buttons = driver.findElements(By.tagName("button"));
		buttons.addAll(driver.findElements(By.tagName("a")));
	
		
		
		// Initialize a row index counter
		XSSFRow headingRow1 = sheet.createRow(rowNum++);
		XSSFRow headingRow = sheet.createRow(rowNum++);
		// Create a cell for the heading
		XSSFCell headingCell = headingRow.createCell(0);
		XSSFCell headingCell1 = headingRow1.createCell(1);
		// Set the heading text
		headingCell.setCellValue("Buttons Without Aria Label");
	
		headingCell1.setCellValue(url);
		headingRow1.createCell(0).setCellValue("Link");

		// Create a font with bold style for the heading
		XSSFFont boldFont = workbook.createFont();
		boldFont.setBold(true);
		// Apply the bold font to the heading cell
		XSSFCellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);
		headingCell.setCellStyle(boldCellStyle);

		// Set default font for other cells
		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setBold(false);
		XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
		defaultCellStyle.setFont(defaultFont);

		// Iterate over the buttons
		for (WebElement button : buttons) {
			String ariaLabel = button.getAttribute("aria-label");

			// If button has no aria-label attribute
			if (ariaLabel == null || ariaLabel.isEmpty()) {

				// Get the text of the button
				String buttonText = button.getText();

				// Create a new row in the Excel sheet
				if (!buttonText.isBlank()) {
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue(buttonText);

					// Print to console
					System.out.println("Button has no aria label : " + buttonText);
				}
			}
		}

////		 Write the workbook content to a file
//	    try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//	        workbook.write(fileOut);
//	    }

	}

	public void getAllMissingAltAttributes(String filePath) throws FileNotFoundException, IOException {
		
		rowNum++;
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			rowNum++;
			// Find all img elements on the page
			XSSFRow headingRow = sheet.createRow(rowNum++);
			// Create a cell for the heading
			XSSFCell headingCell = headingRow.createCell(0);
			// Set the heading text
			headingCell.setCellValue("Buttons With Alt attribute Missing");

			// Create a font with bold style for the heading
			XSSFFont boldFont = workbook.createFont();
			boldFont.setBold(true);
			// Apply the bold font to the heading cell
			XSSFCellStyle boldCellStyle = workbook.createCellStyle();
			boldCellStyle.setFont(boldFont);
			headingCell.setCellStyle(boldCellStyle);

			// Set default font for other cells
			XSSFFont defaultFont = workbook.createFont();
			defaultFont.setBold(false);
			XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
			defaultCellStyle.setFont(defaultFont);
			List<WebElement> imgElements = driver.findElements(By.tagName("img"));

			// Iterate through img elements and capture alt attribute values
			for (WebElement imgElement : imgElements) {
				String altAttributeValue = imgElement.getAttribute("alt");
				if (altAttributeValue == null || altAttributeValue.trim().isEmpty()) {
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue(((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", imgElement)).replaceAll("<", "/"));
					writer.println("Img Element with Missing Alt: " + ((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", imgElement)).replaceAll("<", "/"));

					writer.println("----------------------------------------------");
				}
			}

			// Find all map elements on the page
			List<WebElement> mapElements = driver.findElements(By.tagName("map"));

			// Iterate through map elements and capture alt attribute values
			for (WebElement mapElement : mapElements) {
				String altAttributeValue = mapElement.getAttribute("alt");
				if (altAttributeValue == null || altAttributeValue.trim().isEmpty()) {
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", mapElement));
					writer.println("Map Element with Missing Alt : " + (String) ((JavascriptExecutor) driver)
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
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", logoElement));
					writer.println("Logo Element with Missing Alt: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", logoElement));
					writer.println("----------------------------------------------");
				}
			}

			System.out.println("Elements with empty alt attribute values saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void inputWithoutPlaceholder(String filePath) throws FileNotFoundException, IOException {
		rowNum++;
		XSSFRow headingRow = sheet.createRow(rowNum++);
		// Create a cell for the heading
		XSSFCell headingCell = headingRow.createCell(0);
		// Set the heading text
		headingCell.setCellValue("Input With Placeholder Missing");

		// Create a font with bold style for the heading
		XSSFFont boldFont = workbook.createFont();
		boldFont.setBold(true);
		// Apply the bold font to the heading cell
		XSSFCellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);
		headingCell.setCellStyle(boldCellStyle);

		// Set default font for other cells
		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setBold(false);
		XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
		defaultCellStyle.setFont(defaultFont);
		// Find all input elements on the page
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			List<WebElement> inputElements = driver.findElements(By.tagName("input"));

			// Iterate through input elements
			for (WebElement inputElement : inputElements) {
				// Check if the input element is displayed and does not have a placeholder
				// attribute
				if (inputElement.isDisplayed() && inputElement.getAttribute("placeholder").isEmpty()) {
					// Print the entire input element without placeholder attribute
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", inputElement));
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

	public void getFramesWithoutTitle(String filePath) throws FileNotFoundException, IOException {
		rowNum++;
		XSSFRow headingRow = sheet.createRow(rowNum++);
		// Create a cell for the heading
		XSSFCell headingCell = headingRow.createCell(0);
		// Set the heading text
		headingCell.setCellValue("iFrames with Title Missing");

		// Create a font with bold style for the heading
		XSSFFont boldFont = workbook.createFont();
		boldFont.setBold(true);
		// Apply the bold font to the heading cell
		XSSFCellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);
		headingCell.setCellStyle(boldCellStyle);

		// Set default font for other cells
		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setBold(false);
		XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
		defaultCellStyle.setFont(defaultFont);
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			// Find all iframe elements on the page
			List<WebElement> iframeElements = driver.findElements(By.tagName("iframe"));

			// Iterate through iframe elements and capture title attribute values
			for (WebElement iframeElement : iframeElements) {
				String titleAttributeValue = iframeElement.getAttribute("title");
				if (titleAttributeValue == null || titleAttributeValue.trim().isEmpty()) {
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", iframeElement));
					writer.println("Iframe Element without title: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", iframeElement));
					writer.println("----------------------------------------------");
				}
			}

			System.out.println("Iframe elements without title attribute values saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getAllButtonsWithoutDescriptiveValue(String filePath,int a) throws FileNotFoundException, IOException {
		rowNum++;

		XSSFRow headingRow = sheet.createRow(rowNum++);
		// Create a cell for the heading
		XSSFCell headingCell = headingRow.createCell(0);
		// Set the heading text
		headingCell.setCellValue("Button without Descriptive Value");

		// Create a font with bold style for the heading
		XSSFFont boldFont = workbook.createFont();
		boldFont.setBold(true);
		// Apply the bold font to the heading cell
		XSSFCellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);
		headingCell.setCellStyle(boldCellStyle);

		// Set default font for other cells
		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setBold(false);
		XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
		defaultCellStyle.setFont(defaultFont);
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			// Find all button elements on the page
			List<WebElement> buttonElements = driver.findElements(By.xpath("//button[@type='button']"));

			for (WebElement buttonElement : buttonElements) {

				// Retrieve text using JavaScript
				String buttonText = (String) ((JavascriptExecutor) driver)
						.executeScript("return arguments[0].textContent;", buttonElement);

				// Check if buttonText is not empty before printing
				if (buttonText.isEmpty()) {
					XSSFRow row = sheet.createRow(rowNum++);
					// Create a new cell in the row and set the button text as its value
					row.createCell(3).setCellValue((String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", buttonElement));
					writer.println("Button Element: " + (String) ((JavascriptExecutor) driver)
							.executeScript("return arguments[0].outerHTML;", buttonElement));
					writer.println("----------------------------------------------");
				}
			}

			System.out.println("Visible text values for non-empty button elements saved to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
		}
	}

	public void checkKeyboardFunctionality(String filePath) throws InterruptedException, IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(filePath));
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
							if (!windowHandle.equals(currentHandle)) {
								if (!windowHandle.equals(driver.getWindowHandle())) {
									driver.switchTo().window(windowHandle);
									break;
								}
							}
						}
					}
				}

				if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
					writer.println("Page Functionality Using Keyboard is Working Correctly");
					Assert.assertTrue(true);
				} else {
					writer.println("Page Functionality Using Keyboard is Working Correctly");
					Assert.assertEquals(driver.getCurrentUrl(), linkOfFocusedElement);

				}

			}
		}
		writer.close();
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

	public void characterKey(String filePath) throws InterruptedException {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(filePath));
			// Locate a text field on the page
			WebElement searchBtn = driver.findElement(By.xpath("//img[@alt='site search']"));
			WebElement textField = driver.findElement(By.xpath("//input[@placeholder='ex: Heart Attack Symptoms']"));
			// Create Actions class instance
			Actions actions = new Actions(driver);
			searchBtn.click();
			Thread.sleep(1000);
			textField.click();
			// Special characters with shifted keys
			String specialCharacters = "1234567890-=[];',./";

			// Type each special character
			for (char ch : specialCharacters.toCharArray()) {
				actions.keyDown(Keys.SHIFT).sendKeys(String.valueOf(ch)).keyUp(Keys.SHIFT);
				Thread.sleep(100);
			}

			// Perform the actions on the text field
			actions.build().perform();
			String actual = driver.findElement(By.xpath("//input[@placeholder='ex: Heart Attack Symptoms']"))
					.getAttribute("value");
			driver.findElement(By.xpath("(//button[@aria-label='Close'][normalize-space()='×'])[1]")).click();
			String expected = "!@#$%^&*()_+{}:\"<>?";
			Thread.sleep(5000);
			System.out.println(expected.equals(actual));
			if (expected.equals(actual)) {
				writer.println("Character Key Functionality Working Correctly");
				writer.close();
			}
			Assert.assertEquals(actual, expected);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentMethodName() {
		// Get the stack trace
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

		// The current method name is at index 2 in the stack trace array
		String methodName = stackTraceElements[2].getMethodName();

		return methodName;
	}

	public static void mergeTextFiles(String folderPath, String outputFilePath) {
		try {
			File folder = new File(folderPath);
			File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

			if (files != null && files.length > 0) {
				FileWriter writer = new FileWriter(outputFilePath);

				for (File file : files) {
					Path path = Paths.get(file.getAbsolutePath());
					try (Stream<String> lines = Files.lines(path)) {
						lines.forEach(line -> {
							try {
								writer.write(line);
								writer.write(System.lineSeparator());
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					}
				}

				writer.close();
				System.out.println("Text files merged successfully.");
			} else {
				System.out.println("No text files found in the specified folder.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkKeyboardFunctionality1(String filePath) throws InterruptedException, IOException {
		try {
			Actions actions = new Actions(driver);

			// Move mouse to the right edge of the webpage
//		        actions.moveToElement(driver.findElement(By.cssSelector("input[title='Submit Search']"))).build().perform();
			PrintWriter writer = new PrintWriter(new FileWriter(filePath));
			String baseURL = driver.getCurrentUrl();
			List<WebElement> hrefs = driver.findElements(By.tagName("a"));

			int min = 1;
			int max = hrefs.size(); // Change this value to your desired maximum limit

			// Create an instance of Random class
			Random random = new Random();

			// Generate a random number within the specified range
			int randomNumber = random.nextInt(max - min + 1) + min;
			// Initialize Actions class
			List<String> autoCompleteElementsProcessed = new ArrayList<>();
			List<String> tabbedButton = new ArrayList<>();
			// Simulate pressing the tab button multiple times
			for (int i = 0; i < max; i++) {
				actions.sendKeys("\t").perform();
//				Thread.sleep(1000);
				WebElement focusedElement = driver.switchTo().activeElement();

				if (!(focusedElement.getAttribute("type") == null)) {
					if (focusedElement.getAttribute("type").equalsIgnoreCase("radio")) {
						focusedElement.sendKeys(Keys.DOWN);

					}

					if (focusedElement.getTagName().equalsIgnoreCase("Select")) {
						Thread.sleep(1000);
						Select sel = new Select(driver.findElement(By.name(focusedElement.getAttribute("name"))));
						int options = sel.getOptions().size() - 1;
						int randomOption = random.nextInt(options - min + 1) + min;

						for (int j = 0; j < randomOption; j++) {
							focusedElement.sendKeys(Keys.DOWN);
						}
						System.out.println(sel.getFirstSelectedOption().getText());
						System.out.println(sel.getOptions().get(randomOption).getText());
						Assert.assertEquals(sel.getFirstSelectedOption().getText(),
								sel.getOptions().get(randomOption).getText());

					}

					if (focusedElement.getAttribute("type").equalsIgnoreCase("checkbox")) {
						focusedElement.sendKeys(Keys.SPACE);

						Assert.assertTrue(focusedElement.isSelected());
					}

					if (focusedElement.getAttribute("class").contains("autocomplete")
							&& !autoCompleteElementsProcessed.contains(focusedElement.getAttribute("name"))) {
						focusedElement.sendKeys("in");
						Thread.sleep(3000);
						List<WebElement> autoSuggestOptions = driver
								.findElements(By.cssSelector(".ui-menu.ui-widget.ui-widget-content li"));
						int autoSuggestOptionsCount = autoSuggestOptions.size();

						int randomAutoSuggestOption = random.nextInt(autoSuggestOptionsCount - min + 1) + min;
						String expected = autoSuggestOptions.get(randomAutoSuggestOption - 1).getText();
						for (int k = 0; k < randomAutoSuggestOption; k++) {
							focusedElement.sendKeys(Keys.DOWN);
							Thread.sleep(200);

						}
						Thread.sleep(1000);
						String actual = focusedElement.getAttribute("value");

						System.out.println("Actual : " + focusedElement.getAttribute("value"));
						autoCompleteElementsProcessed.add(focusedElement.getAttribute("name"));
						focusedElement.sendKeys(Keys.ENTER);

						Thread.sleep(2000);
						System.out.println("Expected : " + expected);
						Assert.assertEquals(actual, expected);

					}

				}

				String linkOfFocusedElement = focusedElement.getAttribute("href");
				if (!tabbedButton.contains(linkOfFocusedElement)) {
					linkNavigation(focusedElement, baseURL, writer, linkOfFocusedElement);
					tabbedButton.add(linkOfFocusedElement);
				}

//				// Get the currently focused element
//
//				String linkOfFocusedElement = focusedElement.getAttribute("href");
//				if (!(linkOfFocusedElement == null)) {
//					if (!linkOfFocusedElement.isEmpty()) {
//						Thread.sleep(2000);
//						focusedElement.sendKeys(Keys.ENTER);
//						WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
//						wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
//
//						if (driver.getCurrentUrl().equals(baseURL)) {
//
//							Thread.sleep(3000);
//							if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
//								Assert.assertTrue(true);
//							} else {
//								wait.until(ExpectedConditions.numberOfWindowsToBe(3));
//								// Switch to the new tab
//								Set<String> windowHandles = driver.getWindowHandles();
//								for (String windowHandle : windowHandles) {
//									if (!windowHandle.equals(currentHandle)) {
//										if (!windowHandle.equals(driver.getWindowHandle())) {
//											driver.switchTo().window(windowHandle);
//											break;
//										}
//									}
//								}
//							}
//						}
//						
//
//						if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
//							writer.println("Page Functionality Using Keyboard is Working Correctly");
//							Assert.assertTrue(true);
//						} else {
//							writer.println("Page Functionality Using Keyboard is Working Correctly");
//							Assert.assertEquals(driver.getCurrentUrl(), linkOfFocusedElement);
//
//						}
//
//					}
//				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void linkNavigation(WebElement focusedElement, String baseURL, PrintWriter writer,
			String linkOfFocusedElement) throws InterruptedException {
		if (focusedElement.getTagName().equals("a")
				&& !focusedElement.getText().equalsIgnoreCase("Skip to main content")) {
			System.out.println(focusedElement.getText());

			focusedElement.sendKeys(Keys.ENTER);
			WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
			String currentURL = null;
			Thread.sleep(2000);

			if (!(driver.getCurrentUrl().equals(baseURL))
					|| driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {

				if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
					driver.switchTo().frame("__checkout2");
					Thread.sleep(3000);
					driver.findElement(By.xpath("//button[@data-tracking-element-name='closeButton']")).click();
					driver.switchTo().defaultContent();

				}
				JavascriptExecutor js = (JavascriptExecutor) driver;
				boolean isDocumentReady = (boolean) js.executeScript("return document.readyState === 'complete';");

				if (isDocumentReady) {
					currentURL = driver.getCurrentUrl();
				}

				driver.navigate().back();

			} else {
				if (driver.getCurrentUrl().equals(baseURL)) {

					Thread.sleep(3000);
					if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")) {
						Assert.assertTrue(true);
					} else {
						wait.until(ExpectedConditions.numberOfWindowsToBe(3));
						// Switch to the new tab
						Set<String> windowHandles = driver.getWindowHandles();
						for (String windowHandle : windowHandles) {
							if (!windowHandle.equals(currentHandle)) {
								String currentHandle1 = driver.getWindowHandle();
								if (!windowHandle.equals(currentHandle1)) {
									driver.switchTo().window(windowHandle);
									currentURL = driver.getCurrentUrl();
									driver.close();
									driver.switchTo().window(currentHandle1);
									break;
								}
							}
						}
					}
				}
			}

			if (driver.getCurrentUrl().contains("https://www.heart.org/?form=")
					|| linkOfFocusedElement.contains("https://mygiving.heart.org/")) {
				writer.println("Page Functionality Using Keyboard is Working Correctly");
				Assert.assertTrue(true);
			} else {
				writer.println("Page Functionality Using Keyboard is Working Correctly");
				System.out.println(currentURL);
				System.out.println(linkOfFocusedElement);
				Assert.assertEquals(currentURL, linkOfFocusedElement);

			}
		}

	}
	
//	 public static void mergeExcelFilesFromFolder(String folderPath, String outputPath) throws IOException, InvalidFormatException {
//	        XSSFWorkbook mergedWorkbook = new XSSFWorkbook();
//
//	        // Get all XLSX files from the specified folder
//	        List<String> filePaths = getAllXLSXFilesInFolder(folderPath);
//
//	        for (String filePath : filePaths) {
//	            try (FileInputStream inputStream = new FileInputStream(filePath)) {
//	                Workbook workbook = WorkbookFactory.create(inputStream);
//	                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//	                    Sheet sheet = workbook.getSheetAt(i);
//	                    copySheet(sheet, mergedWorkbook);
//	                }
//	            }
//	        }
//
//	        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
//	            mergedWorkbook.write(outputStream);
//	        }
//	    }
//
//	    private static List<String> getAllXLSXFilesInFolder(String folderPath) throws IOException {
//	        List<String> filePaths = new ArrayList<>();
//	        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath), "*.xlsx")) {
//	            for (Path path : directoryStream) {
//	                filePaths.add(path.toString());
//	            }
//	        }
//	        return filePaths;
//	    }

//	    private static void copySheet(Sheet sourceSheet, Workbook targetWorkbook) {
//	        Sheet targetSheet = targetWorkbook.createSheet(sourceSheet.getSheetName());
//
//	        for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
//	            Row sourceRow = sourceSheet.getRow(i);
//	            Row targetRow = targetSheet.createRow(i);
//
//	            if (sourceRow != null) {
//	                for (int j = sourceRow.getFirstCellNum(); j < sourceRow.getLastCellNum(); j++) {
//	                    Cell sourceCell = sourceRow.getCell(j);
//	                    Cell targetCell = targetRow.createCell(j);
//
//	                    if (sourceCell != null) {
//	                       
//	                        targetCell.setCellType(sourceCell.getCellType());
//
//	                        switch (sourceCell.getCellType()) {
//	                            case Cell.CELL_TYPE_NUMERIC:
//	                                targetCell.setCellValue(sourceCell.getNumericCellValue());
//	                                break;
//	                            case Cell.CELL_TYPE_STRING:
//	                                targetCell.setCellValue(sourceCell.getStringCellValue());
//	                                break;
//	                            case Cell.CELL_TYPE_BOOLEAN:
//	                                targetCell.setCellValue(sourceCell.getBooleanCellValue());
//	                                break;
//	                            case Cell.CELL_TYPE_FORMULA:
//	                                targetCell.setCellFormula(sourceCell.getCellFormula());
//	                                break;
//	                            default:
//	                                // Handle other cell types as needed
//	                        }
//	                    }
//	                }
//	            }
//	        }
//	    }
	    
	 public static void mergeExcelFilesFromFolder(String folderPath, String outputPath) throws IOException, InvalidFormatException {
	        XSSFWorkbook mergedWorkbook = new XSSFWorkbook();
	        XSSFSheet targetSheet = mergedWorkbook.createSheet("Accessibility Result");

	        // Get all XLSX files from the specified folder
	        List<String> filePaths = getAllXLSXFilesInFolder(folderPath);

	        for (String filePath : filePaths) {
	            try (FileInputStream inputStream = new FileInputStream(filePath)) {
	                Workbook workbook = WorkbookFactory.create(inputStream);
	                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	                    Sheet sourceSheet = workbook.getSheetAt(i);
	                    copySheet(sourceSheet, targetSheet);
	                    addColoredRow(targetSheet);
	                }
	            }
	        }

	        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
	            mergedWorkbook.write(outputStream);
	        }
	    }

	    private static List<String> getAllXLSXFilesInFolder(String folderPath) throws IOException {
	        List<String> filePaths = new ArrayList<>();
	        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath), "*.xlsx")) {
	            for (Path path : directoryStream) {
	                filePaths.add(path.toString());
	            }
	        }
	        return filePaths;
	    }

	    private static void copySheet(Sheet sourceSheet, Sheet targetSheet) {
	        int rowCount = targetSheet.getLastRowNum() + 1; // Get the current row count in the target sheet

	        for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
	            Row sourceRow = sourceSheet.getRow(i);
	           
	            Row targetRow = targetSheet.createRow(rowCount++); // Create a new row in the target sheet
	         
	            if (sourceRow != null) {
	                for (int j = sourceRow.getFirstCellNum(); j < sourceRow.getLastCellNum(); j++) {
	                    Cell sourceCell = sourceRow.getCell(j);
	                    Cell targetCell = targetRow.createCell(j);

	                    if (sourceCell != null) {
	                        // Copy cell value and type directly
	                       
	                        switch (sourceCell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                targetCell.setCellValue(sourceCell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                targetCell.setCellValue(sourceCell.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                targetCell.setCellFormula(sourceCell.getCellFormula());
                                break;
                            default:
                                // Handle other cell types as needed
                        }
	                    }
	                }
	            }
	        }
	    }
	    private static void addColoredRow(Sheet sheet) {
	        int lastRowNum = sheet.getLastRowNum();
	        Row coloredRow = sheet.createRow(lastRowNum + 4);
	        CellStyle style = sheet.getWorkbook().createCellStyle();
	        style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
	        style.setFillPattern((short) FillPatternType.SOLID_FOREGROUND.ordinal()); // Convert FillPatternType to short
	        
	        Row lastRow = sheet.getRow(lastRowNum);
	        int lastCellNum = lastRow != null ? lastRow.getLastCellNum() : 0;

	        for (int i = 0; i < 100; i++) {
	            Cell cell = coloredRow.createCell(i);
	            cell.setCellStyle(style);
	        }
	    }
	  
	    
	    public void verifyHyperlinks(String filePath,String url,int b) throws IOException {
	    	rowNum++;
//	    	PrintWriter writer = new PrintWriter(new FileWriter(filePath));
//			sheet= workbook.createSheet("Accessibility"+b);
	        // Find all hyperlink elements
	        List<WebElement> links = driver.findElements(By.tagName("a"));
	        System.out.println(links.size());
	        // Map to store link text and corresponding URL
	        Map<String, String> linkMap = new HashMap<>();
	        
	        
	    	// Initialize a row index counter
			XSSFRow headingRow1 = sheet.createRow(rowNum++);
			XSSFRow headingRow = sheet.createRow(rowNum++);
			// Create a cell for the heading
			XSSFCell headingCell = headingRow.createCell(0);
			XSSFCell headingCell1 = headingRow1.createCell(1);
			// Set the heading text
			headingCell.setCellValue("Broken Links");
	
			// Create a font with bold style for the heading
			XSSFFont boldFont = workbook.createFont();
			boldFont.setBold(true);
			// Apply the bold font to the heading cell
			XSSFCellStyle boldCellStyle = workbook.createCellStyle();
			boldCellStyle.setFont(boldFont);
			headingCell.setCellStyle(boldCellStyle);

			// Set default font for other cells
			XSSFFont defaultFont = workbook.createFont();
			defaultFont.setBold(false);
			XSSFCellStyle defaultCellStyle = workbook.createCellStyle();
			defaultCellStyle.setFont(defaultFont);

	        // Loop through all hyperlinks
	        for (WebElement link : links) {
	            String text = link.getText().trim();
	            String url1 = link.getAttribute("href");
	           
	            // Check if the link is working
	            if (url1 != null && !url1.isEmpty() && !url1.contains("mailto:")) {
	                try {
	                    HttpURLConnection connection = (HttpURLConnection) new URL(url1).openConnection();
	                    connection.setRequestMethod("HEAD");
	                    connection.connect();
	                    int responseCode = connection.getResponseCode();
//	                    System.out.println(url1);
//	                    System.out.println(responseCode);
	                    if(responseCode >400) {
	                    	XSSFRow row = sheet.createRow(rowNum++);
	                    	row.createCell(2).setCellValue("Response ");
	                    	row.createCell(3).setCellValue(responseCode);
	                    	row.createCell(4).setCellValue(url1);
	                    	System.out.println("Broken link: " + url1 + " with response code: " + responseCode);
	                    }
//	                    Assert.assertTrue(responseCode < 400, "Broken link: " + url + " with response code: " + responseCode);
	                } catch (IOException e) {
	                    System.out.println("Exception while checking link: " + url1);
	                }
	            } 
	            
	            
	           
	            if(!(text.isEmpty())) {
	            // If the text is already in the map, check if the URLs match
	            System.out.println(text);
	            if (linkMap.containsKey(text)) {
	           
	            	XSSFRow row = sheet.createRow(rowNum++);
	            	row.createCell(2).setCellValue("Same Text with Different Links");
	            	row.createCell(3).setCellValue(text);
                	row.createCell(4).setCellValue(linkMap.get(text));
                	row.createCell(5).setCellValue(url1);
	            	System.out.println("Difference in Link with same text "+text +"   "+linkMap.get(text)+"     "+url1);
//	                Assert.assertEquals(linkMap.get(text), url, "Mismatch found for link text: " + text);
	            } else {
	                // Otherwise, add the text and URL to the map
	                linkMap.put(text, url1);
	            }
	        }
	        }
	        
	    
////	    	 Write the workbook content to a file
//			    try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//			    	System.out.println("*****************");
//			        workbook.write(fileOut);
//			    }
	        // Ensure that different texts have different URLs
//	        for (Map.Entry<String, String> entry1 : linkMap.entrySet()) {
//	            for (Map.Entry<String, String> entry2 : linkMap.entrySet()) {
//	                if (!entry1.getKey().equals(entry2.getKey())) {
//	                	if(entry1.getValue().equals(entry2.getValue())) {
//	                	
//	                	XSSFRow row = sheet.createRow(rowNum++);
//	                	row.createCell(2).setCellValue("Different Text for Same Link");
//                    	row.createCell(3).setCellValue(entry1.getValue());
//                    	row.createCell(4).setCellValue(entry2.getValue());
//                    	row.createCell(5).setCellValue(entry1.getKey());
//                    	row.createCell(6).setCellValue(entry2.getKey());
////	                    Assert.assertNotEquals(entry1.getValue(), entry2.getValue(), 
////	                            "Different texts found with the same URL: " + entry1.getKey() + " and " + entry2.getKey());
//	                	}
//	                }
//	            }
//	        }
	        
	        // Ensure that different texts have different URLs
	        List<Map.Entry<String, String>> entries = new ArrayList<>(linkMap.entrySet());
	        for (int i = 0; i < entries.size(); i++) {
	            for (int j = i + 1; j < entries.size(); j++) {
	                Map.Entry<String, String> entry1 = entries.get(i);
	                Map.Entry<String, String> entry2 = entries.get(j);
	                if (entry1.getValue().equals(entry2.getValue())) {
	                    XSSFRow row = sheet.createRow(rowNum++);
	                 
	                    row.createCell(2).setCellValue("Different Text for Same Link");
	                    row.createCell(3).setCellValue(entry1.getValue());
	                    row.createCell(4).setCellValue(entry2.getValue());
	                    row.createCell(5).setCellValue(entry1.getKey());
	                    row.createCell(6).setCellValue(entry2.getKey());
	                    System.out.println("Different texts found with the same URL: " + entry1.getKey() + " and " + entry2.getKey() + " " + entry1.getValue());
	                }
	            }
	        }

	    }
	    

}
