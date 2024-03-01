package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.Song;

public interface SongService {
    void addSong(Song song);
    List<Song> fetchAllSongs();
    boolean songExists(String name);
    void updateSong(Song song);
}
