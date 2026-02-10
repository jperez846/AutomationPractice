package base;

import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseUITest {
    protected WebDriver driver;
    // setup a driver
    @BeforeMethod
    public void setup(){
        // setup a driver
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown(){
        DriverFactory.quitDriver();

    }
}
