package net.protsenko.notificationbot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.entity.contract.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bot_users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractEntity {

    @Column(name = "chat_id", unique = true, nullable = false)
    Long chatId;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Enumerated(EnumType.STRING)
    Action action;

    @Column(name = "registered_at", nullable = false)
    LocalDateTime registeredAt;

    @OneToMany
    Set<Notification> notifications;
}
