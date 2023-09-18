package ru.practicum.explore.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_requests")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;

    @Column(nullable = false, length = 50)
    private String status;

    public EventRequest(LocalDateTime created, Event event, User requester, String status) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}