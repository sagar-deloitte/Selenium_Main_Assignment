package test;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;


public class AssignmentTest extends BaseClass {

    // Global variables
    String customerID;
    int amount;

    // Test to launch the URL
    @Test(priority = 1)
    public void launchURL(){

        // Launching the URL
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
        driver.manage().window().maximize();
    }

    // Test to add a customer
    @Test(priority = 2)
    public void addCustomer() throws IOException {

        // Waiting time
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate : Bank Manger Login -> Add customer
        driver.findElement(By.xpath("//*[text()='Bank Manager Login']")).click();
        driver.findElement(By.xpath("//*[@ng-class='btnClass1']")).click();

        // Fetching data from the Excel file
        File file1 = new File(".\\CustomerList.xls");
        FileInputStream inputStream = new FileInputStream(file1);
        HSSFWorkbook wb=new HSSFWorkbook(inputStream);
        HSSFSheet sheet1=wb.getSheetAt(0);

        // First Name
        HSSFRow row1 = sheet1.getRow(1);
        HSSFCell cell1 = row1.getCell(0);
        String firstName = cell1.getStringCellValue();

        // Last Name
        HSSFRow row2 = sheet1.getRow(1);
        HSSFCell cell2 = row1.getCell(1);
        String lastName = cell2.getStringCellValue();

        // Postal Code
        HSSFRow row3 = sheet1.getRow(1);
        HSSFCell cell3 = row1.getCell(2);
        double postalCode = cell3.getNumericCellValue();

        // Filling the add customer form
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[1]/input")).sendKeys(firstName);
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[2]/input")).sendKeys(lastName);
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[3]/input")).sendKeys(String.valueOf(postalCode));
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/button")).click();

        // Reading the customer ID from alert message
        String message = driver.switchTo().alert().getText();
        int len = message.length();
        customerID = message.substring(46,len);

        // Handling alert
        driver.switchTo().alert().accept();

        //Back to home
        driver.findElement(By.xpath("/html/body/div/div/div[1]/button[1]")).click();
    }

    // Test to open account for the customer
    @Test(priority = 3)
    public void openAccount(){

        // Navigate : Bank Manger Login -> Open Account
        driver.findElement(By.xpath("//*[text()='Bank Manager Login']")).click();
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/button[2]")).click();

        // Select customer from drop down menu
        Select customerDropDown = new Select(driver.findElement(By.xpath("//*[@id=\"userSelect\"]")));
        customerDropDown.selectByValue(customerID);

        // Select random currency from drop down menu
        Select currencyDropDown = new Select(driver.findElement(By.xpath("//*[@id=\"currency\"]")));
        int currencyIndex = (int) Math.floor(Math.random()*(3-1+1)+1);
        currencyDropDown.selectByIndex(currencyIndex);

        // Processing account creation
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/button")).click();

        // Handling alert
        driver.switchTo().alert().accept();
    }

    // Test to deposit amount
    @Test(priority = 4)
    public void depositAmount(){

        //Back to home
        driver.findElement(By.xpath("/html/body/div/div/div[1]/button[1]")).click();

        // Customer Login
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div[1]/button")).click();
        Select nameDropDown = new Select(driver.findElement(By.xpath("//*[@id=\"userSelect\"]")));
        nameDropDown.selectByValue(customerID);

        // Deposit a random amount
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[3]/button[2]")).click();
        amount = (int) Math.floor(Math.random()*(3000-100+1)+100);
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[4]/div/form/div/input")).sendKeys(String.valueOf(amount));
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[4]/div/form/button")).click();

        // Verifying account balance
        String accountBalance = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/strong[2]")).getText();
        String strAmount = String.valueOf(amount);
        Assert.assertEquals(strAmount, accountBalance);
    }

    // Test to withdrawal valid amount
    @Test(priority = 5)
    public void withdrawalValidAmount(){

        // Withdrawing a random valid amount
        driver.findElement(By.xpath("//*[@ng-class='btnClass3']")).click();
        int withdrawalAmount = (int) Math.floor(Math.random()*(99-1+10)+10);
        driver.findElement(By.xpath("//*[@class='form-control ng-pristine ng-untouched ng-invalid ng-invalid-required']")).sendKeys(String.valueOf(withdrawalAmount));
        driver.findElement(By.xpath("//*[text()='Withdraw']")).click();

        // Verifying new balance
        amount=amount-withdrawalAmount;
        String strAmount2 = String.valueOf(amount);
        String newAccountBalance = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/strong[2]")).getText();
        Assert.assertEquals(strAmount2, newAccountBalance);
    }

    // Test to withdrawal invalid amount
    @Test(priority = 6)
    public void withdrawalInvalidAmount(){

        // Withdrawing a random invalid amount
        int invalidWithdrawalAmount = (int) Math.floor(Math.random()*(7000-1+3001)+3001);
        driver.findElement(By.xpath("//*[@class='form-control ng-dirty ng-touched ng-invalid ng-invalid-required']")).sendKeys(String.valueOf(invalidWithdrawalAmount));
        driver.findElement(By.xpath("//*[text()='Withdraw']")).click();

        // Verifying error message
        String expectedErrorMessage = "Transaction Failed. You can not withdraw amount more than the balance.";
        String actualErrorMessage = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[4]/div/span")).getText();
        Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}
