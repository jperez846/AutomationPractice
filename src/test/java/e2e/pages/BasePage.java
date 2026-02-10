package e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base Page Object
 *
 * Contains common functionality used by all page objects:
 * - WebDriver instance
 * - Waits and timeouts
 * - Common actions (click, type, etc.)
 *
 * Page Object Model (POM) Pattern:
 * - Separates test logic from page structure
 * - Makes tests maintainable (if UI changes, update one place)
 * - Provides reusable methods for page interactions
 */
public abstract class BasePage {

    /**
     * WebDriver instance - controls the browser
     * Protected so child classes can access it
     */
    protected WebDriver driver;

    /**
     * WebDriverWait - handles explicit waits
     * Used to wait for elements to be visible, clickable, etc.
     */
    protected WebDriverWait wait;

    /**
     * Default timeout in seconds
     * How long to wait for elements before failing
     */
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * Constructor
     *
     * @param driver - WebDriver instance from test
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;

        // Initialize WebDriverWait with default timeout
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        /**
         * PageFactory.initElements():
         * Initializes all @FindBy annotated elements in child classes
         * This is Selenium's way of lazy-loading web elements
         */
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be visible
     *
     * @param element - WebElement to wait for
     * @return WebElement - the visible element
     */
    protected WebElement waitForElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     *
     * @param element - WebElement to wait for
     * @return WebElement - the clickable element
     */
    protected WebElement waitForElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Type text into input field
     * Waits for element to be visible first
     *
     * @param element - Input field
     * @param text - Text to type
     */
    protected void typeText(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear(); // Clear existing text first
        element.sendKeys(text);
    }

    /**
     * Click on element
     * Waits for element to be clickable first
     *
     * @param element - Element to click
     */
    protected void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }

    /**
     * Get text from element
     * Waits for element to be visible first
     *
     * @param element - Element to get text from
     * @return String - text content of element
     */
    protected String getElementText(WebElement element) {
        waitForElementToBeVisible(element);
        return element.getText();
    }

    /**
     * Check if element is displayed
     *
     * @param element - Element to check
     * @return boolean - true if displayed
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get current page URL
     *
     * @return String - current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title
     *
     * @return String - page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}