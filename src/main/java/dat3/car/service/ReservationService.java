package dat3.car.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (reservationOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with <ID> doesn't exist!");

        return new ReservationResponse(reservationOpt.get());
    }
    
    public ReservationResponse create(ReservationRequest reservationRequest) {
        Optional<Car> carOpt = carRepository.findById(reservationRequest.getCarId());
        if (carOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with <id> doesn't exist!");

        Optional<Member> memberOpt = memberRepository.findById(reservationRequest.getMemberUsername());
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with <USERNAME> doesn't exist!");

        Reservation reservation = new Reservation(memberOpt.get(), carOpt.get(), 
            reservationRequest.getRentalDate());
        
        reservation = reservationRepository.save(reservation);
        return new ReservationResponse(reservation);
    }

    public ReservationResponse update(ReservationRequest reservationRequest) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationRequest.getId());
        if (reservationOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with <ID> doesn't exist!");

        Reservation reservation = reservationOpt.get();
        reservation.setRentalDate(reservationRequest.getRentalDate());
        reservation = reservationRepository.save(reservation);

        return new ReservationResponse(reservation);
    }

    public void delete(int id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with <ID> doesn't exist!");

        reservationRepository.delete(reservationOpt.get());
    }
}
