package org.tunes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tunes.dto.SongInfo;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistFE {
    private Integer playlistId;
    private String playlistName;
    private String coverUrl;
    private List<SongInfo> songs;
}
