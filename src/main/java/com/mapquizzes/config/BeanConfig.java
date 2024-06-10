package com.mapquizzes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RequestPredicates.pathExtension;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class BeanConfig {

    @Bean
    public RouterFunction<ServerResponse> spaRouter() {
        ClassPathResource index = new ClassPathResource("static/pages/index.html");
        List<String> extensions = Arrays.asList("js", "css", "html", "ico", "png", "jpg", "gif");
        RequestPredicate spaPredicate = path("/api/**").or(path("/error")).or(pathExtension(extensions::contains)).negate();
        return route().resource(spaPredicate, index).build();
    }
}
