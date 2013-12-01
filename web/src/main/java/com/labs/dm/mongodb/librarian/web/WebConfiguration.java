/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.web;

import com.labs.dm.mongodb.librarian.web.controller.HomeController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author daniel
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfiguration.class)
//@EnableWebMvc
public class WebConfiguration extends WebMvcConfigurerAdapter {

    //<mvc:annotation-driven content-negotiation-manager="contentNegotiationManager" />
    /*@Bean
     public ContentNegotiationManagerFactoryBean bean() {
     ContentNegotiationManagerFactoryBean bean = new ContentNegotiationManagerFactoryBean();
     bean.setFavorPathExtension(false);
     return bean;
     }*/
    @Bean
    public ViewResolver resolver() {
        UrlBasedViewResolver url = new UrlBasedViewResolver();
        url.setPrefix("/WEB-INF/views/");
        url.setViewClass(JstlView.class);
        url.setSuffix(".jsp");
        return url;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public MappingJacksonHttpMessageConverter jacksonMessageConverter() {
        return new MappingJacksonHttpMessageConverter();
    }

    @Bean
    public AnnotationMethodHandlerAdapter AnnotationMethodHandlerAdapter() {
        AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();
        adapter.setMessageConverters(new HttpMessageConverter<?>[]{jacksonMessageConverter()});
        return adapter;
    }
}
