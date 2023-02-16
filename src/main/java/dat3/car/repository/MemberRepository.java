package dat3.car.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dat3.car.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    // Find all members who have made a reservation
    @Query("SELECT m FROM Member m WHERE m.reservations IS NOT EMPTY")
    List<Member> findAllByReservationsIsNotEmpty();
}