package com.mapquizzes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(schema = "users", name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    @NotNull @NotBlank
    private String username;
    @Column(name = "password")
    @NotNull @NotBlank
    private String password;
    @Column(name = "created_at")
    @NotNull
    private OffsetDateTime createdAt;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserImageEntity userImage;
}
