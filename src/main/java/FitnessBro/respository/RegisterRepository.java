package FitnessBro.respository;

import FitnessBro.domain.coach.Entity.Coach;
import FitnessBro.domain.register.Entity.Register;
import FitnessBro.domain.member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import FitnessBro.domain.register.Entity.Register;
import FitnessBro.domain.review.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

public interface RegisterRepository extends JpaRepository<Register, Long> {

    Register findByMemberAndCoach(Member member,Coach coach);
    List<Register> findAllByCoach(Coach coach);
    List<Register> findAllByMember(Member member);
}

public interface RegisterRepository extends JpaRepository<Register,Long> {
    Long countByCoachId(Long coachId);

    Long countByMemberId(Long memberId);
}


