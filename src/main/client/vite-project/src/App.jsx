import { useState } from 'react';
import ProductDetail from './components/ProductDetail';
import './App.css';

function App() {
  // State to hold the product ID input
  const [productId, setProductId] = useState('');

  // State to hold the ID we want to fetch
  const [searchId, setSearchId] = useState(null);

  // Handle form submission
  const handleSearch = (e) => {
    e.preventDefault(); // Prevent page reload

    // Convert input to number and set as search ID
    const id = parseInt(productId, 10);
    if (!isNaN(id) && id > 0) {
      setSearchId(id);
    }
  };

  return (
    <div className="App">
      <h1>Product Finder</h1>

      {/* Search form */}
      <form onSubmit={handleSearch}>
        <input
          type="number"
          placeholder="Enter Product ID"
          value={productId}
          onChange={(e) => setProductId(e.target.value)}
          min="1"
        />
        <button type="submit">Search</button>
      </form>

      {/* Display product details if we have a search ID */}
      {searchId && <ProductDetail productId={searchId} />}
    </div>
  );
}

export default App;