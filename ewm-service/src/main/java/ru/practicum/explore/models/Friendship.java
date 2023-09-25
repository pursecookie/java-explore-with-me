package ru.practicum.explore.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "friendships")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private User friend;

    @Column(name = "friendship_stat", nullable = false)
    private Boolean friendshipStat;

    public Friendship(User user, User friend, Boolean friendshipStat) {
        this.user = user;
        this.friend = friend;
        this.friendshipStat = friendshipStat;
    }
}