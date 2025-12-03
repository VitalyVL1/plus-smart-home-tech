package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Address {
    @Id
    @UuidGenerator
    @Column(name = "address_id", updatable = false, nullable = false)
    private UUID addressId;
    @Column(name = "country", length = 65)
    private String country;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house", length = 20)
    private String house;

    @Column(name = "flat", length = 20)
    private String flat;
}
