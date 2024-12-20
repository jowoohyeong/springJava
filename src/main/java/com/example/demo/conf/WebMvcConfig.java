package com.example.demo.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${server.resources.location}")
    private String location;

    @Value("${server.resources.uri-path}")
    private String uriPath;

    @Value("${server.resources.osType}")
    private String osType;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if(osType.equalsIgnoreCase("window")) {
            registry.addResourceHandler(uriPath + "/**")
            .addResourceLocations("file:///" + location);

        } else {
            registry.addResourceHandler(uriPath + "/**")
            .addResourceLocations("file:" + location);
        }

    }
}
