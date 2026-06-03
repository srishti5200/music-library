package com.musiclibrary.user.client;

import com.musiclibrary.user.dto.SongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "admin-service")
public interface AdminSongClient {

    @GetMapping("/api/admin/songs/public")
    List<SongDto> getAllVisibleSongs();

    @GetMapping("/api/admin/songs/public/{id}")
    SongDto getSongById(@PathVariable("id") Long id);

    @GetMapping("/api/admin/songs/public/search")
    List<SongDto> searchSongs(@RequestParam("keyword") String keyword);

    @GetMapping("/api/admin/songs/public/search/singer")
    List<SongDto> searchBySinger(@RequestParam("singer") String singer);

    @GetMapping("/api/admin/songs/public/search/album")
    List<SongDto> searchByAlbum(@RequestParam("album") String album);

    @GetMapping("/api/admin/songs/public/search/director")
    List<SongDto> searchByDirector(@RequestParam("director") String director);

    @GetMapping("/api/admin/songs/public/batch")
    List<SongDto> getSongsByIds(@RequestParam("ids") List<Long> ids);
}
