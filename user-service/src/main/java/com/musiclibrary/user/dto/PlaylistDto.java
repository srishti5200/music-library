package com.musiclibrary.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

public class PlaylistDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Playlist name is required")
        private String name;
        private String description;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private List<SongDto> songs;
        private int songCount;
    }

    @Data
    public static class AddSongRequest {
        private Long songId;
    }
}
