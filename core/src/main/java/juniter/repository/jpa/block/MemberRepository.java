package juniter.repository.jpa.block;

import juniter.core.model.dbo.wot.Leaver;
import juniter.core.model.dbo.wot.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    @Query(value = "SELECT DISTINCT writtenon FROM wot_member m ORDER BY writtenon ")
//    List<Integer> withMembers();

    @Query(value = "SELECT DISTINCT writtenon FROM wot_member  WHERE dtype = 'Excluded' ORDER BY writtenon ", nativeQuery = true)
    List<Integer> withExcluded();


    @Query(value = "SELECT DISTINCT writtenon FROM wot_member  WHERE dtype = 'Joiner' ORDER BY writtenon ", nativeQuery = true )
    List<Integer> withJoiners();

    @Query(value = "SELECT DISTINCT writtenon FROM wot_member  WHERE dtype = 'Leaver' ORDER BY writtenon ", nativeQuery = true)
    List<Integer> withLeavers();

    @Query(value = "SELECT DISTINCT writtenon FROM wot_member  WHERE dtype = 'Renew' ORDER BY writtenon ", nativeQuery = true)
    List<Integer> withRenewed();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_member WHERE dtype = 'Revoked'  ORDER BY writtenon ", nativeQuery = true)
    List<Integer> withRevoked();


}
