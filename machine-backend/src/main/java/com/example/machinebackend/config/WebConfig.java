package com.example.machinebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射URL路径 /upload/** 到配置文件中指定的物理路径
        // 动态地处理相对路径和绝对路径
        String resourceLocation = new File(uploadDir).getAbsolutePath();
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + resourceLocation + File.separator);
    }
} 