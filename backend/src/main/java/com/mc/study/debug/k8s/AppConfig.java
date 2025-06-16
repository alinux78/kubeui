package com.mc.study.debug.k8s;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

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
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                converters.add(new MappingJackson2HttpMessageConverter(mapper));
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
