package tests;

import application.service.ProductService;
import org.testng.annotations.Test;
import application.model.Product;
import application.model.ProductResponseDTO;
import application.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Why are we using mockito and assert j ? With unit testing we focus on isolation
 * meaning we want to test units of code, this case (ProductService)
 * we want to do this without having to hit a real database, so instead we mock that behavior
 * for example (ProductRepository)
 * also assertJ has cleaner assertions with its readable api & better error reporting
 * * TestNG Annotations Used:
 *  * - @Test: Marks a method as a test case
 *  * - @BeforeMethod: Runs before each test method (like JUnit's @BeforeEach)
 *  * - @AfterMethod: Runs after each test method (not used here)
 *  * - @BeforeClass: Runs once before all tests in the class
 *  * - @AfterClass: Runs once after all tests in the class
 */

public class ProductServiceTests {

    /**
     * @Mock annotation creates a MOCK (fake) instance of ProductRepository.
     *
     * What is a Mock?
     * - A fake object that simulates the behavior of a real object
     * - Allows us to control what the repository returns WITHOUT a database
     * - We can verify that the service calls the repository correctly
     *
     * Why Mock?
     * - Unit tests should be FAST (no database queries)
     * - Unit tests should be ISOLATED (not dependent on external systems)
     * - We want to test ProductService logic, not ProductRepository logic
     */
    @Mock
    private ProductRepository productRepository;
    /**
     * @InjectMocks creates an instance of ProductService and automatically
     * injects the @Mock objects into it.
     *
     * This is equivalent to:
     * productService = new ProductService(productRepository);
     *
     * But Mockito does it automatically for us!
     */
    @InjectMocks
    private ProductService productService;
    /**
     * @BeforeMethod runs before EACH test method.
     * This ensures each test starts with a fresh, clean state.
     *
     * MockitoAnnotations.openMocks(this):
     * - Initializes all @Mock and @InjectMocks annotations
     * - Creates the mock objects and injects them
     * - MUST be called before using mocks
     */
    @BeforeMethod
    public void setUp() {
        // Initialize Mockito annotations (creates mocks and injects them)
        MockitoAnnotations.openMocks(this);

        // Any other setup code can go here
        // Example: Common test data, resetting counters, etc.
    }
    /**
     * TEST 1: Happy Path - Product Exists
     *
     * Tests the scenario where:
     * - We search for a product by ID
     * - The product EXISTS in the database
     * - The service should return a ProductResponseDTO
     *
     * Test Structure (AAA Pattern):
     * 1. ARRANGE: Set up test data and mock behavior
     * 2. ACT: Call the method being tested
     * 3. ASSERT: Verify the results
     */
    @Test
    public void testGetProductById_WhenProductExists_ShouldReturnProductDTO() {
        // ==================== ARRANGE ====================

        // Create a test Product entity (this simulates data from DB)
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Test Product");
        mockProduct.setDescription("A great test product");
        mockProduct.setPrice(29.99);

        /**
         * STUBBING: Define what the mock repository should return
         *
         * when(productRepository.findById(productId))
         *   .thenReturn(Optional.of(mockProduct));
         *
         * Translation: "When someone calls findById(1L) on the mock repository,
         * return Optional.of(mockProduct) instead of hitting a real database"
         *
         * This allows us to control the repository's behavior in tests!
         */
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(mockProduct));

        // ==================== ACT ====================

        // Call the method we're testing
        Optional<ProductResponseDTO> result = productService.getProductById(productId);

        // ==================== ASSERT ====================

        /**
         * ASSERTIONS: Verify the results are correct
         *
         * Using AssertJ for fluent, readable assertions:
         * - assertThat(result).isPresent() - Checks Optional is not empty
         * - assertThat(dto.id()).isEqualTo(1L) - Checks values match
         */

        // Verify the result is present (not empty)
        assertThat(result).isPresent();

        // Extract the DTO from the Optional
        ProductResponseDTO dto = result.get();

        // Verify all fields were mapped correctly from Product to DTO
        assertThat(dto.id()).isEqualTo(productId);
        assertThat(dto.name()).isEqualTo("Test Product");
        assertThat(dto.description()).isEqualTo("A great test product");
        assertThat(dto.price()).isEqualTo(29.99);

        /**
         * VERIFICATION: Ensure the repository method was called
         *
         * verify(productRepository, times(1)).findById(productId);
         *
         * Translation: "Verify that findById(1L) was called EXACTLY once
         * on the mock repository"
         *
         * This ensures the service actually uses the repository!
         */
        verify(productRepository, times(1)).findById(productId);

        /**
         * verifyNoMoreInteractions: Ensures no other methods were called
         * This is optional but helps catch unexpected behavior
         */
        verifyNoMoreInteractions(productRepository);
    }



}

