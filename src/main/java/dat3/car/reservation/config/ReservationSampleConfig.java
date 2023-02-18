package dat3.car.reservation.config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dat3.car.car.config.CarSampleConfig;
import dat3.car.car.entity.Car;
import dat3.car.member.config.MemberSampleConfig;
import dat3.car.member.entity.Member;
import dat3.car.reservation.dto.ReservationRequest;
import dat3.car.reservation.entity.Reservation;

@Configuration
public class ReservationSampleConfig {

    private CarSampleConfig carSampleConfig = new CarSampleConfig();
    private MemberSampleConfig memberSampleConfig = new MemberSampleConfig();

    @Bean
    public List<Reservation> reservationSamples() {
        List<Car> carSamples = carSampleConfig.carSamples();
        List<Member> memberSamples = memberSampleConfig.memberSamples();

        return Arrays.asList(new Reservation[] {
            new Reservation(memberSamples.get(0), carSamples.get(0), LocalDateTime.now()),
            new Reservation(memberSamples.get(1), carSamples.get(1), LocalDateTime.now()),
            new Reservation(memberSamples.get(2), carSamples.get(2), LocalDateTime.now())
        });
    }

    @Bean
    public List<ReservationRequest> reservationRequestSamples() {
        return reservationSamples().stream().map(r -> new ReservationRequest(r)).collect(Collectors.toList());
    }
}
