package co.edu.uptc.edamicrokafka.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class EdamicrokafkaGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdamicrokafkaGatewayApplication.class, args);
	}

}
