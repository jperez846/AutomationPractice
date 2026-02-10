package application.controller;


import application.model.ProductResponseDTO;
import application.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Handles incoming HTTP requests and returns HTTP responses.
 * Delegates all business operations to the ProductService.
 */
@RestController // combines @Controller and @Response body returns JSON/XML directly
@RequestMapping("/api/products") // base path for all endpoints in this controller
public class ProductController {
    private final ProductService productService;

    @Autowired // Inject the ProductService dependency
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to retrieve a product by ID.
     * Handles HTTP concerns (GET verb, URL mapping, status codes).
     */
    @GetMapping("/{id}") // Maps HTTP GET requests to /api/products/{id}
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        System.out.println("was able to enter in this piece of logic");
        // Call service layer to handle logic
        Optional<ProductResponseDTO> dto = productService.getProductById(id);

        // Return appropriate HTTP response (200 OK or 404 Not Found)
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
