package dev.abpira.sct;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "SCT service REST API Documentation",
                description = "Supply Chain Trace Documentation",
                contact = @Contact(
                        name = "Ali Bostanpira",
                        email = "alibostanpira@gmail.com",
                        url = "https://github.com/alibostanpira"
                )
        )
)
public class SupplyChainTraceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplyChainTraceApplication.class, args);
    }
}
