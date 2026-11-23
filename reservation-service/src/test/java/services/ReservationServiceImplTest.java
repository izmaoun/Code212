package services;

import clients.ResourceClient;
import dao.ReservationRepository;
import dto.ReservationRequest;
import dto.ResourceDto;
import exceptions.ReservationConflictException;
import mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ReservationServiceImplTest {

    @Test
    void litLeStockDisponibleRenvoyeParResourceService() {
        ResourceDto resource = JsonMapper.builder().build().readValue("""
                {"id":1,"name":"Ordinateur","quantityTotal":10,"quantityAvailable":5,"status":"AVAILABLE"}
                """, ResourceDto.class);

        assertEquals(5, resource.quantity());
    }

    @Test
    void refuseUneReservationQuandLeStockEstInsuffisant() {
        ResourceClient resourceClient = mock(ResourceClient.class);
        ReservationRepository repository = mock(ReservationRepository.class);
        ReservationServiceImpl service = new ReservationServiceImpl(
                repository, resourceClient, mock(ReservationMapper.class));

        long resourceId = 1L;
        when(resourceClient.getResourceById(resourceId))
                .thenReturn(new ResourceDto(resourceId, "Ordinateur", null, 0.0, 5, "AVAILABLE"));
        LocalDateTime debut = LocalDateTime.of(2026, 11, 21, 9, 0);
        ReservationRequest request = new ReservationRequest(resourceId, 2L, 6, debut, debut.plusHours(1));

        assertThrows(ReservationConflictException.class, () -> service.createReservation(request));
        verifyNoInteractions(repository);
    }
}
