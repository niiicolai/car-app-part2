package dat3.car.config;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.dto.car.CarRequest;
import dat3.car.dto.member.MemberRequest;
import dat3.car.dto.reservation.ReservationRequest;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class SampleTestConfig {
    
    private SampleConfig config = new SampleConfig();

    @Bean
    public List<Member> memberSamples() {        
        return config.memberSamples();
    }

    @Bean
    public List<MemberRequest> memberRequestSamples() {
        return config.memberRequestSamples();
    }

    @Bean
    public List<Car> carSamples() {
        return config.carSamples();
    }

    @Bean
    public List<CarRequest> carRequestSamples() {
        return config.carRequestSamples();
    }

    @Bean
    public List<Reservation> reservationSamples() {
        return config.reservationSamples();
    }

    @Bean
    public List<ReservationRequest> reservationRequestSamples() {
        return config.reservationRequestSamples();
    }
}
