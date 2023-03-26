package com.fadlurahmanf.starter.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;
import com.google.common.base.Predicate;

@Configuration
public class SpringFoxConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Starter App API")
                .description("Keperluan Belajar")
                .contact(new Contact("Taufik Fadlurahman Fajari", "www.linkedin.com/in/tffajari", "tffajari@gmail.com"))
                .license("Taufik Fadlurahman Fajari License")
                .version("1.0").build();
    }
}
