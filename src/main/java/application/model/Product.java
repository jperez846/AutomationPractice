package application.model;

// these imports mark this class as a JPA entity (Java Persistence API)
// mapping it directly to a database table in a relational database

import jakarta.persistence.*;

/**
 * Represents the product table in the database
 * This should ideally not be exposed directly to the client
 */

@Entity // Marks this class as a JPA entity, mapped to a database table
@Table(name = "product") // explicitly define the table
public class Product {
    @Id //specifies the primary key of the entity
    //Auto generate this field
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Double price;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
