package com.apirest.apirest.service;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {
    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("/templates/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setSuffix(".html");
        // emailTemplateResolver.setTemplateMode("XHTML");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);

        return emailTemplateResolver;
    }
    @Bean
    public TemplateEngine emailTemplateEngine(ClassLoaderTemplateResolver templateResolver ){
        TemplateEngine templateEngine=new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
