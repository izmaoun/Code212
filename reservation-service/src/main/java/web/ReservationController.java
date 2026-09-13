package web;

import dto.ReservationRequest;
import dto.ReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ReservationResponse getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationResponse> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PatchMapping("/{id}/confirm")
    public ReservationResponse confirmReservation(@PathVariable Long id) {
        return reservationService.confirmReservation(id);
    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
}
