package com.chwipoClova.subscription.entity;

import com.chwipoClova.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 100)
    @Column(name = "division", length = 100)
    private String division;

    @Size(max = 100)
    @Column(name = "thumbnail", length = 100)
    private String thumbnail;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "published", nullable = false)
    private Instant published;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @NotNull
    @Column(name = "delFlag", nullable = false)
    private Integer delFlag;

}