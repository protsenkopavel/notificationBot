package net.protsenko.notificationbot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.entity.contract.AbstractEntity;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends AbstractEntity {

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "seconds")
    Long seconds;



    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
