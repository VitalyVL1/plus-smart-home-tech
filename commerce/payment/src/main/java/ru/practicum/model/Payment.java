package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.dto.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Payment {
    @Id
    @UuidGenerator
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID paymentId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "total_payment", precision = 15, scale = 2)
    private BigDecimal totalPayment;

    @Column(name = "delivery_total", precision = 15, scale = 2)
    private BigDecimal deliveryTotal;

    @Column(name = "fee_total", precision = 15, scale = 2)
    private BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

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
