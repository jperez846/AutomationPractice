/**
 * Mock Data for Tests
 * Centralized test data makes tests more maintainable
 * If the data structure changes, you only update it here
 */

/**
 * Mock product that exists in the system
 */
export const mockProduct = {
    id: 1,
    name: 'Test Product',
    description: 'A great test product',
    price: 29.99,
  };
  
  /**
   * Mock product with minimal data
   */
  export const mockProductMinimal = {
    id: 2,
    name: 'Minimal Product',
    description: null,
    price: 0,
  };
  
  /**
   * Mock product with all fields populated
   */
  export const mockProductFull = {
    id: 3,
    name: 'Premium Product',
    description: 'The best product money can buy with a very long description that goes on and on',
    price: 999.99,
  };
  
  /**
   * Array of mock products for list testing
   */
  export const mockProductList = [
    mockProduct,
    mockProductMinimal,
    mockProductFull,
    {
      id: 4,
      name: 'Fourth Product',
      description: 'Another product',
      price: 49.99,
    },
  ];
  
  /**
   * Mock error responses
   */
  export const mockErrors = {
    notFound: {
      response: {
        status: 404,
        data: { message: 'Product not found' },
      },
    },
    serverError: {
      response: {
        status: 500,
        data: { message: 'Internal server error' },
      },
    },
    networkError: {
      request: {},
      message: 'Network Error',
    },
  };