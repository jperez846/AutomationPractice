package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import config.PropertiesFileReader;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static PropertiesFileReader data = new PropertiesFileReader("config.properties");
    public static void initDriver(){
        ChromeOptions options = new ChromeOptions();
        if(data.getPropertyValue("headless").equals("true")){
            options.addArguments("--headless-new");
        }
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver(options));


    }
    public static WebDriver getDriver(){
        return driver.get();
    }

    public static void quitDriver(){
        if(driver.get() != null){
            driver.get().quit();
            driver.remove();
        }
    }

}
