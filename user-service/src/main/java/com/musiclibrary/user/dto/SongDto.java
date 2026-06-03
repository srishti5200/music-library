package com.musiclibrary.user.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SongDto {
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
