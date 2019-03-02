package juniter.service.gva;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Rest [POST] entry point for GraphQL
 *
 * @author ben
 */
@ConditionalOnExpression("${juniter.useGVA:false}")
@RestController
public class GVARestEntryPoint  {
    private static final Logger LOG = LogManager.getLogger();

    private GraphQL graphQL;

    @Autowired
    private ObjectMapper objectMapper;

    public GVARestEntryPoint(GVABlockService bService, TransactionService tService, WoTService wService) {

        // configure
        final GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages("juniter.graphql")
                .withResolverBuilders(new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(new Ticker())
                .withOperationsFromSingleton(bService, GVABlockService.class)
                .withOperationsFromSingleton(tService, TransactionService.class)
                .withOperationsFromSingleton(wService, WoTService.class)
                //.withOperationsFromSingleton(wMutate, WoTMutation.class)
                .withValueMapperFactory(new JacksonValueMapperFactory())

                .generate();

        LOG.info("Initializing GVA controller supportMutation? " + schema.isSupportingMutations()
                + "   supportSubscription? " + schema.isSupportingSubscriptions());
        graphQL = GraphQL.newGraphQL(schema)
                .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy())
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }



    public static class Ticker {



        @GraphQLSubscription(name = "tictac")
        public Publisher<Integer> tick() {
            Observable<Integer> observable = Observable.create(emitter -> {
                emitter.onNext(1);
                Thread.sleep(1000);
                emitter.onNext(2);
                Thread.sleep(1000);
                emitter.onComplete();
            });

            return observable.toFlowable(BackpressureStrategy.BUFFER);
        }
    }


    /**
     * The entry point handling all GraphQL requests
     *
     * @param request
     * @param raw
     * @return
     */
   // @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/graphql",
            //headers = "*",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> graphql(@RequestBody Map<String, Object> request, HttpServletRequest raw) {
        LOG.info("[POST] /graphql ");
        LOG.debug(" - " + request.get("query"));

        String op = request.get("operationName")!=null?request.get("operationName").toString():"";

        final var executionResult = graphQL.execute(
                ExecutionInput.newExecutionInput()
                        .query(request.get("query").toString())
                        .operationName(op)
                        .variables(GraphQLHelper.getVariables(request, objectMapper))
                        .context(raw)
                        .build());
        return executionResult.toSpecification();
    }


}