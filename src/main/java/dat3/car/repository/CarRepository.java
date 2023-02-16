package dat3.car.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dat3.car.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    // Find all cars with a certain brand and mode
    List<Car> findAllByBrandAndModel(String brand, String model);

    // Find the average price per day of all cars in the system
    @Query("SELECT AVG(c.pricePrDay) FROM Car c")
    Double findAveragePricePrDay();
    
    // Find all cars with the best discount
    @Query("SELECT c FROM Car c WHERE c.bestDiscount = (SELECT MAX(c2.bestDiscount) FROM Car c2)")
    List<Car> findAllWithBestDiscount();

    // Find all cars that have not been reserved
    @Query("SELECT c FROM Car c WHERE c.reservations IS EMPTY")
    List<Car> findAllByReservationsIsEmpty();
}