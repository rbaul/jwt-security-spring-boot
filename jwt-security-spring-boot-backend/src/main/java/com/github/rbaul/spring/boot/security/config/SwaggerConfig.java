package com.github.rbaul.spring.boot.security.config;

import com.github.rbaul.spring.boot.security.web.controllers.ProductController;
import com.github.rbaul.spring.boot.security.web.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String AUTHORIZATION_TOKEN_REFERENCE = "Authorization-Token";

    @Value("${application.formatted-version:development}")
    private String version;

    @Value("${application.title:demo-server}")
    private String title;

    @Value("${application.baseIp:localhost:8080}")
    private String baseIp;

    @Autowired
    private JwtSecurityProperties jwtSecurityProperties;

    @Bean
    public Docket securityApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Security API")
                .enable(true)
                .enableUrlTemplating(true)
                .host(baseIp)
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(UserController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Product API")
                .enable(true)
                .enableUrlTemplating(true)
                .host(baseIp)
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ProductController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .version(version).build();
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_TOKEN_REFERENCE, jwtSecurityProperties.getToken().getHeaderName(), ApiKeyVehicle.HEADER.getValue());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authScopes = new AuthorizationScope[1];
        authScopes[0] = new AuthorizationScopeBuilder()
                .scope("global")
                .description("full access").build();
        return Collections.singletonList(SecurityReference.builder()
                .reference(AUTHORIZATION_TOKEN_REFERENCE)
                .scopes(authScopes).build());
    }

}