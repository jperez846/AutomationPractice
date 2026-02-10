package application.service;

import application.model.ProductResponseDTO;
import application.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import application.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Business layer where core logic, validation, and transaction management occur
 * The controller calls the service. The service calls the repository
 */

@Service // Marks this class as a service component, managed by spring
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    // Inject the ProductRepository dependency, there is the option to do field injection but constructor injection is preferred
    //What does auto wired do though ?
    /*
    Using autowired means that spring automatically provides the repository instance
    Benefits:
   - Loose coupling - less dependent on other modules, services, components
   - Easier to test
   - Spring manages the object lifecyle

     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves a product by its ID and converts it to a DTO.
     * Contains business logic (e.g., checking for existence, potential discounts).
     */
    /* The transactional annotation in spring boot demaracates a method or class as requiring transactional execution
    ensuring that a set of database operations are treated as a single unit of work.
    Benefits:
    Atomicity (All or nothing): All database operations within the annotatd method succeed together or fail together. If any operation fails
    the entire transaction is rolled back to its state before the transaction began, preventing data inconsistency.
    Automatic Management: Springs transaction manager automatically opens a transaction when the annotated method is called
    and commits it after successful completion
        By default, the transaction automatically rolls back if an unchecked exception (a RuntimeException or an Error) is thrown .
        If a checked exception is thrown, the transaction is committed by default, unless you configure the annotation otherwise .
    Connection Management: It manages the database connection lifecycle, ensuring that the connection is closed or returned to the connection pool
    after the transaction is complete. (No db leaks anymore :) )


     */
    @Transactional(readOnly = true) // Optimize transaction for read-only operation
    public Optional<ProductResponseDTO> getProductById(Long id) {
        // optional just means it may be a null or not null container
        Optional<Product> productEntity = productRepository.findById(id); // Fetch entity from DB
        // once we have the product entity container we can transform it only if it exists, this is a functional transformation not a data structure :)

        // Business Logic: If product found, map to DTO
        // here we are using java referencing, It provides a concise way to refer to an existing method without executing it immediately
        // In this context we are using as a short hand for the lambda expression productEntity.map(product -> this.convertToDto(product))
        return productEntity.map(this::convertToDto);
    }

    // A simple helper method to map Entity to DTO (real apps might use MapStruct/ModelMapper)
    private ProductResponseDTO convertToDto(Product product) {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }
}
