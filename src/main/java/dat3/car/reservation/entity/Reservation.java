package dat3.car.reservation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import dat3.car.car.entity.Car;
import dat3.car.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    @CreationTimestamp
    private LocalDateTime created;

    private LocalDateTime rentalDate;

    public Reservation(Member member, Car car, LocalDateTime rentalDate) {
        this.member = member;
        this.car = car;
        this.rentalDate = rentalDate;
    }

    public Reservation(int id, Member member, Car car, LocalDateTime rentalDate) {
        this.id = id;
        this.member = member;
        this.car = car;
        this.rentalDate = rentalDate;
    }
}
