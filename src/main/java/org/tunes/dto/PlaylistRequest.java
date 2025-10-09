package org.tunes.dto;

import lombok.Data;

@Data
public class PlaylistRequest {
    private String name;
    private String emotion;  // optional
    private String coverUrl; // optional
}
