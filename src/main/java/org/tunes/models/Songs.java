package org.tunes.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "songs", schema = "emotunes")
public class Songs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "artist_name", length = 150)
    private String artistName;

    @Column(name = "spotify_id", unique = true, length = 100)
    private String spotifyId;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "duration_milliseconds")
    private Integer durationMilliseconds;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public Songs() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @Column(name = "song_url", length = 500)
    private String songUrl;

    @Column(length = 10)
    private String emotion; // default 'neutral', optional

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    // Inverse side of Many-to-Many with Playlists
    @ManyToMany(mappedBy = "songs", fetch = FetchType.LAZY)
    private Set<Playlists> playlists = new HashSet<>();
}

