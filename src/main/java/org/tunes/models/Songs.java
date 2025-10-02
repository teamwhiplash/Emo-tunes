package org.tunes.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
public class Songs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    private LocalDate releaseDate;

    @Column(length = 150)
    private String artistName;

    @Column(unique = true, length = 100)
    private String spotifyId;

    @Column(length = 500)
    private String coverUrl;

    @Column(length = 500)
    private String songUrl;

    private Integer durationSeconds;

    private Double danceability;
    private Double energy;

    @Column(length = 10)
    private String keySig;

    private Double loudness;
    private Double speechiness;
    private Double acousticness;
    private Double instrumentalness;
    private Double liveness;
    private Double valence;
    private Double tempo;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public Songs() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getSongUrl() { return songUrl; }
    public void setSongUrl(String songUrl) { this.songUrl = songUrl; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public Double getDanceability() { return danceability; }
    public void setDanceability(Double danceability) { this.danceability = danceability; }

    public Double getEnergy() { return energy; }
    public void setEnergy(Double energy) { this.energy = energy; }

    public String getKeySig() { return keySig; }
    public void setKeySig(String keySig) { this.keySig = keySig; }

    public Double getLoudness() { return loudness; }
    public void setLoudness(Double loudness) { this.loudness = loudness; }

    public Double getSpeechiness() { return speechiness; }
    public void setSpeechiness(Double speechiness) { this.speechiness = speechiness; }

    public Double getAcousticness() { return acousticness; }
    public void setAcousticness(Double acousticness) { this.acousticness = acousticness; }

    public Double getInstrumentalness() { return instrumentalness; }
    public void setInstrumentalness(Double instrumentalness) { this.instrumentalness = instrumentalness; }

    public Double getLiveness() { return liveness; }
    public void setLiveness(Double liveness) { this.liveness = liveness; }

    public Double getValence() { return valence; }
    public void setValence(Double valence) { this.valence = valence; }

    public Double getTempo() { return tempo; }
    public void setTempo(Double tempo) { this.tempo = tempo; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
