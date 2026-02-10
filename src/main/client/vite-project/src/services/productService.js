import axios from '../api/axiosConfig';
/**
* Service layer for product-related API calls
* All HTTP communication with the Spring Boot backend goes through here

*/
const productService = {
/**
* Fetch a product by ID
* @param {number} id - The product ID
* @returns {Promise<Object>} - the product data (Product Response DTO)
* throws {Error} - if request fails
*/
    getProductById: async (id) => {
        try {
          // axios automatically:
          // - Adds baseURL: /api
          // - Parses JSON response
          // - Throws error for non-2xx status codes
          const response = await axios.get(`/products/${id}`);
           // response.data contains the parsed JSON (ProductResponseDTO)
           // No need to call .json() like with fetch!
          return response.data;
        }
        catch(error) {
        // enhanced error handling with axios
            if(error.response) {
                //server responded with error status
                const status = error.response.status;
                const message = error.response.data?.message || error.message;
                if(status === 404) {
                    throw new Error(`Product with ID ${id} not found`);

                }
                else if(status === 500) {
                     throw new Error('Server error - please try again later');
                }
                else {
                      throw new Error(message || 'Failed to fetch product');
                }
            }
            else if(error.request) {
            // No response received (network error)
                throw new Error('Network error - please check your connection');
            }
            else {
                    // Other errors
                      throw new Error(error.message || 'An unexpected error occurred');
            }

        }
   },


/**
   * Fetch all products
   * @returns {Promise<Array>} - Array of products
   * @throws {Error} - If request fails
   */
  getAllProducts: async () => {
    try {
      const response = await axios.get('/products');
      return response.data;
    } catch (error) {
      console.error('Error fetching all products:', error);
      throw new Error('Failed to fetch products');
    }
  },
  /**
     * Create a new product
     * @param {Object} productData - Product data to create
     * @returns {Promise<Object>} - Created product
     * @throws {Error} - If request fails
     */
    createProduct: async (productData) => {
      try {
        // axios automatically stringifies the object to JSON
        const response = await axios.post('/products', productData);
        return response.data;
      } catch (error) {
        console.error('Error creating product:', error);

        if (error.response?.status === 400) {
          // Validation error
          throw new Error(error.response.data?.message || 'Invalid product data');
        }
        throw new Error('Failed to create product');
      }
    },

    /**
     * Update an existing product
     * @param {number} id - Product ID
     * @param {Object} productData - Updated product data
     * @returns {Promise<Object>} - Updated product
     * @throws {Error} - If request fails
     */
    updateProduct: async (id, productData) => {
      try {
        const response = await axios.put(`/products/${id}`, productData);
        return response.data;
      } catch (error) {
        console.error('Error updating product:', error);

        if (error.response?.status === 404) {
          throw new Error(`Product with ID ${id} not found`);
        }
        throw new Error('Failed to update product');
      }
    },

    /**
     * Delete a product
     * @param {number} id - Product ID to delete
     * @returns {Promise<void>}
     * @throws {Error} - If request fails
     */
    deleteProduct: async (id) => {
      try {
        await axios.delete(`/products/${id}`);
        // No return data needed for delete
      } catch (error) {
        console.error('Error deleting product:', error);

        if (error.response?.status === 404) {
          throw new Error(`Product with ID ${id} not found`);
        }
        throw new Error('Failed to delete product');
      }
    },

    /**
     * Search products by name
     * @param {string} searchTerm - Search term
     * @returns {Promise<Array>} - Matching products
     * @throws {Error} - If request fails
     */
    searchProducts: async (searchTerm) => {
      try {
        // Axios automatically handles URL encoding of params
        const response = await axios.get('/products/search', {
          params: {
            name: searchTerm
          }
        });
        return response.data;
      } catch (error) {
        console.error('Error searching products:', error);
        throw new Error('Failed to search products');
      }
    },

    /**
     * Get products by price range
     * @param {number} minPrice - Minimum price
     * @param {number} maxPrice - Maximum price
     * @returns {Promise<Array>} - Products in range
     * @throws {Error} - If request fails
     */
    getProductsByPriceRange: async (minPrice, maxPrice) => {
      try {
        const response = await axios.get('/products/price-range', {
          params: {
            min: minPrice,
            max: maxPrice
          }
        });
        return response.data;
      } catch (error) {
        console.error('Error fetching products by price range:', error);
        throw new Error('Failed to fetch products');
      }
    },

};

export default productService;
