package dat3.car.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dat3.car.dto.reservation.ReservationRequest;
import dat3.car.dto.reservation.ReservationResponse;
import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private CarRepository carRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        MemberRepository memberRepository,
        CarRepository carRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.carRepository = carRepository;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(reservation -> new ReservationResponse(reservation)).collect(Collectors.toList());
    }

    public ReservationResponse find(int id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);

        return new ReservationResponse(reservationOpt.get());
    }
    
    public ReservationResponse create(ReservationRequest reservationRequest) {
        Optional<Member> member = memberRepository.findById(reservationRequest.getMemberUsername());
        Optional<Car> car = carRepository.findById(reservationRequest.getCarId());
        Reservation reservation = new Reservation(member.get(), car.get(), reservationRequest.getRentalDate());
        
        reservation = reservationRepository.save(reservation);
        return new ReservationResponse(reservation);
    }

    public ReservationResponse update(ReservationRequest reservationRequest) {
        Optional<Member> member = memberRepository.findById(reservationRequest.getMemberUsername());
        Optional<Car> car = carRepository.findById(reservationRequest.getCarId());
        Reservation reservation = new Reservation(member.get(), car.get(), reservationRequest.getRentalDate());
        reservation.setId(reservationRequest.getId());

        reservation = reservationRepository.save(reservation);
        return new ReservationResponse(reservation);
    }

    public void delete(int id) {
        reservationRepository.deleteById(id);
    }
}
