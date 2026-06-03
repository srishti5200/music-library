package com.musiclibrary.user.controller;

import com.musiclibrary.user.dto.PlaylistDto;
import com.musiclibrary.user.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
@Tag(name = "Playlists", description = "CRUD operations on user playlists")
@SecurityRequirement(name = "bearerAuth")
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    @Operation(summary = "Create a new playlist")
    public ResponseEntity<PlaylistDto.Response> createPlaylist(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PlaylistDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playlistService.createPlaylist(userDetails.getUsername(), request));
    }

    @GetMapping
    @Operation(summary = "Get all playlists for the logged-in user")
    public ResponseEntity<List<PlaylistDto.Response>> getMyPlaylists(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(playlistService.getUserPlaylists(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific playlist by ID")
    public ResponseEntity<PlaylistDto.Response> getPlaylist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getPlaylistById(userDetails.getUsername(), id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a playlist")
    public ResponseEntity<PlaylistDto.Response> updatePlaylist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody PlaylistDto.CreateRequest request) {
        return ResponseEntity.ok(playlistService.updatePlaylist(userDetails.getUsername(), id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a playlist")
    public ResponseEntity<Void> deletePlaylist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        playlistService.deletePlaylist(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/songs/{songId}")
    @Operation(summary = "Add a song to a playlist")
    public ResponseEntity<PlaylistDto.Response> addSong(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.addSongToPlaylist(userDetails.getUsername(), id, songId));
    }

    @DeleteMapping("/{id}/songs/{songId}")
    @Operation(summary = "Remove a song from a playlist")
    public ResponseEntity<PlaylistDto.Response> removeSong(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.removeSongFromPlaylist(userDetails.getUsername(), id, songId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search playlists by song name")
    public ResponseEntity<List<PlaylistDto.Response>> searchBySong(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String songName) {
        return ResponseEntity.ok(playlistService.searchPlaylistsBySong(userDetails.getUsername(), songName));
    }
}
