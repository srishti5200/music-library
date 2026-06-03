package com.musiclibrary.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String subject;

    @Column(length = 1000)
    private String message;

    private String status; // SENT, FAILED

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();
}
