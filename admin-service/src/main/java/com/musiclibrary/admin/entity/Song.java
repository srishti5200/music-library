package com.musiclibrary.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    private String singer;

    private String musicDirector;

    private String albumName;

    private LocalDate releaseDate;

    private String genre;

    private String audioUrl;

    @Builder.Default
    @Column(nullable = false)
    private boolean visible = true;
}
