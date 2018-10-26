package juniter.service.graphql;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;

/**
 * Rest [POST] entry point for GraphQL
 *
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.graphql.enabled:false}")
@RestController
public class GVAController {
	private static final Logger LOG = LoggerFactory.getLogger(GVAController.class);

	private final GraphQL graphQL;

	public GVAController(BlockService bService, GQLTxService tService) {

		final GraphQLSchema schema = new GraphQLSchemaGenerator() //
				.withBasePackages("juniter.graphql").withResolverBuilders(new AnnotatedResolverBuilder()) //
				.withOperationsFromSingleton(bService, BlockService.class) //
				.withOperationsFromSingleton(tService, GQLTxService.class) //
				.withValueMapperFactory(new JacksonValueMapperFactory()) //
				.generate();
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