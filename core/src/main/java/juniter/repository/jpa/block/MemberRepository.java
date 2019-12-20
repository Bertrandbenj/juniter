package juniter.repository.jpa.block;

import juniter.core.model.dbo.wot.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    @Query(value = "SELECT DISTINCT writtenon FROM wot_member m ORDER BY writtenon ")
//    List<Integer> withMembers();

    @Query(value = "SELECT DISTINCT written.number FROM Excluded m ORDER BY written.number")
    List<Integer> withExcluded();

    @Query(value = "SELECT DISTINCT written.number FROM Joiner m ORDER BY written.number")
    List<Integer> withJoiners();

    @Query(value = "SELECT DISTINCT written.number FROM Leaver m ORDER BY written.number")
    List<Integer> withLeavers();

    @Query(value = "SELECT DISTINCT written.number FROM Renew m ORDER BY written.number")
    List<Integer> withRenewed();

    @Query(value = "SELECT DISTINCT written.number FROM Revoked m ORDER BY written.number")
    List<Integer> withRevoked();


}
