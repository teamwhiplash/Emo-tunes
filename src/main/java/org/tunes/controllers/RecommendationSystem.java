package org.tunes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tunes.dto.SongInfo;
import org.tunes.models.Songs;
import org.tunes.services.PlaylistService;
import org.tunes.services.Recommendation;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendationSystem {

    @Autowired
    private Recommendation recommendation;

    @GetMapping("/songs")
    public List<SongInfo> getSongsByEmotion(
            @RequestParam Integer userId,
            @RequestParam String emotion,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset
    ) {
        return recommendation.findAllSongs(userId, emotion, limit, offset);
    }


}
