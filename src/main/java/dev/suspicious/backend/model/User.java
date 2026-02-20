package dev.suspicious.backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String address;

    @Enumerated(EnumType.STRING)
    @Default
    private Role role = Role.user;
}