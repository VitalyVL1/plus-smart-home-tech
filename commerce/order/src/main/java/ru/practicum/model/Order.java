package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.order.OrderState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Order {
    @Id
    @UuidGenerator
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState state;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "total_price", precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "delivery_price", precision = 15, scale = 2)
    private BigDecimal deliveryPrice;

    @Column(name = "product_price", precision = 15, scale = 2)
    private BigDecimal productPrice;

    @Column(name = "username")
    private String username;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "delivery_address_id")
    @ToString.Exclude
    private Address address;

    /**
     * Дата создания записи.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Дата последнего обновления.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
