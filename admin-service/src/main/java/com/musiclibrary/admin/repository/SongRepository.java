package com.musiclibrary.admin.repository;

import com.musiclibrary.admin.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByVisibleTrue();
    List<Song> findByVisibleFalse();
    List<Song> findByIdInAndVisibleTrue(List<Long> ids);
    List<Song> findBySingerContainingIgnoreCaseAndVisibleTrue(String singer);
    List<Song> findByAlbumNameContainingIgnoreCaseAndVisibleTrue(String albumName);
    List<Song> findByMusicDirectorContainingIgnoreCaseAndVisibleTrue(String musicDirector);

    @Query("SELECT s FROM Song s WHERE s.visible = true AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.singer) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.albumName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.musicDirector) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Song> searchVisibleByKeyword(@Param("keyword") String keyword);
}
