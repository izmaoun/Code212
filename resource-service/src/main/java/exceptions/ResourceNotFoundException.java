package exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Ressource introuvable avec l'id : " + id);
    }
}