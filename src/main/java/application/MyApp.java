package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// the spring boot application annotation marks the class as a spring boot application and enables auto-configuration
// and component scanning
@SpringBootApplication
public class MyApp {
    public static void main(String[] args){
        SpringApplication.run(MyApp.class, args);

    }

}
