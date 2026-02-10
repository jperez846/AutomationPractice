package application.model;

/**
 * Data Transfer Object (DTO) for safely exposing product data via the API
 */
public record ProductResponseDTO (
    Long id,
    String name,
    String description,
    Double price
){}
// records automatically provide getters and setters, are immutable, constructor, equals, hashcode and to string methods
