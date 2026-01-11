package backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// --- CORS Configuration (The Fix for Your Error) ---
// This class configures CORS globally for the entire application.
// Place this in a file like 'WebConfig.java' in your configuration package.

@Configuration
class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Applies CORS configuration to all endpoints
                        .allowedOrigins(

                                "http://localhost:22222",
                                "https://app.duylong.art",
                                "https://backend.duylong.art",
                                "http://192.168.22.4:22222")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allows specified HTTP methods
                        .allowedHeaders("*") // Allows all headers;
                        .allowCredentials(true); // Allows cookies and credentials
            }
        };
    }
}
