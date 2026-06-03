package com.musiclibrary.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

public class AdminDto {

    @Data
    public static class LoginRequest {
        @Email @NotBlank private String email;
        @NotBlank private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String email;
        private String name;
        public AuthResponse(String token, String email, String name) {
            this.token = token; this.email = email; this.name = name;
        }
    }

    @Data
    public static class SongRequest {
        @NotBlank(message = "Title is required") private String title;
        private String singer;
        private String musicDirector;
        private String albumName;
        private LocalDate releaseDate;
        private String genre;
        private String audioUrl;
        private boolean visible = true;
    }

    @Data
    public static class SongResponse {
        private Long id;
        private String title;
        private String singer;
        private String musicDirector;
        private String albumName;
        private LocalDate releaseDate;
        private String genre;
        private String audioUrl;
        private boolean visible;
    }
}
