package e2e.tests;

import e2e.pages.ProductFinderPage;
import e2e.utils.ScreenshotUtils;
import e2e.utils.TestDataManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URL;


/**
 * End-to-End Tests for Product Search Flow
 *
 * PREREQUISITES:
 * 1. Spring Boot backend running on http://localhost:8080
 * 2. Vite React frontend running on http://localhost:3000
 * 3. H2 database accessible
 *
 * These tests:
 * - Use REAL browser (Chrome)
 * - Hit REAL backend API
 * - Use REAL database
 * - Simulate ACTUAL user actions
 *
 * Test Flow:
 * @BeforeClass: Setup WebDriver, create test data (once)
 * @BeforeMethod: Navigate to page (before each test)
 * @Test: Execute test scenario
 * @AfterMethod: Take screenshot if failed
 * @AfterClass: Cleanup (close browser, delete data)
 */

/**
 * E2E Tests with Docker Selenium Support
 *
 * This test class works in two modes:
 * 1. Local mode: Uses local ChromeDriver
 * 2. Docker mode: Uses Selenium Grid in Docker
 *
 * Mode is determined by system properties:
 * - selenium.hub.url: If set, use Grid (Docker mode)
 * - If not set, use local ChromeDriver (local mode)
 */
@Test(groups = "e2e")
public class ProductSearchE2ETest {

    // ==================== TEST CONFIGURATION ====================

    /**
     * WebDriver instance - controls the browser
     * Static so it's shared across all tests (faster)
     */
    private static WebDriver driver;

    /**
     * Page Object for Product Finder page
     */
    private ProductFinderPage productFinderPage;

    /**
     * Base URL of the React application
     * Update this if your frontend runs on different port
     */
   // private static final String BASE_URL = "http://localhost:5173";
    /**
     * Configuration from system properties or environment variables
     * These can be set via:
     * - Command line: -DseleniumHubUrl=http://localhost:4444
     * - Docker compose: environment variables
     * - IDE run configuration
     */
    private static final String SELENIUM_HUB_URL = System.getProperty(
            "seleniumHubUrl",
            System.getenv("SELENIUM_HUB_URL")
    );

    private static final String BASE_URL = System.getProperty(
            "frontendUrl",
            System.getenv().getOrDefault("FRONTEND_URL", "http://localhost:5173")
    );

    private static final String BROWSER = System.getProperty(
            "browser",
            System.getenv().getOrDefault("BROWSER", "chrome")
    );



    /**
     * Test data IDs
     * Using consistent IDs makes tests predictable
     */
    private static final Long EXISTING_PRODUCT_ID = 3000L;
    private static final Long NON_EXISTENT_PRODUCT_ID = 999L;

    // ==================== SETUP & TEARDOWN ====================

    /**
     * @BeforeClass - Runs ONCE before all tests in this class
     *
     * Sets up:
     * - WebDriver (browser)
     * - Test data in database
     */
    @BeforeClass
    public void setupClass() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 Setting up E2E Test Suite");
        System.out.println("=".repeat(60));

        // Log configuration
        System.out.println("Configuration:");
        System.out.println("  Selenium Hub: " + (SELENIUM_HUB_URL != null ? SELENIUM_HUB_URL : "Local ChromeDriver"));
        System.out.println("  Frontend URL: " + BASE_URL);
        System.out.println("  Browser: " + BROWSER);
        System.out.println("=".repeat(60) + "\n");

        // ======================================================================
        // INITIALIZE WEBDRIVER
        // ======================================================================
        //
        // Two modes:
        // 1. Docker mode: SELENIUM_HUB_URL is set → use RemoteWebDriver
        // 2. Local mode: SELENIUM_HUB_URL is null → use ChromeDriver
        // ======================================================================

        if (SELENIUM_HUB_URL != null) {
            // ================================================================
            // DOCKER MODE: Use Selenium Grid
            // ================================================================
            System.out.println("🐳 Using Selenium Grid (Docker mode)");
            driver = createRemoteWebDriver();

        } else {
            // ================================================================
            // LOCAL MODE: Use local ChromeDriver
            // ================================================================
            System.out.println("💻 Using local ChromeDriver");

            // WebDriverManager auto-downloads ChromeDriver
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = createChromeOptions();
            driver = new org.openqa.selenium.chrome.ChromeDriver(options);
        }

