package test;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class captureScreenShot extends BaseClass{
    public static String getScreenShot(WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String path =System.getProperty("user.dir"+"/Screenshot/"+System.currentTimeMillis()+".png");
        File destination = new File(path);
        FileUtils.copyFile(source, destination);
        return path;
    }
}
