package com.meawallet.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.meawallet.weather.message.store.SwaggerMessageStore.API_INFO_DESCRIPTION;
import static com.meawallet.weather.message.store.SwaggerMessageStore.API_INFO_TITLE;
import static com.meawallet.weather.message.store.SwaggerMessageStore.API_INFO_VERSION;
import static com.meawallet.weather.message.store.SwaggerMessageStore.WEATHER_CONTROLLER_TAG_DESCRIPTION;
import static com.meawallet.weather.message.store.SwaggerMessageStore.WEATHER_CONTROLLER_TAG_NAME;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.meawallet.weather"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
        return appendTags(docket);
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(WEATHER_CONTROLLER_TAG_NAME, WEATHER_CONTROLLER_TAG_DESCRIPTION)
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_INFO_TITLE)
                .description(API_INFO_DESCRIPTION)
                .version(API_INFO_VERSION)
                .build();
    }

}
