package softserve.academy.simplecrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns(
                                "http://localhost:3000",
                                "https://your-frontend.example.com")
                        .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                        .allowedHeaders("Authorization","Content-Type")
                        .allowCredentials(true)
                        .maxAge(3600);

            }
        };
    }
}
