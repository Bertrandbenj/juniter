package juniter.service.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import juniter.service.async.SimpleLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.coxautodev.graphql.tools.SchemaParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ConditionalOnExpression("${juniter.graphql.enabled:false}")
@RestController
public class GraphQLController {
	private static final Logger logger = LoggerFactory.getLogger(GraphQLController.class);

    private final GraphQL graphQL;
//    
//    SchemaParser sc = SchemaParser.newParser()
//    .file("my-schema.graphqls")

    public GraphQLController(CarService carService) {
//        GraphQLSchema schema = new GraphQLSchemaGenerator()
//                .withResolverBuilders(
//                        //Resolve by annotations
//                        new AnnotatedResolverBuilder())
//                .withOperationsFromSingleton(carService)
//                .withValueMapperFactory(new JacksonValueMapperFactory())
//                .generate();
//        graphQL = GraphQL.newGraphQL(schema).build();
    	GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withResolverBuilders(
                        //Resolve by annotations
                        new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(carService)
                .withValueMapperFactory(new JacksonValueMapperFactory())
                .generate();
graphQL = GraphQL.newGraphQL(schema).build();
    }

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> graphql(@RequestBody Map<String, String> request, HttpServletRequest raw) {
       logger.info("[POST] /graphql "+request.get("query"));
    	
    	ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(request.get("query"))
                .operationName(request.get("operationName"))
                .context(raw)
                .build());
        return executionResult.toSpecification();
    }
}