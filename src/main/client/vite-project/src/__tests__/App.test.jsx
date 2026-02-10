/**
 * Integration Tests for App Component
 * 
 * What we're testing:
 * - Form submission works correctly
 * - Product search is triggered
 * - Input validation works
 * - Component integration (App + ProductDetail)
 * 
 * Testing Strategy:
 * - Use user-event to simulate real user interactions
 * - Test the entire user flow from input to display
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from '../App';
import productService from '../services/productService';
import { mockProduct } from '../test/mockData';

vi.mock('../services/productService');

describe('App Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  /**
   * TEST: Initial Render
   * 
   * Verifies app renders with correct initial elements
   */
  it('should render the app with search form', () => {
    // ACT
    render(<App />);

    // ASSERT
    expect(screen.getByText('Product Finder')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Enter Product ID')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Search' })).toBeInTheDocument();
  });

  /**
   * TEST: Form Submission Flow
   * 
   * Verifies entire search flow from input to result display
   */
  it('should search for product when form is submitted', async () => {
    // ARRANGE
    productService.getProductById.mockResolvedValueOnce(mockProduct);
    
    /**
     * userEvent provides realistic user interactions
     * It's better than fireEvent because it simulates actual user behavior
     */
    const user = userEvent.setup();

    // ACT
    render(<App />);

    // Find the input field
    const input = screen.getByPlaceholderText('Enter Product ID');
    
    // Find the search button
    const searchButton = screen.getByRole('button', { name: 'Search' });

    // Simulate user typing in the input
    await user.type(input, '1');
    
    // Verify input value updated
    expect(input).toHaveValue(1);

    // Simulate user clicking search button
    await user.click(searchButton);

    // ASSERT: Product detail should be rendered
    await waitFor(() => {
      expect(screen.getByText('Test Product')).toBeInTheDocument();
    });

    expect(productService.getProductById).toHaveBeenCalledWith(1);
  });

  /**
   * TEST: Invalid Input Handling
   * 
   * Verifies app doesn't search with invalid IDs
   */
  it('should not search with invalid product ID', async () => {
    // ARRANGE
    const user = userEvent.setup();

    // ACT
    render(<App />);

    const input = screen.getByPlaceholderText('Enter Product ID');
    const searchButton = screen.getByRole('button', { name: 'Search' });

    // Try entering negative number
    await user.type(input, '-1');
    await user.click(searchButton);

    // ASSERT: Service should not be called
    expect(productService.getProductById).not.toHaveBeenCalled();
  });

  /**
   * TEST: Empty Input Handling
   * 
   * Verifies app doesn't search with empty input
   */
  it('should not search with empty input', async () => {
    // ARRANGE
    const user = userEvent.setup();

    // ACT
    render(<App />);

    const searchButton = screen.getByRole('button', { name: 'Search' });
    
    // Click search without entering anything
    await user.click(searchButton);

    // ASSERT
    expect(productService.getProductById).not.toHaveBeenCalled();
  });

  /**
   * TEST: Multiple Searches
   * 
   * Verifies app can handle multiple sequential searches
   */
  it('should handle multiple searches', async () => {
    // ARRANGE
    const product1 = { ...mockProduct, id: 1, name: 'Product 1' };
    const product2 = { ...mockProduct, id: 2, name: 'Product 2' };

    productService.getProductById
      .mockResolvedValueOnce(product1)
      .mockResolvedValueOnce(product2);

    const user = userEvent.setup();

    // ACT
    render(<App />);

    const input = screen.getByPlaceholderText('Enter Product ID');
    const searchButton = screen.getByRole('button', { name: 'Search' });

    // First search
    await user.clear(input);
    await user.type(input, '1');
    await user.click(searchButton);

    await waitFor(() => {
      expect(screen.getByText('Product 1')).toBeInTheDocument();
    });

    // Second search
    await user.clear(input);
    await user.type(input, '2');
    await user.click(searchButton);

    await waitFor(() => {
      expect(screen.getByText('Product 2')).toBeInTheDocument();
    });

    // ASSERT
    expect(productService.getProductById).toHaveBeenCalledTimes(2);
  });

  /**
   * TEST: Form Submit with Enter Key
   * 
   * Verifies form submission works with Enter key
   */
  it('should submit form when Enter key is pressed', async () => {
    // ARRANGE
    productService.getProductById.mockResolvedValueOnce(mockProduct);
    const user = userEvent.setup();

    // ACT
    render(<App />);

    const input = screen.getByPlaceholderText('Enter Product ID');
    
    // Type and press Enter
    await user.type(input, '1{Enter}');

    // ASSERT
    await waitFor(() => {
      expect(screen.getByText('Test Product')).toBeInTheDocument();
    });
  });
});