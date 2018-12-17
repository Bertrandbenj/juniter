package juniter.service.gva;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import juniter.service.gva.tx.TransactionService;
import juniter.service.gva.wot.WoTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
 *
 */
@ConditionalOnExpression("${juniter.useGVA:false}")
@RestController
public class GVARestEntryPoint {
	private static final Logger LOG = LoggerFactory.getLogger(GVARestEntryPoint.class);

	private final GraphQL graphQL;

	public GVARestEntryPoint(BlockService bService, TransactionService tService,  WoTService wService) {

		final GraphQLSchema schema = new GraphQLSchemaGenerator()
				.withBasePackages("juniter.graphql")
				.withResolverBuilders(new AnnotatedResolverBuilder())
				.withOperationsFromSingleton(bService, BlockService.class)
				.withOperationsFromSingleton(tService, TransactionService.class)
				.withOperationsFromSingleton(wService, WoTService.class)
				//.withOperationsFromSingleton(wMutate, WoTMutation.class)
				.withValueMapperFactory(new JacksonValueMapperFactory())

				.generate();

		LOG.info("Initializing GVA controller supportMutation?" + schema.isSupportingMutations()
		+" supportSUbscription?" + schema.isSupportingSubscriptions());
		graphQL = GraphQL.newGraphQL(schema).build();
	}

	@PostMapping(value = "/graphql", //
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Map<String, Object> graphql(@RequestBody Map<String, String> request, HttpServletRequest raw) {
		LOG.info("[POST] /graphql ");
		LOG.debug(" - " + request.get("query"));

		final var executionResult = graphQL.execute(//
				ExecutionInput.newExecutionInput() //
						.query(request.get("query")) //
						.operationName(request.get("operationName")) //
						.context(raw)//
						.build());
		return executionResult.toSpecification();
	}
}