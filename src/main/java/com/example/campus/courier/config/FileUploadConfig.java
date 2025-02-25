package com.example.campus.courier.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射，使用绝对路径
        String path = System.getProperty("user.dir") + "/static/images/";
        registry.addResourceHandler("/static/images/**")
                .addResourceLocations("file:" + path);
    }
} 