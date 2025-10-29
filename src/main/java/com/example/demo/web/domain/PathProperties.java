package com.example.demo.web.domain;

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
