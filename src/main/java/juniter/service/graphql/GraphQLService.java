package juniter.service.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.model.Block;
import juniter.repository.BlockRepository;

@Service
public class GraphQLService {

	@Autowired
	private BlockRepository blockRepository;

	@GraphQLQuery(name = "cars")
	public List<Block> getCars() {
		return blockRepository.findAll();
	}
}
