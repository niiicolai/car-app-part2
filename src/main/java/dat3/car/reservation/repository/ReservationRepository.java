package dat3.car.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dat3.car.member.entity.Member;
import dat3.car.reservation.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Find all reservations made by a certain member
    List<Reservation> findAllByMember(Member member);

    // Find the total number of reservations made by a certain member
    int countByMember(Member member);
}
