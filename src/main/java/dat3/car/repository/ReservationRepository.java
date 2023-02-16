package dat3.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dat3.car.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
