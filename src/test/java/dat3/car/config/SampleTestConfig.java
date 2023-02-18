package dat3.car.config;

import dat3.car.car.config.CarSampleConfig;
import dat3.car.car.dto.CarRequest;
import dat3.car.car.entity.Car;
import dat3.car.member.config.MemberSampleConfig;
import dat3.car.member.dto.MemberRequest;
import dat3.car.member.entity.Member;
import dat3.car.reservation.config.ReservationSampleConfig;
import dat3.car.reservation.dto.ReservationRequest;
import dat3.car.reservation.entity.Reservation;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class SampleTestConfig {
    
    private CarSampleConfig carSampleConfig = new CarSampleConfig();
    private MemberSampleConfig memberSampleConfig = new MemberSampleConfig();
    private ReservationSampleConfig reservationSampleConfig = new ReservationSampleConfig();

    @Bean
    public List<Member> memberSamples() {      
        System.out.println(memberSampleConfig.memberSamples());  
        System.out.println(memberSampleConfig.memberSamples());  
        System.out.println(memberSampleConfig.memberSamples());  
        System.out.println(memberSampleConfig.memberSamples());  
        System.out.println(memberSampleConfig.memberSamples());  
        System.out.println(memberSampleConfig.memberSamples());  
        return memberSampleConfig.memberSamples();
    }

    @Bean
    public List<MemberRequest> memberRequestSamples() {
        return memberSampleConfig.memberRequestSamples();
    }

    @Bean
    public List<Car> carSamples() {
        return carSampleConfig.carSamples();
    }

    @Bean
    public List<CarRequest> carRequestSamples() {
        return carSampleConfig.carRequestSamples();
    }

    @Bean
    public List<Reservation> reservationSamples() {
        return reservationSampleConfig.reservationSamples();
    }

    @Bean
    public List<ReservationRequest> reservationRequestSamples() {
        return reservationSampleConfig.reservationRequestSamples();
    }
}
