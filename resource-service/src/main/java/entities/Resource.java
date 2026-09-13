package entities;
//package code212.resource.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.*;
@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(name = "quantity_total", nullable = false)
    private Integer quantityTotal;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = ResourceStatus.AVAILABLE;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable(int qty) {
        return this.status == ResourceStatus.AVAILABLE
                && this.quantityAvailable != null
                && this.quantityAvailable >= qty;
    }

    public void decrementStock(int qty) {
        if (!isAvailable(qty)) {
            throw new IllegalStateException(
                    "Stock insuffisant pour la ressource id=" + id);
        }
        this.quantityAvailable -= qty;
    }

    public void incrementStock(int qty) {
        if (this.quantityAvailable + qty > this.quantityTotal) {
            throw new IllegalStateException(
                    "Impossible de dépasser le stock total");
        }
        this.quantityAvailable += qty;
    }
}