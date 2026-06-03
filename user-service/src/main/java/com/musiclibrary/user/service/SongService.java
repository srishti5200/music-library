package com.musiclibrary.user.service;

import com.musiclibrary.user.client.AdminSongClient;
import com.musiclibrary.user.dto.SongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

    private final AdminSongClient adminSongClient;

    public List<SongDto> getAllVisibleSongs() {
        return adminSongClient.getAllVisibleSongs();
    }

    public SongDto getSongById(Long id) {
        return adminSongClient.getSongById(id);
    }

    public List<SongDto> searchSongs(String keyword) {
        return adminSongClient.searchSongs(keyword);
    }

    public List<SongDto> searchBySinger(String singer) {
        return adminSongClient.searchBySinger(singer);
    }

    public List<SongDto> searchByAlbum(String album) {
        return adminSongClient.searchByAlbum(album);
    }

    public List<SongDto> searchByMusicDirector(String musicDirector) {
        return adminSongClient.searchByDirector(musicDirector);
    }
}
