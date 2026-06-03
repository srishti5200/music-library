package com.musiclibrary.notification.service;

import com.musiclibrary.notification.entity.NotificationLog;
import com.musiclibrary.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationLogRepository logRepository;

    @Value("${notification.from-email}")
    private String fromEmail;

    @Value("${notification.subscriber-emails:}")
    private String subscriberEmails;

    @Async
    public void sendNewSongNotification(Map<String, String> payload) {
        String songTitle = payload.getOrDefault("songTitle", "New Song");
        String singer = payload.getOrDefault("singer", "Unknown Artist");
        String subject = "🎵 New Song Added: " + songTitle;
        String body = String.format(
                "Hello Music Lover!\n\nA new song has been added to the Music Library:\n\n" +
                "Title: %s\nArtist: %s\n\nLog in to listen now!\n\n— Music Library Team",
                songTitle, singer
        );

        List<String> recipients = getSubscriberList();
        if (recipients.isEmpty()) {
            log.info("No subscribers configured. Skipping email for: {}", songTitle);
            logNotification("NO_SUBSCRIBERS", subject, body, "SKIPPED");
            return;
        }

        for (String email : recipients) {
            sendEmail(email.trim(), subject, body);
        }
    }

    @Async
    public void sendCustomNotification(String to, String subject, String body) {
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to: {}", to);
            logNotification(to, subject, body, "SENT");
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            logNotification(to, subject, body, "FAILED");
        }
    }

    private List<String> getSubscriberList() {
        if (subscriberEmails == null || subscriberEmails.isBlank()) return List.of();
        return Arrays.asList(subscriberEmails.split(","));
    }

    private void logNotification(String recipient, String subject, String message, String status) {
        logRepository.save(NotificationLog.builder()
                .recipient(recipient).subject(subject)
                .message(message).status(status).build());
    }

    public List<NotificationLog> getLogs() {
        return logRepository.findAll();
    }
}
