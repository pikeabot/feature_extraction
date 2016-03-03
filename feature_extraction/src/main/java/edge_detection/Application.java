package edge_detection;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@SpringBootApplication
public class Application {
    
	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}
	
}
