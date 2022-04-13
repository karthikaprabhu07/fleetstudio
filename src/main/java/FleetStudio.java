import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.FleetStudioPage;
import utils.ExtentTestManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FleetStudio {
    WebDriver driver = WebDriverManager.chromedriver().create();
    FleetStudioPage objFleetStudio = new FleetStudioPage();
    ExtentTest logger;
    JavascriptExecutor executor = (JavascriptExecutor) driver;


    public static String takeScreenshot(WebDriver driver, String screenShotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File finalDestination = new File("./Reports/" + screenShotName + ".png");
        FileUtils.copyFile(source, finalDestination);
        return finalDestination.getName();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        //driver = WebDriverManager.chromedriver().create();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://fleetstudio.com/works/");
    }

    @Test
    public void failTest() {
        logger = ExtentTestManager.startTest("failTest", "Failure Test");
        String currentURL = driver.getCurrentUrl();
        Assert.assertEquals(currentURL, "https://fleetstudio.com/works/)");
        logger.log(Status.PASS, "Test Case (failTest) Status is passed");
    }
    @Test
    public void validateThePage() {

        Assert.assertEquals(driver.findElement(FleetStudioPage.lblHeader).getText(),"A Product Development Technology Company");
        executor.executeScript("window.scrollBy(0,350)");
        Select objSelect=new Select(driver.findElement(FleetStudioPage.ddlFilter));
        List<WebElement> elementCount=objSelect.getOptions();
        Assert.assertEquals(elementCount.size(),3);
        executor.executeScript("window.scrollBy(0,-350)");
        executor.executeScript("arguments[0].click()",driver.findElement(FleetStudioPage.imgFleetStudio));
        Assert.assertEquals(driver.getCurrentUrl(),"https://fleetstudio.com/works/");



             logger.log(Status.PASS,"Test case of (pass test) Status is passed");


    }

    @AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.log(Status.FAIL, "Test Case Failed is " + result.getName());
            String screenshotPath = FleetStudio.takeScreenshot(driver, result.getName());
            //To add it in the extent report
            logger.addScreenCaptureFromPath(screenshotPath, "Failed_Test_Case");
        }
    }

    @AfterTest
    public void endReport() {
        ExtentTestManager.endTest();
    }
}