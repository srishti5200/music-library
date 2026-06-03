package com.musiclibrary.admin.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service", url = "${notification.service.url:}")
public interface NotificationClient {

    @PostMapping("/api/notifications/new-song")
    void notifyNewSong(@RequestBody Map<String, String> payload);
}
