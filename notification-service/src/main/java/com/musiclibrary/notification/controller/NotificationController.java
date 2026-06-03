package com.musiclibrary.notification.controller;

import com.musiclibrary.notification.entity.NotificationLog;
import com.musiclibrary.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Email notification endpoints")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/new-song")
    @Operation(summary = "Send new song notification to all subscribers")
    public ResponseEntity<String> newSongNotification(@RequestBody Map<String, String> payload) {
        notificationService.sendNewSongNotification(payload);
        return ResponseEntity.ok("Notification queued");
    }

    @PostMapping("/custom")
    @Operation(summary = "Send a custom notification to a specific email")
    public ResponseEntity<String> customNotification(@RequestBody Map<String, String> payload) {
        notificationService.sendCustomNotification(
                payload.get("to"), payload.get("subject"), payload.get("body")
        );
        return ResponseEntity.ok("Custom notification queued");
    }

    @GetMapping("/logs")
    @Operation(summary = "Get all notification logs")
    public ResponseEntity<List<NotificationLog>> getLogs() {
        return ResponseEntity.ok(notificationService.getLogs());
    }
}
