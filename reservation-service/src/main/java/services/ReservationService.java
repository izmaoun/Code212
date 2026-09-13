package services;

import dto.ReservationRequest;
import dto.ReservationResponse;

import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request);
    ReservationResponse getReservationById(Long id);
    List<ReservationResponse> getAllReservations();
    List<ReservationResponse> getReservationsByUser(Long userId);
    ReservationResponse confirmReservation(Long id);
    ReservationResponse cancelReservation(Long id);
}
