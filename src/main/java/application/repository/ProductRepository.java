package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import application.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) layer resonsible for database operations
 * Spring automatically translates persistence exceptions into Spring's Data Exception Hierarchy
 */
@Repository //Stereotype annotation, just defines the components role for the persistence layer
/*
When you extend JpaRepository : you automatically get these methods without writing any code :
// Save operations
productRepository.save(product);
productRepository.saveAll(listOfProducts);

// Find operations
productRepository.findById(1L);
productRepository.findAll();
productRepository.existsById(1L);
productRepository.count();

// Delete operations
productRepository.deleteById(1L);
productRepository.delete(product);
productRepository.deleteAll();
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find by exact name
    Optional<Product> findByName(String name);

    // Find products cheaper than a price
    List<Product> findByPriceLessThan(Double maxPrice);

    // Find products more expensive than a price
    List<Product> findByPriceGreaterThan(Double price);

    // Find products within a price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find by name containing a string (case-insensitive search)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Find products and sort by price
    List<Product> findAllByOrderByPriceAsc();

    // Find by multiple conditions
    List<Product> findByNameAndPriceLessThan(String name, Double maxPrice);


}
