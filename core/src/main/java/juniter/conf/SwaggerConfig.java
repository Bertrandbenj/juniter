package juniter.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static juniter.conf.StaticHTMLConfig.CLASSPATH_RESOURCE_LOCATIONS;

@EnableSwagger2
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket actuator() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Technical monitoring")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/actuator.*"))
                .build()
                .apiInfo(metaData())
                ;
    }

    @Bean
    public Docket juniter() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Juniter' extras")
                .select()
                .apis(RequestHandlerSelectors.any())//.basePackage("juniter.service.rdf"))
                .paths(PathSelectors.regex("/api.*"))
                //.paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
//                .useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.GET,
//                        List.of(new ResponseMessageBuilder().code(500)
//                                        .message("500 message")
//                                        .responseModel(new ModelRef("Error"))
//                                        .build(),
//                                new ResponseMessageBuilder().code(403)
//                                        .message("Forbidden!!!!!")
//                                        .build()))
                ;
    }


    @Bean
    public Docket duniterBMA() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Duniter BMA")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.bma"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                ;
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Juniter BMA API")
                .description("REST API (BMA) for Juniter, for more developments tools, also visit https://localhost:8443/ws.html, https://localhost:8443/gvasubs.html, https://localhost:8443/graphiql ")
                .version(getClass().getPackage().getSpecificationVersion())
                .license("WTF PL")
                .licenseUrl("http://www.wtfpl.net/")
                .contact(new Contact("Benjamin Bertrand", "https://www.e-is.pro", "benjamin.bertrand@e-is.pro"))

                .build();
    }

    /**
     * {{@See }}
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");


        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**")
                    .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        }
    }


}

