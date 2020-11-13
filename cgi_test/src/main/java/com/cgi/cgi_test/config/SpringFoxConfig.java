package com.cgi.cgi_test.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    public static final String STRING="string";
    public static final String HEADER="header";
    @Bean
    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfoBuilder()
                        .title("CGI Bank Accounts Services")
                        .description("Provides APIS for CGI Bank Accounts Services")
                        .version("0.1.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(("com.cgi.cgi_test.api")))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDateTime.class, String.class)
                .globalOperationParameters(getParameters());
    }

    private List<Parameter> getParameters() {
        final ParameterBuilder parameterBuilder = new ParameterBuilder();
        final List<Parameter> parameters = new ArrayList<>();
        parameters.clear();
        parameterBuilder.name("Accept").modelRef(new ModelRef(STRING)).parameterType(HEADER).defaultValue("application/json").required(true).build();
        parameters.add(parameterBuilder.build());
        parameterBuilder.name("Content-Type").modelRef(new ModelRef(STRING)).parameterType(HEADER).defaultValue("application/json").required(true).build();
        parameters.add(parameterBuilder.build());
        return parameters;
    }
}