package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.delivery.DeliveryState;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Delivery {
    @Id
    @UuidGenerator
    @Column(name = "delivery_id", updatable = false, nullable = false)
    private UUID deliveryId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "from_address_id", nullable = false)
    private Address fromAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "to_address_id", nullable = false)
    private Address toAddress;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false, length = 20)
    private DeliveryState deliveryState;

    /**
     * Дата создания доставки.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Дата последнего обновления информации о доставке.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
