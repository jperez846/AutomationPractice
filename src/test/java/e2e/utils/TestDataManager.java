package e2e.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Test Data Manager
 *
 * Manages test data in the H2 database for E2E tests
 *
 * Purpose:
 * - Set up known data before tests
 * - Clean up data after tests
 * - Ensure tests have consistent starting state
 */
public class TestDataManager {

    // H2 database connection details
   // private static final String DB_URL = "jdbc:h2:mem:testdb";
    //private static final String DB_URL = "jdbc:h2:file:./data/testdb;AUTO_SERVER=TRUE";
    private static final String DB_URL = "jdbc:h2:tcp://localhost:9092/mem:testdb;IFEXISTS=TRUE";

    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    /**
     * Insert test product into database
     *
     * @param id - Product ID
     * @param name - Product name
     * @param description - Product description
     * @param price - Product price
     * @throws Exception - if database operation fails
     */
    public static void insertTestProduct(Long id, String name, String description, Double price) throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            // SQL INSERT statement
            String sql = String.format(
                    "INSERT INTO product (id, name, description, price) VALUES (%d, '%s', '%s', %f)",
                    id, name, description, price
            );

            stmt.execute(sql);
            System.out.println("✓ Inserted test product: " + name + " (ID: " + id + ")");
        }
    }

    /**
     * Delete test product from database
     *
     * @param id - Product ID to delete
     * @throws Exception - if database operation fails
     */
    public static void deleteTestProduct(Long id) throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "DELETE FROM product WHERE id = " + id;
            stmt.execute(sql);
            System.out.println("✓ Deleted test product with ID: " + id);
        }
    }

    /**
     * Delete all products from database
     * Use with caution!
     *
     * @throws Exception - if database operation fails
     */
    public static void deleteAllProducts() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM product");
            System.out.println("✓ Deleted all test products");
        }
    }

    /**
     * Create default test products
     * Standard set of products for testing
     *
     * @throws Exception - if database operation fails
     */
    public static void createDefaultTestProducts() throws Exception {
        insertTestProduct(1L, "Test Product 1", "First test product", 29.99);
        insertTestProduct(2L, "Test Product 2", "Second test product", 49.99);
        insertTestProduct(3L, "Test Product 3", "Third test product", 99.99);
    }
}