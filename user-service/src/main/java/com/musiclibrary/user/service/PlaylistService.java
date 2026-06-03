package com.musiclibrary.user.service;

import com.musiclibrary.user.client.AdminSongClient;
import com.musiclibrary.user.dto.PlaylistDto;
import com.musiclibrary.user.dto.SongDto;
import com.musiclibrary.user.entity.Playlist;
import com.musiclibrary.user.entity.User;
import com.musiclibrary.user.exception.ResourceNotFoundException;
import com.musiclibrary.user.repository.PlaylistRepository;
import com.musiclibrary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final AdminSongClient adminSongClient;

    public PlaylistDto.Response createPlaylist(String email, PlaylistDto.CreateRequest request) {
        User user = getUserByEmail(email);
        Playlist playlist = Playlist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();
        return toDto(playlistRepository.save(playlist));
    }

    public List<PlaylistDto.Response> getUserPlaylists(String email) {
        User user = getUserByEmail(email);
        return playlistRepository.findByUserId(user.getId())
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public PlaylistDto.Response getPlaylistById(String email, Long playlistId) {
        User user = getUserByEmail(email);
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: " + playlistId));
        return toDto(playlist);
    }

    public PlaylistDto.Response updatePlaylist(String email, Long playlistId, PlaylistDto.CreateRequest request) {
        User user = getUserByEmail(email);
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: " + playlistId));
        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        return toDto(playlistRepository.save(playlist));
    }

    public void deletePlaylist(String email, Long playlistId) {
        User user = getUserByEmail(email);
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: " + playlistId));
        playlistRepository.delete(playlist);
    }

    @Transactional
    public PlaylistDto.Response addSongToPlaylist(String email, Long playlistId, Long songId) {
        User user = getUserByEmail(email);
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: " + playlistId));
        // Verify song exists and is visible in admin-service
        adminSongClient.getSongById(songId);
        if (!playlist.getSongIds().contains(songId)) {
            playlist.getSongIds().add(songId);
        }
        return toDto(playlistRepository.save(playlist));
    }

    @Transactional
    public PlaylistDto.Response removeSongFromPlaylist(String email, Long playlistId, Long songId) {
        User user = getUserByEmail(email);
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: " + playlistId));
        playlist.getSongIds().remove(songId);
        return toDto(playlistRepository.save(playlist));
    }

    public List<PlaylistDto.Response> searchPlaylistsBySong(String email, String songName) {
        User user = getUserByEmail(email);
        List<Playlist> playlists = playlistRepository.findByUserId(user.getId());

        // Collect all unique song IDs across all playlists
        List<Long> allSongIds = playlists.stream()
                .flatMap(p -> p.getSongIds().stream())
                .distinct()
                .collect(Collectors.toList());

        if (allSongIds.isEmpty()) return List.of();

        // Fetch songs in batch from admin-service and build a lookup map
        List<SongDto> songs = adminSongClient.getSongsByIds(allSongIds);
        String lowerName = songName.toLowerCase();

        // Filter playlists that contain at least one song matching the name
        return playlists.stream()
                .filter(p -> songs.stream()
                        .anyMatch(s -> p.getSongIds().contains(s.getId())
                                && s.getTitle().toLowerCase().contains(lowerName)))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private PlaylistDto.Response toDto(Playlist playlist) {
        List<SongDto> songs = playlist.getSongIds().isEmpty()
                ? List.of()
                : adminSongClient.getSongsByIds(playlist.getSongIds());

        PlaylistDto.Response dto = new PlaylistDto.Response();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setSongs(songs);
        dto.setSongCount(songs.size());
        return dto;
    }
}
