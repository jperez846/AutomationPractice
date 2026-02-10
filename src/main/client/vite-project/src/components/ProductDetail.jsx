import { useState, useEffect } from 'react';
import productService from '../services/productService';

/**
 * Component to display product details
 * Fetches product data from Spring Boot backend
 */
function ProductDetail({ productId }) {
  // State to hold product data
  const [product, setProduct] = useState(null);

  // State to track loading status
  const [loading, setLoading] = useState(true);

  // State to hold any error messages
  const [error, setError] = useState(null);

  // useEffect runs when component mounts or when productId changes
  useEffect(() => {
    // Function to fetch product data
    const fetchProduct = async () => {
      try {
        setLoading(true); // Show loading state
        setError(null); // Clear any previous errors

        // Call the service to get product data
        const data = await productService.getProductById(productId);

        // Update state with fetched data
        setProduct(data);

      } catch (err) {
        // Handle errors (network issues, 404, etc.)
        setError(err.message);
        console.error('Failed to fetch product:', err);

      } finally {
        // Always set loading to false when done
        setLoading(false);
      }
    };

    // Only fetch if we have a valid productId
    if (productId) {
      fetchProduct();
    }
  }, [productId]); // Re-run effect when productId changes

  // Render loading state
  if (loading) {
    return <div className="loading">Loading product...</div>;
  }

  // Render error state
  if (error) {
    return (
      <div className="error">
        <h3>Error loading product</h3>
        <p>{error}</p>
      </div>
    );
  }

  // Render product not found state
  if (!product) {
    return <div className="not-found">Product not found</div>;
  }

  // Render product details
  return (
    <div className="product-detail">
      <h2>{product.name}</h2>
      <p className="description">{product.description}</p>
      <p className="price">${product.price.toFixed(2)}</p>
      <p className="id">Product ID: {product.id}</p>
    </div>
  );
}

export default ProductDetail;