package ru.practicum.explore.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}