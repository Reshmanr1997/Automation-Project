package Selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Programs {
	WebDriver driver;
	
	@BeforeTest
	public void setup()
	{
		driver=new ChromeDriver();
	}
	
	@BeforeMethod 
	public void urlload()
	{
		driver.get("https://www.pepperfry.com/");
		driver.manage().window().maximize();
	}
	
	@Test
	public void test1() throws Exception
	{
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		WebElement mouse=driver.findElement(By.xpath("/html/body/app-root/pf-header/header/div[2]/pf-header-main/div/div[1]/div/div[4]/pf-header-icons/div/div[1]/div[1]/div/div[1]"));
		Actions act=new Actions(driver);
		act.moveToElement(mouse).perform();
		driver.findElement(By.xpath("//*[@id=\"Button\"]/div/span")).click();
		
		File f=new File("D:\\email.xlsx");
		FileInputStream fi=new FileInputStream(f);
		XSSFWorkbook wb=new XSSFWorkbook(fi);
		XSSFSheet sh=wb.getSheet("Sheet1");
		
		for(int i=1;i<=sh.getLastRowNum();i++)
		{
			String email=sh.getRow(i).getCell(0).getStringCellValue();
			System.out.println("email="+email);
			driver.findElement(By.xpath("//*[@id=\"take-mobile-email-container\"]/form/div/div/div/div[2]/input")).clear();
			driver.findElement(By.xpath("//*[@id=\"take-mobile-email-container\"]/form/div/div/div/div[2]/input")).sendKeys(email);
			driver.findElement(By.xpath("//*[@id=\"take-mobile-email-container\"]/form/pf-button")).click();    
		}
		driver.findElement(By.xpath("//*[@id=\"desktop-header-login\"]/div/div[1]/div/a")).click();
		driver.findElement(By.xpath("//*[@id=\"search\"]")).sendKeys("chairs");
		Actions action=new Actions(driver);
		action.sendKeys(Keys.RETURN).perform();
		Thread.sleep(3000);
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,450)", "");
		
		String parentWindow=driver.getWindowHandle();
		driver.findElement(By.xpath("//*[@id=\"scroller\"]/div/div[4]/div/pf-clip-product-card/div/div[1]/div[1]/a/pf-image/div/img")).click();
		Set<String> allwindowhandles = driver.getWindowHandles();
		for(String handle:allwindowhandles)
		{
			if(!handle.equalsIgnoreCase(parentWindow))
			{
				driver.switchTo().window(handle);
				driver.findElement(By.xpath("/html/body/app-root/main/app-vip/div/div[2]/div[2]/div[2]/section/div[5]/pf-vip-cta/div/div/div[2]/pf-button")).click();
				Thread.sleep(10000);
				File scr=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileHandler.copy(scr, new File("D://cart.png"));
				driver.close();
	    }
			driver.switchTo().window(parentWindow);
        }
		String actualtitle=driver.getTitle();
		System.out.println(actualtitle);
		
		String exp="Pepperfry";
		if(actualtitle.equals(exp))
		{
			System.out.println("pass");
		}
		else
		{
			System.out.println("fail");
		} 
		
		List<WebElement> li = driver.findElements(By.tagName("a"));
		for(WebElement s:li)
		{
			String link=s.getAttribute("href");
			verify(link);
		}
	}

	private void verify(String link) {
		try
		{
			URL ob=new URL(link);
			HttpURLConnection con=(HttpURLConnection)ob.openConnection();
			con.connect();
			if(con.getResponseCode()==200)
			{
				System.out.println("valid-----"+link);
			}
			else if(con.getResponseCode()==404)
			{
				System.out.println("broken link------"+link);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	
		
	}
	
		}
	

