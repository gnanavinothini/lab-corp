import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Test
public class LabCorpPortalTest {

    @Test
    public void JobSearch() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\selenium_standalone_binaries\\windows\\googlechrome\\32bit\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
        driver.get("https://www.labcorp.com/");

        /** Navigating to the careers(child) window using window handle**/
        String parentWindow= driver.getWindowHandle();
        WebElement careersLink=driver.findElement(By.xpath("//*[@id=\"topofpage\"]/div/footer/div[1]/div/div[1]/div/nav[1]/ul/li[3]/a"));
        careersLink.click();
        Set<String> allWindows =  driver.getWindowHandles();
        for(String curWindow : allWindows) {
            if(!curWindow.equals(parentWindow)) {
                driver.switchTo().window(curWindow);
                driver.getTitle();
                WebElement searchText = driver.findElement(By.id("search-keyword-b09c90b107"));
                searchText.sendKeys("QA Test Automation Developer");
                WebElement submitButton = driver.findElement(By.id("search-submit-b09c90b107"));
                submitButton.click();
                WebDriverWait wt = new WebDriverWait(driver,90);
                wt.until(ExpectedConditions.elementToBeClickable (By.xpath("//*[@id=\"search-results-list\"]/ul/li[1]/a/span[1]")));
                WebElement jobResult = driver.findElement(By.xpath("//*[@id=\"search-results-list\"]/ul/li[1]/a/span[1]"));
                jobResult.click();

                /** Read the JobTitle, JobLocation, JobID from previous page before clicking on Apply button **/
                HashMap<String,String> expectedMap = new HashMap<String, String>();
                wt.until(ExpectedConditions.presenceOfElementLocated (By.xpath("//*[@id=\"content\"]/div[3]/section[2]/h1")));
                WebElement jobTitle = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/section[2]/h1"));
                String expectedJobTitle = jobTitle.getText();
                WebElement jobLocation = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/section[2]/div[1]/span[1]"));
                String[] expectedJobLocation = jobLocation.getText().replaceAll("Location","").trim().split(",");
                String expectedJobCity = expectedJobLocation[0];
                String expectedJobState = expectedJobLocation[1];
                WebElement jobID = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/section[2]/div[1]/span[2]"));
                String expectedJobID = jobID.getText().replaceAll("Job ID","").trim();
                expectedMap.put("JobTitle",expectedJobTitle);
                expectedMap.put("JobCity", expectedJobCity);
                expectedMap.put("JobID", expectedJobID);
                WebElement applyNowButton = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/section[2]/a"));
                applyNowButton.click();
               // Thread.sleep(10000);

                /** Read the JobTitle, JobLocation, JobID from next page after clicking on Apply button **/
                HashMap<String,String> actualMap = new HashMap<String, String>();
                wt.until(ExpectedConditions.presenceOfElementLocated (By.xpath("//*[@id=\"ae-main-content\"]/div/div/div[2]/div/div/div[1]/span[1]")));
                WebElement nextPageJobTitle = driver.findElement(By.xpath("//*[@id=\"ae-main-content\"]/div/div/div[2]/div/div/div[1]/span[1]"));
                String actualJobTitle = nextPageJobTitle.getText();
                WebElement nextPageJobLocation = driver.findElement(By.xpath("//*[@id=\"ae-main-content\"]/div/div/div[3]/div/div[1]/div/div[1]/span/span"));
                String[] actualJobLocation = nextPageJobLocation.getText().split(",");
                String actualJobCity = actualJobLocation[0];
                String actualJobState = actualJobLocation[1];
                WebElement nextPageJobID = driver.findElement(By.xpath("//*[@id=\"ae-main-content\"]/div/div/div[2]/div/div/div[1]/span[2]"));
                String actualJobID = nextPageJobID.getText().replaceAll("#","");
                actualMap.put("JobTitle",actualJobTitle);
                actualMap.put("JobCity", actualJobCity);
                actualMap.put("JobID", actualJobID);
                System.out.println("Expected Map-----" + expectedMap);
                System.out.println("Actual Map------" + actualMap);


                /** Return to JobSearch page **/
                wt.until(ExpectedConditions.presenceOfElementLocated (By.xpath("//*[@class=\"btn btn-secondary ae-button\"]")));
                WebElement returnButton = driver.findElement(By.xpath("//*[@class=\"btn btn-secondary ae-button\"]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", returnButton);
                WebElement returnPageText= wt.until(ExpectedConditions.presenceOfElementLocated (By.xpath("//strong[contains(text(),'apply through the internal careers')]")));
                String actualReturnPageText = returnPageText.getText();
                String expectedReturnPageText = "Are you a current Labcorp employee?  If so, please apply through the internal careers site on mylabcorp.com.  The path is mylabcorp.com/myworkday/On the Job/Internal Careers.";

                /** Validation for Return to job Search Page**/
                Assert.assertEquals(actualReturnPageText, expectedReturnPageText);

                /** Validation for JobTitle, JobLocation, Job ID Match **/
                Assert.assertEquals(actualMap,expectedMap);
                driver.close();
            }
        }
        driver.switchTo().window(parentWindow);


    }
}
