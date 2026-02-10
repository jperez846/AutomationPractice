/**
 * Unit Tests for Product Service
 * 
 * What we're testing:
 * - Service methods make correct API calls
 * - Service handles successful responses correctly
 * - Service handles errors appropriately
 * - Service transforms data correctly
 * 
 * Testing Strategy:
 * - Mock axios to avoid real HTTP calls
 * - Test each service method in isolation
 * - Test both success and error scenarios
 */

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import productService from '../productService';
import axios from '../../api/axiosConfig';
import { mockProduct, mockProductList, mockErrors } from '../../test/mockData';

/**
 * Mock the axios module
 * vi.mock() replaces the real axios with a fake version
 * This prevents actual HTTP requests during tests
 */
vi.mock('../../api/axiosCo  nfig');

describe('ProductService', () => {
  /**
   * beforeEach runs before EACH test
   * Ensures each test starts with a clean slate
   */
  beforeEach(() => {
    // Clear all mock function call history
    vi.clearAllMocks();
  });

  /**
   * afterEach runs after EACH test
   * Clean up after tests
   */
  afterEach(() => {
    // Reset all mocks to initial state
    vi.resetAllMocks();
  });

  /**
   * TEST SUITE: getProductById
   * Groups related tests together
   */
  describe('getProductById', () => {
    /**
     * TEST: Successful product fetch
     * 
     * Verifies:
     * - Correct API endpoint is called
     * - Response data is returned correctly
     * - Product data matches expected structure
     */
    it('should fetch product successfully when product exists', async () => {
      // ARRANGE: Set up mock response
      // When axios.get is called, return this mock data
      axios.get.mockResolvedValueOnce({ data: mockProduct });

      // ACT: Call the service method
      const result = await productService.getProductById(1);

      // ASSERT: Verify behavior
      // 1. Check axios was called with correct endpoint
      expect(axios.get).toHaveBeenCalledWith('/products/1');
      
      // 2. Check axios was called exactly once
      expect(axios.get).toHaveBeenCalledTimes(1);
      
      // 3. Check returned data matches mock
      expect(result).toEqual(mockProduct);
      
      // 4. Check specific fields
      expect(result.id).toBe(1);
      expect(result.name).toBe('Test Product');
      expect(result.price).toBe(29.99);
    });

    /**
     * TEST: Product not found (404)
     * 
     * Verifies:
     * - Service handles 404 errors correctly
     * - Error message is appropriate
     */
    it('should throw error when product not found (404)', async () => {
      // ARRANGE: Mock a 404 error response
      axios.get.mockRejectedValueOnce(mockErrors.notFound);

      // ACT & ASSERT: Expect the promise to reject with specific error
      await expect(productService.getProductById(999))
        .rejects
        .toThrow('Product with ID 999 not found');

      // Verify axios was called
      expect(axios.get).toHaveBeenCalledWith('/products/999');
    });

    /**
     * TEST: Server error (500)
     * 
     * Verifies service handles server errors
     */
    it('should throw error when server error occurs (500)', async () => {
      // ARRANGE: Mock a 500 error
      axios.get.mockRejectedValueOnce(mockErrors.serverError);

      // ACT & ASSERT
      await expect(productService.getProductById(1))
        .rejects
        .toThrow('Server error - please try again later');
    });

    /**
     * TEST: Network error
     * 
     * Verifies service handles network failures
     */
    it('should throw error when network error occurs', async () => {
      // ARRANGE: Mock a network error (no response)
      axios.get.mockRejectedValueOnce(mockErrors.networkError);

      // ACT & ASSERT
      await expect(productService.getProductById(1))
        .rejects
        .toThrow('Network error - please check your connection');
    });

    /**
     * TEST: Generic error
     * 
     * Verifies service handles unexpected errors
     */
    it('should throw generic error for unknown errors', async () => {
      // ARRANGE: Mock an unexpected error
      const unexpectedError = new Error('Something weird happened');
      axios.get.mockRejectedValueOnce(unexpectedError);

      // ACT & ASSERT
      await expect(productService.getProductById(1))
        .rejects
        .toThrow('Something weird happened');
    });
  });

  /**
   * TEST SUITE: getAllProducts
   */
  describe('getAllProducts', () => {
    it('should fetch all products successfully', async () => {
      // ARRANGE
      axios.get.mockResolvedValueOnce({ data: mockProductList });

      // ACT
      const result = await productService.getAllProducts();

      // ASSERT
      expect(axios.get).toHaveBeenCalledWith('/products');
      expect(result).toEqual(mockProductList);
      expect(result).toHaveLength(4);
      expect(Array.isArray(result)).toBe(true);
    });

    it('should throw error when fetching all products fails', async () => {
      // ARRANGE
      axios.get.mockRejectedValueOnce(new Error('Network error'));

      // ACT & ASSERT
      await expect(productService.getAllProducts())
        .rejects
        .toThrow('Failed to fetch products');
    });
  });
});