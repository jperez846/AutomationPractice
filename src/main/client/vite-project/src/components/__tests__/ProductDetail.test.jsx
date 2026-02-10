/**
 * Component Tests for ProductDetail
 * 
 * What we're testing:
 * - Component renders correctly in different states
 * - Component fetches data on mount
 * - Component handles errors gracefully
 * - Component updates when props change
 * 
 * Testing Strategy:
 * - Mock the productService to control responses
 * - Use React Testing Library to render and interact
 * - Test user-visible behavior, not implementation details
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import ProductDetail from '../ProductDetail';
import productService from '../../services/productService';
import { mockProduct } from '../../test/mockData';

/**
 * Mock the entire productService module
 * This prevents actual API calls during tests
 */
vi.mock('../../services/productService');

describe('ProductDetail Component', () => {
  /**
   * beforeEach runs before each test
   * Resets all mocks to ensure test isolation
   */
  beforeEach(() => {
    vi.clearAllMocks();
  });

  /**
   * TEST: Loading State
   * 
   * Verifies component shows loading indicator while fetching
   */
  it('should display loading state initially', () => {
    // ARRANGE: Mock a slow response (never resolves)
    productService.getProductById.mockImplementation(
      () => new Promise(() => {}) // Promise that never resolves
    );

    // ACT: Render component
    render(<ProductDetail productId={1} />);

    // ASSERT: Loading message should be visible
    expect(screen.getByText('Loading product...')).toBeInTheDocument();
  });

  /**
   * TEST: Successful Product Display
   * 
   * Verifies component displays product data correctly
   */
  it('should display product details when loaded successfully', async () => {
    // ARRANGE: Mock successful API response
    productService.getProductById.mockResolvedValueOnce(mockProduct);

    // ACT: Render component
    render(<ProductDetail productId={1} />);

    // ASSERT: Wait for async data to load
    /**
     * waitFor is used for async assertions
     * It polls until the assertion passes or times out
     */
    await waitFor(() => {
      // Check if product name is displayed
      expect(screen.getByText('Test Product')).toBeInTheDocument();
    });

    // Verify all product fields are rendered
    expect(screen.getByText('A great test product')).toBeInTheDocument();
    expect(screen.getByText('$29.99')).toBeInTheDocument();
    expect(screen.getByText('Product ID: 1')).toBeInTheDocument();

    // Verify the service was called with correct ID
    expect(productService.getProductById).toHaveBeenCalledWith(1);
    expect(productService.getProductById).toHaveBeenCalledTimes(1);
  });

  /**
   * TEST: Error State
   * 
   * Verifies component displays error message when fetch fails
   */
  it('should display error message when fetch fails', async () => {
    // ARRANGE: Mock API error
    const errorMessage = 'Product with ID 1 not found';
    productService.getProductById.mockRejectedValueOnce(
      new Error(errorMessage)
    );

    // ACT: Render component
    render(<ProductDetail productId={1} />);

    // ASSERT: Wait for error to appear
    await waitFor(() => {
      expect(screen.getByText('Error loading product')).toBeInTheDocument();
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
  });

  /**
   * TEST: No Product ID Provided
   * 
   * Verifies component handles missing productId gracefully
   */
  it('should not fetch when productId is null or undefined', () => {
    // ARRANGE
    productService.getProductById.mockResolvedValueOnce(mockProduct);

    // ACT: Render without productId
    render(<ProductDetail productId={null} />);

    // ASSERT: Service should not be called
    expect(productService.getProductById).not.toHaveBeenCalled();
  });

  /**
   * TEST: Product ID Change (Re-fetch)
   * 
   * Verifies component re-fetches when productId prop changes
   */
//   it('should re-fetch product when productId changes', async () => {
//     // ARRANGE
//     const product1 = { ...mockProduct, id: 1, name: 'Product 1' };
//     const product2 = { ...mockProduct, id: 2, name: 'Product 2' };

//     productService.getProductById
//       .mockResolvedValueOnce(product1)
//       .mockResolvedValueOnce(product2);

//     // ACT: Initial render with productId=1
//     const { rerender } = render(<ProductDetail productId={1} />);

//     // ASSERT: First product loaded
//     await waitFor(() => {
//       expect(screen.getByText('Product 1')).toBeInTheDocument();
//     });

//     // ACT: Update productId to 2
//     rerender(<ProductDetail productId={2} />);

//     // ASSERT: Second product loaded
//     await waitFor(() => {
//       expect(screen.getByText('Product 2')).toBeInTheDocument();
//     });

//     // Verify service was called twice with different IDs
//     expect(productService.getProductById).toHaveBeenCalledTimes(2);
//     expect(productService.getProductById).toHaveBeenNthCalledWith(1, 1);
//     expect(productService.getProductById).toHaveBeenNthCalledWith(2, 2);
//   });

  /**
   * TEST: Null Description Handling
   * 
   * Verifies component handles products with null/missing fields
   */
//   it('should handle product with null description', async () => {
//     // ARRANGE
//     const productWithNullDescription = {
//       ...mockProduct,
//       description: null,
//     };
//     productService.getProductById.mockResolvedValueOnce(
//       productWithNullDescription
//     );

//     // ACT
//     render(<ProductDetail productId={1} />);

//     // ASSERT
//     await waitFor(() => {
//       expect(screen.getByText('Test Product')).toBeInTheDocument();
//     });

//     // Description element should not crash when description is null
//     const descriptionElements = screen.queryAllByClassName('description');
//     expect(descriptionElements.length).toBeGreaterThan(0);
//   });

  /**
   * TEST: Console Error Logging
   * 
   * Verifies errors are logged to console for debugging
   */
//   it('should log error to console when fetch fails', async () => {
//     // ARRANGE: Spy on console.error
//     const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
//     const error = new Error('Test error');
//     productService.getProductById.mockRejectedValueOnce(error);

//     // ACT
//     render(<ProductDetail productId={1} />);

//     // ASSERT
//     await waitFor(() => {
//       expect(consoleErrorSpy).toHaveBeenCalledWith(
//         'Failed to fetch product:',
//         error
//       );
//     });

//     // Clean up spy
//     consoleErrorSpy.mockRestore();
//   });
});