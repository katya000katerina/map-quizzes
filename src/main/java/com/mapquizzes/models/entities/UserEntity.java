package com.mapquizzes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
@Table(schema = "users", name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private Integer id;
    @Column(name = "username")
    @NotNull @NotBlank
    private String username;
    @Column(name = "password")
    @NotNull @NotBlank
    private String password;
    @Column(name = "created_at")
    @NotNull
    private OffsetDateTime offsetDateTime;
    @OneToOne(mappedBy = "userEntity", fetch = FetchType.LAZY)
    private UserImageEntity userImageEntity;
}
