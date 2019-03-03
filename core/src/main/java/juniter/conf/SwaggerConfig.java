package juniter.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static juniter.conf.StaticHTMLConfig.CLASSPATH_RESOURCE_LOCATIONS;

//@EnableWebMvc
//@EnableSwagger //Enable swagger 1.2 spec
//@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
//@ComponentScan(basePackageClasses = BlockchainController.class)
//@Import(Swagger2DocumentationConfiguration.class)



@EnableSwagger2
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket actuator() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("monitoring")
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
                .groupName("juniter")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        List.of(new ResponseMessageBuilder().code(500)
                                        .message("500 message")
                                        .responseModel(new ModelRef("Error"))
                                        .build(),
                                new ResponseMessageBuilder().code(403)
                                        .message("Forbidden!!!!!")
                                        .build()))

                ;
    }


    @Bean
    public Docket juniter2() {


        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("juniter2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.bma"))
                .paths(PathSelectors.regex("/blockchain.*"))
                .build()
                .apiInfo(metaData())
                ;
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Juniter BMA API")
                .description("REST API (BMA) for Juniter")
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

