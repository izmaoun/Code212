package services;

import clients.ResourceClient;
import dao.ReservationRepository;
import dto.ReservationRequest;
import dto.ReservationResponse;
import dto.ResourceDto;
import dto.StockUpdateRequest;
import entities.Reservation;
import entities.ReservationStatus;
import exceptions.ReservationConflictException;
import exceptions.ReservationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.ReservationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceClient resourceClient;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponse createReservation(ReservationRequest request) {
        ResourceDto resource = resourceClient.getResourceById(request.resourceId());

        if (resource.quantity() == null || resource.quantity() < request.quantity()) {
            throw new ReservationConflictException("Stock insuffisant pour la ressource " + request.resourceId());
        }

        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation saved = reservationRepository.save(reservation);

        try {
            resourceClient.updateStock(request.resourceId(), new StockUpdateRequest(-request.quantity()));
            saved.setStatus(ReservationStatus.CONFIRMED);
            saved = reservationRepository.save(saved);
            log.info("Reservation confirmée pour la ressource {}", request.resourceId());
        } catch (Exception ex) {
            saved.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(saved);
            throw new ReservationConflictException("Impossible de confirmer la réservation: " + ex.getMessage());
        }

        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return reservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservation.setStatus(ReservationStatus.CANCELLED);
        resourceClient.updateStock(reservation.getResourceId(), new StockUpdateRequest(reservation.getQuantity()));
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }
}
