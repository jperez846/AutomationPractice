package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * REST Assured API Tests for Product API
 *
 * REST Assured Structure (Given-When-Then):
 * - GIVEN: Set up preconditions (headers, auth, params, body)
 * - WHEN: Perform the action (GET, POST, PUT, DELETE)
 * - THEN: Verify the response (status code, body, headers)
 *
 * IMPORTANT: Your Spring Boot application MUST be running for these tests to work!
 * These are INTEGRATION tests, not unit tests.
 */
public class ProductApiTests {
    private static RequestSpecification requestSpec;
    String URI = "http://localhost:8080";
    String BASE_PATH = "/api/products";

    /**
     * @BeforeClass runs ONCE before all tests in this class
     * Sets up common configuration for all tests
     */
    @BeforeClass
    public void setup(){
        /**
         * RequestSpecBuilder: Builder pattern for creating request specifications
         * Centralizes common request settings
         */
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(URI);
        builder.setBasePath(BASE_PATH);
        //Set content type for all requests
        builder.setContentType(ContentType.JSON);
        // Accept JSON responses
        builder.setAccept(ContentType.JSON);
        // Enable request/response logging (helpful for debugging)
        // builder.addFilter(new RequestLoggingFilter());
        // builder.addFilter(new ResponseLoggingFilter());


        requestSpec = builder.build();
        /**
         * Alternative: Set global defaults (affects ALL tests in the project)
         * RestAssured.baseURI = BASE_URI;
         * RestAssured.basePath = BASE_PATH;
         */
    }

    @Test
    public void testGetProduct_WhenProductExists_ShouldReturn200(){
        /**
         * REST Assured follows the Given-When-Then pattern:
         *
         * given() - Set up the request (spec, headers, params)
         * when() - Perform the HTTP action (GET, POST, etc.)
         * then() - Validate the response (status, body, headers)
         */
        Response response = RestAssured.given().spec(requestSpec).when().get("/102").then().extract().response();

        System.out.println("im right hereeeee");
        Assert.assertEquals(response.statusCode(), 200);
        //Assert.assertNotNull(response.jsonPath().get("data.id"));
    }
}
