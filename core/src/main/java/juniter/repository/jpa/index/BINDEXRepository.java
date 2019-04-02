package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.BINDEX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository to manage {@link BINDEX} instances.
 */
@Repository
public interface BINDEXRepository extends JpaRepository<BINDEX, Long> {

    @Override
    <S extends BINDEX> List<S> saveAll(Iterable<S> entities);

    @Override
    List<BINDEX> findAll();

    @Override
    void delete(BINDEX entity);

    @Override
    long count();

    Optional<BINDEX> findFirstByNumberIsNotNullOrderByNumberDesc();

    public default Optional<BINDEX> head(){
        return findFirstByNumberIsNotNullOrderByNumberDesc();
    }


    @Transactional
    @Modifying
    default void trim(int bIndexSize){
        var top1 = head().map(b-> b.number).orElseThrow();

        findAll().stream()
                .filter(b-> b.number < top1-bIndexSize)
                .forEach(trim-> {
                    delete(trim);

        });
    }

    @Override
    public void deleteAll();
}

	
