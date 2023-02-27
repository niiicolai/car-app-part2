package dat3.car.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperConfig {
    // På grund af en fejlbesked om at ObjectMapper ikke understøtter LocalDateTime,
    // så har jeg lavet nedenstående bean, som laver en objectmapper,
    // der loader et modul der gør ObjectMapper understøtter LocalDateTime.
    @Bean
    public ObjectMapper objectMapper() {        
        // https://github.com/FasterXML/jackson-modules-java8
		// auto-discover modules to support LocalDateTime 
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

        return objectMapper;
    }
}
