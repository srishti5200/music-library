package com.musiclibrary.admin.service;

import com.musiclibrary.admin.config.NotificationClient;
import com.musiclibrary.admin.dto.AdminDto;
import com.musiclibrary.admin.entity.Song;
import com.musiclibrary.admin.exception.ResourceNotFoundException;
import com.musiclibrary.admin.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongAdminService {

    private final SongRepository songRepository;
    private final NotificationClient notificationClient;

    public AdminDto.SongResponse addSong(AdminDto.SongRequest request) {
        Song song = Song.builder()
                .title(request.getTitle())
                .singer(request.getSinger())
                .musicDirector(request.getMusicDirector())
                .albumName(request.getAlbumName())
                .releaseDate(request.getReleaseDate())
                .genre(request.getGenre())
                .audioUrl(request.getAudioUrl())
                .visible(request.isVisible())
                .build();
        Song saved = songRepository.save(song);

        // Notify asynchronously — fire and forget, don't fail if notification fails
        try {
            notificationClient.notifyNewSong(Map.of(
                "songTitle", saved.getTitle(),
                "singer", saved.getSinger() != null ? saved.getSinger() : "Unknown"
            ));
        } catch (Exception e) {
            log.warn("Notification service unavailable: {}", e.getMessage());
        }

        return toDto(saved);
    }

    public List<AdminDto.SongResponse> getAllSongs() {
        return songRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AdminDto.SongResponse getSongById(Long id) {
        return toDto(findById(id));
    }

    public AdminDto.SongResponse updateSong(Long id, AdminDto.SongRequest request) {
        Song song = findById(id);
        song.setTitle(request.getTitle());
        song.setSinger(request.getSinger());
        song.setMusicDirector(request.getMusicDirector());
        song.setAlbumName(request.getAlbumName());
        song.setReleaseDate(request.getReleaseDate());
        song.setGenre(request.getGenre());
        song.setAudioUrl(request.getAudioUrl());
        song.setVisible(request.isVisible());
        return toDto(songRepository.save(song));
    }

    public void deleteSong(Long id) {
        songRepository.delete(findById(id));
    }

    public AdminDto.SongResponse toggleVisibility(Long id) {
        Song song = findById(id);
        song.setVisible(!song.isVisible());
        return toDto(songRepository.save(song));
    }

    public List<AdminDto.SongResponse> getVisibleSongs() {
        return songRepository.findByVisibleTrue().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AdminDto.SongResponse getVisibleSongById(Long id) {
        Song song = songRepository.findById(id)
                .filter(Song::isVisible)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found: " + id));
        return toDto(song);
    }

    public List<AdminDto.SongResponse> searchVisibleSongs(String keyword) {
        return songRepository.searchVisibleByKeyword(keyword).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AdminDto.SongResponse> searchBySinger(String singer) {
        return songRepository.findBySingerContainingIgnoreCaseAndVisibleTrue(singer).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AdminDto.SongResponse> searchByAlbum(String album) {
        return songRepository.findByAlbumNameContainingIgnoreCaseAndVisibleTrue(album).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AdminDto.SongResponse> searchByDirector(String director) {
        return songRepository.findByMusicDirectorContainingIgnoreCaseAndVisibleTrue(director).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AdminDto.SongResponse> getSongsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return songRepository.findByIdInAndVisibleTrue(ids).stream().map(this::toDto).collect(Collectors.toList());
    }

    private Song findById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found: " + id));
    }

    private AdminDto.SongResponse toDto(Song song) {
        AdminDto.SongResponse dto = new AdminDto.SongResponse();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setSinger(song.getSinger());
        dto.setMusicDirector(song.getMusicDirector());
        dto.setAlbumName(song.getAlbumName());
        dto.setReleaseDate(song.getReleaseDate());
        dto.setGenre(song.getGenre());
        dto.setAudioUrl(song.getAudioUrl());
        dto.setVisible(song.isVisible());
        return dto;
    }
}
