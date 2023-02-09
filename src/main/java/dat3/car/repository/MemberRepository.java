package dat3.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dat3.car.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
}