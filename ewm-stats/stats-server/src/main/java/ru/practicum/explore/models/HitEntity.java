package ru.practicum.explore.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hit")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String app;

    @Column(nullable = false, length = 200)
    private String uri;

    @Column(nullable = false, length = 50)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public HitEntity(String app, String uri, String ip, LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}