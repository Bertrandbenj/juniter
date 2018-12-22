package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository to manage {@link BINDEX} instances.
 */
@Repository
public interface BINDEXRepository extends JpaRepository<BINDEX, Long> {

    @Override
    <S extends BINDEX> List<S> saveAll(Iterable<S> entities);

    Optional<BINDEX> findFirstByNumberIsNotNullOrderByNumberDesc();

    default Optional<BINDEX> head(){
        return findFirstByNumberIsNotNullOrderByNumberDesc();
    }



    default void trim(Long bIndexSize){
        var top1 = head().map(b-> b.number).orElseThrow();

        findAll().stream()
                .filter(b-> b.number <= top1-bIndexSize)
                .forEach(trim-> {
                    delete(trim);

        });
    }
}

	
