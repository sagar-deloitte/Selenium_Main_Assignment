package test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseClass {

    // Global variables
    public static WebDriver driver;
    static ExtentTest test;
    static ExtentReports report;

    // Initial setup
    @BeforeTest

    public static WebDriver initialSetUp() {
        report = new ExtentReports(System.getProperty("Selenium_Main_Assignment.dir")+"ReportResults.html");
        test = report.startTest("AssignmentTest");
        System.setProperty("webdriver.chrome.driver","C:\\Users\\sagathakur\\Documents\\chromedriver.exe");
        driver = new ChromeDriver();
        return driver;
    }


    // Logging and Reporting
    @AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if(result.getStatus() == ITestResult.FAILURE) {

            test.log(LogStatus.FAIL, (result.getName()+" Failed"));

            // Taking screenshots
            String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String destination = System.getProperty("Selenium_Main_Assignment.dir") + "/TestsScreenshots/"+dateName+".png";
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);
        }
        else if(result.getStatus() == ITestResult.SUCCESS){
            test.log(LogStatus.PASS, (result.getName()+" Successful"));
        }
        else {
            test.log(LogStatus.SKIP, (result.getName()+" Skipped"));
        }
    }


    @AfterClass
    public static void endTest()
    {
        report.endTest(test);
        report.flush();
    }

}