        // ======================================================================
        // CREATE TEST DATA
        // ======================================================================
        //
        // Note: In Docker, this connects to backend container
        // Connection URL must be accessible from this container
        // ======================================================================

        System.out.println("📊 Creating test data...");

        // For Docker: Use backend service name
        // For local: Use localhost
        if (SELENIUM_HUB_URL != null) {
            // Running in Docker - backend is accessible as "backend" hostname
            System.out.println("  Using Docker network hostname for database");
        }

        TestDataManager.insertTestProduct(
                EXISTING_PRODUCT_ID,
                "E2E Test Product",
                "This product is for end-to-end testing",
                79.99
        );

        System.out.println("✅ Setup complete!\n");
    }
    /**
     * Create RemoteWebDriver for Selenium Grid
     *
     * RemoteWebDriver:
     * - Connects to Selenium Hub
     * - Requests a browser session
     * - Sends commands over HTTP to Hub
     * - Hub forwards commands to browser node
     *
     * @return RemoteWebDriver instance
     * @throws Exception if connection fails
     */
    private static WebDriver createRemoteWebDriver() throws Exception {
        System.out.println("  Connecting to Selenium Hub: " + SELENIUM_HUB_URL);

        // Configure Chrome options
        ChromeOptions options = createChromeOptions();

        // Create RemoteWebDriver
        // URL: Selenium Hub endpoint (e.g., http://selenium-hub:4444)
        // Options: Browser capabilities (Chrome with specific options)
        // /wd/hub, url for setting up web driver hub with our desired options
        RemoteWebDriver remoteDriver = new RemoteWebDriver(
                new URL(SELENIUM_HUB_URL + "/wd/hub"),
                options
        );

        System.out.println("  ✓ Connected to Selenium Grid");
        System.out.println("  Session ID: " + remoteDriver.getSessionId());

        return remoteDriver;
    }

    /**
     * Create Chrome options
     *
     * ChromeOptions configures browser behavior:
     * - Window size, headless mode, extensions, etc.
     *
     * @return ChromeOptions
     */
    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // ======================================================================
        // CHROME ARGUMENTS
        // ======================================================================
        //
        // These modify Chrome's behavior
        // ======================================================================

        // Disable notifications
        options.addArguments("--disable-notifications");

        // Disable popup blocking (allow popups in tests)
        options.addArguments("--disable-popup-blocking");

        // Start maximized (full screen)
        options.addArguments("--start-maximized");

        // Disable GPU (prevents some crashes in Docker)
        options.addArguments("--disable-gpu");

        // No sandbox (required in Docker)
        // Sandbox provides security isolation
        // Docker already provides isolation, so we disable it
        options.addArguments("--no-sandbox");

        // Disable /dev/shm usage (required in Docker)
        // /dev/shm is shared memory - limited in containers
        options.addArguments("--disable-dev-shm-usage");

        // ======================================================================
        // HEADLESS MODE (Optional)
        // ======================================================================
        //
        // Headless: Run browser without GUI
        // Useful for:
        // - Faster execution (no rendering)
        // - CI/CD environments (no display)
        // - Background testing
        //
        // But: Can't watch tests run, harder to debug
        // ======================================================================

        // Uncomment for headless mode:
        // options.addArguments("--headless=new");

        // ======================================================================
        // CHROME PREFERENCES
        // ======================================================================
        //
        // Set Chrome preferences (like chrome://settings)
        // ======================================================================

        // Disable password save prompts
        options.setExperimentalOption("prefs", java.util.Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false
        ));

        return options;
    }

    /**
     * @BeforeMethod - Runs BEFORE EACH test method
     *
     * Ensures each test starts with fresh state
     */
    @BeforeMethod
    public void setupTest() {
        System.out.println("🧪 Starting new test...");

        // Create new page object for this test
        productFinderPage = new ProductFinderPage(driver);

        // Navigate to the Product Finder page
        productFinderPage.navigateTo(BASE_URL);

        // Verify page loaded correctly
        Assert.assertTrue(
                productFinderPage.isPageLoaded(),
                "Product Finder page should be loaded"
        );
    }

    /**
     * @AfterMethod - Runs AFTER EACH test method
     *
     * @param result - Test result object from TestNG
     */
    @AfterMethod
    public void teardownTest(ITestResult result) {
        /**
         * Take screenshot if test failed
         * Helps debug what went wrong
         */
        if (!result.isSuccess()) {
            System.out.println("❌ Test failed, taking screenshot...");
            ScreenshotUtils.captureFailure(driver, result.getName());
        }

        System.out.println("✓ Test complete\n");
    }

    /**
     * @AfterClass - Runs ONCE after all tests complete
     *
     * Cleanup:
     * - Close browser
     * - Delete test data
     */
    @AfterClass
    public void teardownClass() throws Exception {
        System.out.println("\n🧹 Cleaning up...");

        // Delete test data from database
        TestDataManager.deleteTestProduct(EXISTING_PRODUCT_ID);

        // Close browser
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed");
        }

        System.out.println("✅ Cleanup complete!\n");
    }

    // ==================== TEST CASES ====================

    /**
     * TEST 1: Happy Path - Search for Existing Product
     *
     * User Story:
     * As a user, I want to search for a product by ID
     * So that I can view its details
     *
     * Steps:
     * 1. Enter valid product ID
     * 2. Click search button
     * 3. Verify product details display correctly
     */
    @Test(priority = 1, description = "Verify user can search and view existing product")
    public void testSearchExistingProduct() {
        System.out.println("TEST: Search for existing product (ID: " + EXISTING_PRODUCT_ID + ")");

        // ACT: Perform search
        productFinderPage
                .searchForProduct(EXISTING_PRODUCT_ID.toString())
                .waitForLoadingToFinish();

        // ASSERT: Verify product is displayed
        Assert.assertTrue(
                productFinderPage.isProductDisplayed(),
                "Product details should be displayed"
        );

        // ASSERT: Verify product details are correct
        Assert.assertEquals(
                productFinderPage.getProductName(),
                "E2E Test Product",
                "Product name should match"
        );

        Assert.assertEquals(
                productFinderPage.getProductDescription(),
                "This product is for end-to-end testing",
                "Product description should match"
        );

        Assert.assertEquals(
                productFinderPage.getProductPrice(),
                "$79.99",
                "Product price should match"
        );

        Assert.assertTrue(
                productFinderPage.getProductId().contains(EXISTING_PRODUCT_ID.toString()),
                "Product ID should be displayed"
        );

        System.out.println("✓ Product details verified successfully");
    }

    /**
     * TEST 2: Sad Path - Search for Non-Existent Product
     *
     * User Story:
     * As a user, when I search for a product that doesn't exist
     * I should see an appropriate error message
     *
     * Steps:
     * 1. Enter non-existent product ID
     * 2. Click search
     * 3. Verify error message displays
     */
