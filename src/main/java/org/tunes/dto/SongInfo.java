package org.tunes.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SongInfo {
    private String songID;
    private String songName;
    private String artistName;
    private String releaseDate;
    private String coverURL;
    private Integer duration;
    private String songURL;

    // Optional constructor if you want custom logic
    public SongInfo(String songID, String songName, String artistName, String releaseDate, String coverURL, Integer duration) {
        this.songID = songID;
        this.songName = songName;
        this.artistName = artistName;
        this.releaseDate = releaseDate;
        this.coverURL = coverURL;
        this.duration = duration;
        this.songURL = songID; // your custom logic
    }
}
