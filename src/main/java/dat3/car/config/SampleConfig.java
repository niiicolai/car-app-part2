package dat3.car.config;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.dto.car.CarRequest;
import dat3.car.dto.member.MemberRequest;
import dat3.car.dto.reservation.ReservationRequest;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfig {

    @Bean
    public List<Member> memberSamples() {
        var members = Arrays.asList(new Member[] {
            new Member("user1", "pass1", "user1@email.com", "John", "Doe", "123 Main St", "New York", "10001"),
            new Member("user2", "pass2", "user2@email.com", "Jane", "Smith", "456 Elm St", "Los Angeles", "90001"),
            new Member("user3", "pass3", "user3@email.com", "Bob", "Johnson", "789 Oak St", "Chicago", "60601"),
            new Member("user4", "pass4", "user4@email.com", "Emily", "Brown", "246 Pine St", "Houston", "77001"),
            new Member("user5", "pass5", "user5@email.com", "Michael", "Davis", "369 Cedar St", "Phoenix", "85001")
        });

        String[] favoriteCarColors = {"green", "red", "blue"};

        Map<String,String> phones = new HashMap<>();
        phones.put("work", "555-555-5555");
        phones.put("home", "555-555-1234");
        phones.put("mobile", "555-555-4321");

        for (Member member : members) {
            member.setFavoriteCarColors(Arrays.asList(favoriteCarColors));
            member.setPhones(phones);
        }

        return members;
    }

    @Bean
    public List<MemberRequest> memberRequestSamples() {
        return memberSamples().stream().map(c -> new MemberRequest(c)).collect(Collectors.toList());
    }

    @Bean
    public List<Car> carSamples() {
        return Arrays.asList(new Car[] {
            new Car("Toyota", "Camry", 150, 40000),
            new Car("Honda", "Civic", 200, 35000),
            new Car("Tesla", "Model 3", 350, 55000),
            new Car("Ford", "Mustang", 580, 32000),
            new Car("Chevrolet", "Corvette", 480, 75000),
            new Car("Opel", "Kadett", 780, 25400)
        });
    }

    @Bean
    public List<CarRequest> carRequestSamples() {
        return carSamples().stream().map(c -> new CarRequest(c)).collect(Collectors.toList());
    }

    @Bean
    public List<Reservation> reservationSamples() {
        List<Member> members = memberSamples();
        List<Car> cars = carSamples();

        return Arrays.asList(new Reservation[] {
            new Reservation(members.get(0), cars.get(0), LocalDateTime.now()),
            new Reservation(members.get(1), cars.get(1), LocalDateTime.now()),
            new Reservation(members.get(2), cars.get(2), LocalDateTime.now())
        });
    }

    @Bean
    public List<ReservationRequest> reservationRequestSamples() {
        return reservationSamples().stream().map(r -> new ReservationRequest(r)).collect(Collectors.toList());
    }
}
