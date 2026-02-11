package e2e.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot Utilities
 *
 * Captures screenshots for test debugging and reporting
 * Especially useful when tests fail
 */
public class ScreenshotUtils {

    // Directory to save screenshots
    private static final String SCREENSHOT_DIR = "screenshots/";

    /**
     * Take screenshot and save to file
     *
     * @param driver - WebDriver instance
     * @param testName - Name of test (for filename)
     * @return String - path to screenshot file
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

            // Create filename: testName_timestamp.png
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Save to file
            FileUtils.copyFile(screenshot, new File(filePath));

            System.out.println("📸 Screenshot saved: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.err.println("❌ Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot on test failure
     * Convenience method for TestNG listeners
     *
     * @param driver - WebDriver instance
     * @param testName - Name of failed test
     * @return String - path to screenshot
     */
    public static String captureFailure(WebDriver driver, String testName) {
        return takeScreenshot(driver, testName + "_FAILED");
    }
}