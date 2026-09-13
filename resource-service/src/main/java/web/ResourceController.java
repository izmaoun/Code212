package web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import dto.ResourceRequest;
import dto.ResourceResponse;
import dto.StockUpdateRequest;
import services.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
@Tag(name = "Resources", description = "Gestion des ressources CODE 212")
@SecurityRequirement(name = "bearerAuth")
public class ResourceController {

    private final ResourceService service;

    // ═══════════════════════════════════════════════════
    // ADMIN uniquement (hasAuthority, PAS hasRole)
    // ═══════════════════════════════════════════════════

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Créer une ressource (ADMIN)")
    public ResponseEntity<ResourceResponse> create(@Valid @RequestBody ResourceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Modifier une ressource (ADMIN)")
    public ResourceResponse update(@PathVariable Long id,
                                   @Valid @RequestBody ResourceRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une ressource (ADMIN)")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ═══════════════════════════════════════════════════
    // Accessible à tous les utilisateurs authentifiés
    // ═══════════════════════════════════════════════════

    @GetMapping
    @Operation(summary = "Lister / rechercher les ressources")
    public List<ResourceResponse> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {
        return service.search(category, name);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une ressource")
    public ResourceResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Vérifier la disponibilité (interne)")
    public boolean checkAvailability(@PathVariable Long id,
                                     @RequestParam int quantity) {
        return service.checkAvailability(id, quantity);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Mettre à jour le stock (interne)")
    public ResourceResponse updateStock(@PathVariable Long id,
                                        @Valid @RequestBody StockUpdateRequest request) {
        return service.updateStock(id, request.delta());
    }
}