package org.tunes.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SongInfo {
    String songID;
    String songName;
    String artistName;
    String releaseDate;
    String coverURL;
    Integer duration;
    String songURL;

}
