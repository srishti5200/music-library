package com.musiclibrary.user.controller;

import com.musiclibrary.user.dto.SongDto;
import com.musiclibrary.user.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@Tag(name = "Songs", description = "Browse and search songs")
@SecurityRequirement(name = "bearerAuth")
public class SongController {

    private final SongService songService;

    @GetMapping
    @Operation(summary = "Get all visible songs")
    public ResponseEntity<List<SongDto>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllVisibleSongs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get song details by ID")
    public ResponseEntity<SongDto> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search songs by keyword (title, singer, album, director)")
    public ResponseEntity<List<SongDto>> searchSongs(@RequestParam String keyword) {
        return ResponseEntity.ok(songService.searchSongs(keyword));
    }

    @GetMapping("/search/singer")
    public ResponseEntity<List<SongDto>> searchBySinger(@RequestParam String singer) {
        return ResponseEntity.ok(songService.searchBySinger(singer));
    }

    @GetMapping("/search/album")
    public ResponseEntity<List<SongDto>> searchByAlbum(@RequestParam String album) {
        return ResponseEntity.ok(songService.searchByAlbum(album));
    }

    @GetMapping("/search/director")
    public ResponseEntity<List<SongDto>> searchByDirector(@RequestParam String director) {
        return ResponseEntity.ok(songService.searchByMusicDirector(director));
    }
}
