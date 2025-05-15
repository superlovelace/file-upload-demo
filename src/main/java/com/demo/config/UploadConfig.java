package com.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author peter
 */
@Configuration
public class UploadConfig {

    public static String PATH;

    @Value("${upload.path}")
    public void setPath(String path) {
        UploadConfig.PATH = path;
    }

}