//    @Test(priority = 2, description = "Verify error message for non-existent product")
//    public void testSearchNonExistentProduct() {
//        System.out.println("TEST: Search for non-existent product (ID: " + NON_EXISTENT_PRODUCT_ID + ")");
//
//        // ACT: Search for product that doesn't exist
//        productFinderPage
//                .searchForProduct(NON_EXISTENT_PRODUCT_ID.toString())
//                .waitForLoadingToFinish();
//
//        // ASSERT: Verify error is displayed
//        Assert.assertTrue(
//                productFinderPage.isErrorDisplayed(),
//                "Error message should be displayed"
//        );
//
//        // ASSERT: Verify error message content
//        Assert.assertEquals(
//                productFinderPage.getErrorHeading(),
//                "Error loading product",
//                "Error heading should match"
//        );
//
//        Assert.assertTrue(
//                productFinderPage.getErrorMessage().contains("not found"),
//                "Error message should mention product not found"
//        );
//
//        System.out.println("✓ Error message verified successfully");
//    }
//
//    /**
//     * TEST 3: UI Validation - Empty Search
//     *
//     * User Story:
//     * As a user, I should not be able to search with empty input
//     *
//     * Steps:
//     * 1. Leave input empty
//     * 2. Click search
//     * 3. Verify no API call is made (no loading, no results)
//     */
//    @Test(priority = 3, description = "Verify empty search is prevented")
//    public void testEmptySearch() {
//        System.out.println("TEST: Attempt search with empty input");
//
//        // ACT: Click search without entering ID
//        productFinderPage.clickSearch();
//
//        // ASSERT: No product or error should be shown
//        Assert.assertFalse(
//                productFinderPage.isProductDisplayed(),
//                "Product should not be displayed"
//        );
//
//        Assert.assertFalse(
//                productFinderPage.isErrorDisplayed(),
//                "Error should not be displayed"
//        );
//
//        System.out.println("✓ Empty search prevented successfully");
//    }
//
//    /**
//     * TEST 4: Multiple Sequential Searches
//     *
//     * User Story:
//     * As a user, I want to search for multiple products in sequence
//     * And see updated results each time
//     *
//     * Steps:
//     * 1. Search for existing product
//     * 2. Search for non-existent product
//     * 3. Search for existing product again
//     * 4. Verify results update correctly each time
//     */
//    @Test(priority = 4, description = "Verify multiple sequential searches work correctly")
//    public void testMultipleSearches() throws Exception {
//        System.out.println("TEST: Multiple sequential searches");
//
//        // Create second test product
//        Long secondProductId = 101L;
//        TestDataManager.insertTestProduct(
//                secondProductId,
//                "Second E2E Product",
//                "Another test product",
//                39.99
//        );
//
//        try {
//            // Search 1: First product
//            System.out.println("  → Search 1: Product " + EXISTING_PRODUCT_ID);
//            productFinderPage
//                    .searchForProduct(EXISTING_PRODUCT_ID.toString())
//                    .waitForLoadingToFinish();
//
//            Assert.assertTrue(productFinderPage.isProductDisplayed());
//            Assert.assertEquals(productFinderPage.getProductName(), "E2E Test Product");
//
//            // Search 2: Non-existent product
//            System.out.println("  → Search 2: Non-existent product");
//            productFinderPage
//                    .searchForProduct(NON_EXISTENT_PRODUCT_ID.toString())
//                    .waitForLoadingToFinish();
//
//            Assert.assertTrue(productFinderPage.isErrorDisplayed());
//
//            // Search 3: Second product
//            System.out.println("  → Search 3: Product " + secondProductId);
//            productFinderPage
//                    .searchForProduct(secondProductId.toString())
//                    .waitForLoadingToFinish();
//
//            Assert.assertTrue(productFinderPage.isProductDisplayed());
//            Assert.assertEquals(productFinderPage.getProductName(), "Second E2E Product");
//
//            System.out.println("✓ All searches completed successfully");
//
//        } finally {
//            // Cleanup second product
//            TestDataManager.deleteTestProduct(secondProductId);
//        }
//    }
//
//    /**
//     * TEST 5: Page Title and URL Verification
//     *
//     * User Story:
//     * As a user, I want to ensure I'm on the correct page
//     *
//     * Steps:
//     * 1. Verify page URL is correct
//     * 2. Verify page heading is displayed
//     */
//    @Test(priority = 5, description = "Verify page URL and title")
//    public void testPageUrlAndTitle() {
//        System.out.println("TEST: Page URL and title verification");
//
//        // ASSERT: Verify URL
//        Assert.assertEquals(
//                productFinderPage.getCurrentUrl(),
//                BASE_URL + "/",
//                "Page URL should match expected"
//        );
//
//        // ASSERT: Verify heading
//        Assert.assertEquals(
//                productFinderPage.getPageHeading(),
//                "Product Finder",
//                "Page heading should be 'Product Finder'"
//        );
//
//        System.out.println("✓ URL and title verified successfully");
//    }
//
//    /**
//     * TEST 6: Loading State Verification
//     *
//     * User Story:
//     * As a user, I want to see a loading indicator while data is fetching
//     * So I know the app is working
//     *
//     * Note: This test might be flaky if the response is very fast
//     */
//    @Test(priority = 6, description = "Verify loading state is shown during search")
//    public void testLoadingState() {
//        System.out.println("TEST: Loading state verification");
//
//        // ACT: Start search
//        productFinderPage.enterProductId(EXISTING_PRODUCT_ID.toString());
//        productFinderPage.clickSearch();
//
//        // ASSERT: Loading should appear (might be very brief)
//        // Note: This assertion might fail if backend is very fast
//        // In real scenarios, you might add artificial delay in backend for testing
//
//        // Wait for loading to finish
//        productFinderPage.waitForLoadingToFinish();
//
//        // ASSERT: Product should now be displayed
//        Assert.assertTrue(
//                productFinderPage.isProductDisplayed(),
//                "Product should be displayed after loading"
//        );
//
//        System.out.println("✓ Loading state test complete");
//    }
}