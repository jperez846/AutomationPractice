package e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Product Finder Page
 *
 * Represents the main UI page where users search for products
 * Maps to your App.jsx component
 *
 * This class encapsulates:
 * - All web elements on the page (using @FindBy)
 * - All actions users can perform (search, view results, etc.)
 * - All information users can see (product details, errors, etc.)
 */
public class ProductFinderPage extends BasePage {

    // ==================== WEB ELEMENTS ====================

    /**
     * @FindBy - Selenium annotation to locate elements
     * Locator strategies:
     * - id: Most reliable, fastest
     * - className: Good for styled elements
     * - xpath: Most flexible but slower
     * - css: Balance of flexibility and speed
     */

    /**
     * Page heading "Product Finder"
     * Xpath explanation: //h1 = find h1 element anywhere
     */
    @FindBy(xpath = "//h1[text()='Product Finder']")
    private WebElement pageHeading;

    /**
     * Product ID input field
     * css explanation: input[placeholder='...'] = input with specific placeholder
     */
    @FindBy(css = "input[placeholder='Enter Product ID']")
    private WebElement productIdInput;

    /**
     * Search button
     * xpath explanation: //button[text()='Search'] = button with "Search" text
     */
    @FindBy(xpath = "//button[text()='Search']")
    private WebElement searchButton;

    /**
     * Loading message
     * className explanation: Finds element by CSS class name
     */
    @FindBy(className = "loading")
    private WebElement loadingMessage;

    /**
     * Product name (h2 in product-detail div)
     * css explanation: .product-detail h2 = h2 inside product-detail class
     */
    @FindBy(css = ".product-detail h2")
    private WebElement productName;

    /**
     * Product description
     */
    @FindBy(css = ".product-detail .description")
    private WebElement productDescription;

    /**
     * Product price
     */
    @FindBy(css = ".product-detail .price")
    private WebElement productPrice;

    /**
     * Product ID display
     */
    @FindBy(css = ".product-detail .id")
    private WebElement productIdDisplay;

    /**
     * Error message container
     */
    @FindBy(className = "error")
    private WebElement errorContainer;

    /**
     * Error heading
     */
    @FindBy(xpath = "//div[@class='error']//h3")
    private WebElement errorHeading;

    /**
     * Error message text
     */
    @FindBy(xpath = "//div[@class='error']//p")
    private WebElement errorMessage;

    /**
     * "Product not found" message
     */
    @FindBy(className = "not-found")
    private WebElement notFoundMessage;

    // ==================== CONSTRUCTOR ====================

    /**
     * Constructor
     *
     * @param driver - WebDriver instance
     */
    public ProductFinderPage(WebDriver driver) {
        super(driver);
    }

    // ==================== NAVIGATION ====================

    /**
     * Navigate to the Product Finder page
     *
     * @param baseUrl - Base URL (http://localhost:3000)
     * @return ProductFinderPage - for method chaining
     */
    public ProductFinderPage navigateTo(String baseUrl) {
        driver.get(baseUrl);

        // Wait for page to load by checking heading is visible
        waitForElementToBeVisible(pageHeading);

        return this;
    }

    // ==================== ACTIONS ====================

    /**
     * Enter product ID in search field
     *
     * @param productId - ID to search for
     * @return ProductFinderPage - for method chaining
     */
    public ProductFinderPage enterProductId(String productId) {
        typeText(productIdInput, productId);
        return this;
    }

    /**
     * Click the search button
     *
     * @return ProductFinderPage - for method chaining
     */
    public ProductFinderPage clickSearch() {
        clickElement(searchButton);
        return this;
    }

    /**
     * Perform complete search flow
     * Convenience method combining enter + click
     *
     * @param productId - ID to search for
     * @return ProductFinderPage - for method chaining
     */
    public ProductFinderPage searchForProduct(String productId) {
        enterProductId(productId);
        clickSearch();
        return this;
    }

    /**
     * Wait for loading to finish
     * Waits until loading message disappears
     *
     * @return ProductFinderPage - for method chaining
     */
    public ProductFinderPage waitForLoadingToFinish() {
        try {
            // Wait for loading message to appear first
            waitForElementToBeVisible(loadingMessage);

            // Then wait for it to disappear
            wait.until(driver -> !isElementDisplayed(loadingMessage));
        } catch (Exception e) {
            // Loading might be too fast to catch, that's OK
        }
        return this;
    }

    // ==================== VERIFICATIONS ====================

    /**
     * Check if page heading is displayed
     *
     * @return boolean - true if heading visible
     */
    public boolean isPageLoaded() {
        return isElementDisplayed(pageHeading);
    }

    /**
     * Check if product details are displayed
     *
     * @return boolean - true if product shown
     */
    public boolean isProductDisplayed() {
        return isElementDisplayed(productName);
    }

    /**
     * Check if error is displayed
     *
     * @return boolean - true if error shown
     */
    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorContainer);
    }

    /**
     * Check if "not found" message is displayed
     *
     * @return boolean - true if not found message shown
     */
    public boolean isNotFoundMessageDisplayed() {
        return isElementDisplayed(notFoundMessage);
    }

    /**
     * Check if loading message is displayed
     *
     * @return boolean - true if loading
     */
    public boolean isLoading() {
        return isElementDisplayed(loadingMessage);
    }

    // ==================== GETTERS ====================

    /**
     * Get displayed product name
     *
     * @return String - product name
     */
    public String getProductName() {
        return getElementText(productName);
    }

    /**
     * Get displayed product description
     *
     * @return String - product description
     */
    public String getProductDescription() {
        return getElementText(productDescription);
    }

    /**
     * Get displayed product price
     *
     * @return String - product price
     */
    public String getProductPrice() {
        return getElementText(productPrice);
    }

    /**
     * Get displayed product ID
     *
     * @return String - product ID
     */
    public String getProductId() {
        return getElementText(productIdDisplay);
    }

    /**
     * Get error heading text
     *
     * @return String - error heading
     */
    public String getErrorHeading() {
        return getElementText(errorHeading);
    }

    /**
     * Get error message text
     *
     * @return String - error message
     */
    public String getErrorMessage() {
        return getElementText(errorMessage);
    }

    /**
     * Get page heading text
     *
     * @return String - page heading
     */
    public String getPageHeading() {
        return getElementText(pageHeading);
    }
}