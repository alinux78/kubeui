package com.mc.study.debug.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class AppConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Content-Type", "Authorization")
                        ;
            }
        };
    }

    @Bean
    public String k8sApiConfig() throws IOException {
        //ApiClient client = Config.defaultClient(); // Uses ~/.kube/config
        ApiClient client = Config.fromConfig("/home/guest/.kube/dev.config");
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        return "done";
    }

}
