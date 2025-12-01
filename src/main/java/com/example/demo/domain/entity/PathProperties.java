package com.example.demo.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("path")
public class PathProperties {
    private String usbDir;
    private String eventDir;

}
