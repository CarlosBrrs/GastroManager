package com.kaiho.gastromanager.infrastructure.auth.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_tokens")
@Getter
@Setter
@Builder
public class VerificationTokenEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    private UUID token;

    @OneToOne
    @JoinColumn(name = "user_uuid", nullable = false, referencedColumnName = "uuid")
    private UserEntity user;

    private Instant expiryDate;

}