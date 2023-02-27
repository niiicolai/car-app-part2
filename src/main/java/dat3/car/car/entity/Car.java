package dat3.car.car.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dat3.car.motorRegister.entity.MotorRegister;
import dat3.car.reservation.entity.Reservation;

@Entity
@NoArgsConstructor
@Getter 
@Setter 
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "rental_price_day")
  private double pricePrDay;

  @Column(name = "max_discount")
  private int bestDiscount;

  @CreationTimestamp
  private LocalDateTime created;

  @UpdateTimestamp
  private LocalDateTime lastEdited;

  @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
  private List<Reservation> reservations;

  String make;
  String model;

  public Car(String make, String model, double pricePrDay, int bestDiscount) {
    this.make = make;
    this.model = model;
    this.pricePrDay = pricePrDay;
    this.bestDiscount = bestDiscount;
  }
}
