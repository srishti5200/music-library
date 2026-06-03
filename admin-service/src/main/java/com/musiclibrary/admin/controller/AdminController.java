package com.musiclibrary.admin.controller;

import com.musiclibrary.admin.dto.AdminDto;
import com.musiclibrary.admin.service.AdminAuthService;
import com.musiclibrary.admin.service.SongAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminAuthService adminAuthService;
    private final SongAdminService songAdminService;

    // ─── Auth ────────────────────────────────────────────────────────────────

    @PostMapping("/auth/login")
    @Tag(name = "Admin Auth")
    @Operation(summary = "Admin login")
    public ResponseEntity<AdminDto.AuthResponse> login(@Valid @RequestBody AdminDto.LoginRequest request) {
        return ResponseEntity.ok(adminAuthService.login(request));
    }

    // ─── Song Management ─────────────────────────────────────────────────────

    @PostMapping("/songs")
    @Tag(name = "Song Management")
    @Operation(summary = "Add a new song to the library")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdminDto.SongResponse> addSong(@Valid @RequestBody AdminDto.SongRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songAdminService.addSong(request));
    }

    @GetMapping("/songs")
    @Tag(name = "Song Management")
    @Operation(summary = "Get all songs (including hidden)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<AdminDto.SongResponse>> getAllSongs() {
        return ResponseEntity.ok(songAdminService.getAllSongs());
    }

    @GetMapping("/songs/{id}")
    @Tag(name = "Song Management")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdminDto.SongResponse> getSong(@PathVariable Long id) {
        return ResponseEntity.ok(songAdminService.getSongById(id));
    }

    @PutMapping("/songs/{id}")
    @Tag(name = "Song Management")
    @Operation(summary = "Update song details")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdminDto.SongResponse> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody AdminDto.SongRequest request) {
        return ResponseEntity.ok(songAdminService.updateSong(id, request));
    }

    @DeleteMapping("/songs/{id}")
    @Tag(name = "Song Management")
    @Operation(summary = "Delete a song from the library")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songAdminService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/songs/{id}/toggle-visibility")
    @Tag(name = "Song Management")
    @Operation(summary = "Toggle song visibility (restrict/allow user access)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdminDto.SongResponse> toggleVisibility(@PathVariable Long id) {
        return ResponseEntity.ok(songAdminService.toggleVisibility(id));
    }

    // ─── Public endpoints (no auth) — for inter-service calls from user-service ──

    @GetMapping("/songs/public")
    @Tag(name = "Song Management")
    @Operation(summary = "Get all visible songs (public)")
    public ResponseEntity<List<AdminDto.SongResponse>> getVisibleSongs() {
        return ResponseEntity.ok(songAdminService.getVisibleSongs());
    }

    @GetMapping("/songs/public/{id}")
    @Tag(name = "Song Management")
    @Operation(summary = "Get a visible song by ID (public)")
    public ResponseEntity<AdminDto.SongResponse> getVisibleSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songAdminService.getVisibleSongById(id));
    }

    @GetMapping("/songs/public/search")
    @Tag(name = "Song Management")
    @Operation(summary = "Search visible songs by keyword (public)")
    public ResponseEntity<List<AdminDto.SongResponse>> searchVisibleSongs(@RequestParam String keyword) {
        return ResponseEntity.ok(songAdminService.searchVisibleSongs(keyword));
    }

    @GetMapping("/songs/public/search/singer")
    @Tag(name = "Song Management")
    public ResponseEntity<List<AdminDto.SongResponse>> searchBySinger(@RequestParam String singer) {
        return ResponseEntity.ok(songAdminService.searchBySinger(singer));
    }

    @GetMapping("/songs/public/search/album")
    @Tag(name = "Song Management")
    public ResponseEntity<List<AdminDto.SongResponse>> searchByAlbum(@RequestParam String album) {
        return ResponseEntity.ok(songAdminService.searchByAlbum(album));
    }

    @GetMapping("/songs/public/search/director")
    @Tag(name = "Song Management")
    public ResponseEntity<List<AdminDto.SongResponse>> searchByDirector(@RequestParam String director) {
        return ResponseEntity.ok(songAdminService.searchByDirector(director));
    }

    @GetMapping("/songs/public/batch")
    @Tag(name = "Song Management")
    @Operation(summary = "Get multiple visible songs by IDs (public)")
    public ResponseEntity<List<AdminDto.SongResponse>> getSongsByIds(
            @RequestParam List<Long> ids) {
        return ResponseEntity.ok(songAdminService.getSongsByIds(ids));
    }
}
