package exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("Reservation introuvable avec l'id: " + id);
    }
}
