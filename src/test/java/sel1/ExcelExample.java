package sel1;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;



public class ExcelExample {

	WebDriver driver;
	WebDriverWait wait;
	
	String appURL ="https://www.linkedin.com/";
	private By bySignInLink = By.linkText("Sign in");
	private By byEmail = By.name("session_key");
	private By byPassword=By.name("session_password");
	private By bySignIn=By.xpath("//button[@type='submit']");
	private By byError=By.id("error-for-username");
	
	@BeforeClass
	public void testSetup() {
		System.setProperty("Webdriver.gecko.driver","./src/main/resources/drivers/geckodrver.exe");
		driver=new FirefoxDriver();
		driver.manage().window().maximize();
		wait= new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	@Test(dataProvider ="inputData")
	public void verifyInvalidLogin(String username, String password) {
		driver.get(appURL);
		driver.findElement(bySignInLink).click();
		driver.findElement(byEmail).sendKeys(username);
		driver.findElement(byPassword).sendKeys(password);
		driver.findElement(bySignIn).click();
		
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byError));
		String expectedErrorMessage="Please enter a valid username";
		String actualErrorMessage=driver.findElement(byError).getText();
		Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
	}
	
	@DataProvider(name="inputData")
	public Object[][] getCellData() throws IOException {
		
		//locating the excel file
		FileInputStream file = new FileInputStream("./src/test/resources/sampledoc.xlsx");
		// create the workbook instance
		XSSFWorkbook wb = new XSSFWorkbook(file);
		//go to desired sheet
		XSSFSheet s= wb.getSheet("Sheet1");
		
		int rowcount = s.getLastRowNum()+1;
		int cellcount=s.getRow(0).getLastCellNum();
		
		Object data[][] = new Object[rowcount][cellcount];
		
		for(int i=1;i<rowcount;i++) {
			Row r = s.getRow(i);
			for(int j=0;j<cellcount;j++) {
				Cell c = r.getCell(j);
				data[i][j]=c.getStringCellValue();
			}
		}
		wb.close();
		return data;
	}
	
}
