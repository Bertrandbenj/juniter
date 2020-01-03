package juniter.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
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
        return new Docket(DocumentationType.SPRING_WEB)
                .groupName("Technical monitoring")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/actuator.*"))
                .build()
                .apiInfo(metaData()
                        .title("Technical monitoring")
                        .description("Prometheus at http://localhost:9090/graph?g0.range_input=1h&g0.expr=process_cpu_usage&g0.tab=0")
                        .build())
                ;
    }

    @Bean
    public Docket rdf() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API - RDF")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.rdf"))
                .paths(PathSelectors.regex("/jena.*"))
                .build()
                .apiInfo(metaData()
                        .title("RDF api using Jena")
                        .version("0.0.1")
                        .description("Ex : run https://localhost:8443/jena/rdf/ontology/dbo  then load it into  http://www.visualdataweb.de/webvowl/# \n <br/>http://localhost:8000/#juniter_dbo<br/>http://localhost:8000/#bindex")
                        .build());
    }

    @Bean
    public Docket web() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Juniter's extra")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData()
                        .title("Juniter's extra")
                        .description("Ex graphviz https://localhost:8443/graphviz/svg/block/5")
                        .build());
    }

    @Bean
    public Docket websocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API - WS2P")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.ws2p"))
                .paths(PathSelectors.none())
                .build()
                .apiInfo(metaData()
                        .title("WS2P - WebSocket Duniter API")
                        .version("1.0.0")
                        .description(" for more developments tools, also visit https://localhost:8443/ws.html")
                        .build())
                ;
    }

    @Bean
    public Docket gva() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API - GVA")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.gva"))
                .paths(PathSelectors.regex("/graph.*"))
                .build()
                .apiInfo(metaData()
                        .title("GVA - Graphql access point")
                        .version("0.9.0")
                        .description("for more developments tools, also visit https://localhost:8443/gvasubs.html,  https://localhost:8443/graphiql").build())

                ;
    }


    @Bean
    public Docket duniterBMA() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("API - BMA")
                .select()
                .apis(RequestHandlerSelectors.basePackage("juniter.service.bma"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData()
                        .title("BMA - REST Duniter Protocol")
                        .version("1.0.0")
                        .build());
        //                .useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.GET,
//                        List.of(new ResponseMessageBuilder().code(500)
//                                        .message("500 message")
//                                        .responseModel(new ModelRef("Error"))
//                                        .build(),
//                                new ResponseMessageBuilder().code(403)
//                                        .message("Forbidden!!!!!")
//                                        .build()))
    }

    private ApiInfoBuilder metaData() {
        return new ApiInfoBuilder()
                //.title("Juniter API")
                //.description("Juniter API ")
                //.version("1.0")
                .termsOfServiceUrl("terms of service")
                .license("WTF PL")
                .licenseUrl("http://www.wtfpl.net/")
                .contact(new Contact("Benjamin Bertrand", "https://github.com/Bertrandbenj/juniter", "bertrandbenj@gmail.com"))
                ;
    }

    /**
     * {{@See }}
     *
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

