package entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceTest {

    @Test
    void retirerDeuxUnitesLaisseTroisUnitesEnStock() {
        // Préparer une ressource avec 5 unités.
        Resource resource = Resource.builder()
                .status(ResourceStatus.AVAILABLE)
                .quantityTotal(5)
                .quantityAvailable(5)
                .build();

        // Exécuter la méthode à tester.
        resource.decrementStock(2);

        // Comparer le résultat attendu au résultat obtenu.
        assertEquals(3, resource.getQuantityAvailable());
    }

    @Test
    void retirerSixUnitesEstRefuseQuandIlEnResteCinq() {
        Resource resource = Resource.builder()
                .status(ResourceStatus.AVAILABLE)
                .quantityTotal(5)
                .quantityAvailable(5)
                .build();

        assertThrows(IllegalStateException.class, () -> resource.decrementStock(6));
        assertEquals(5, resource.getQuantityAvailable());
    }
}