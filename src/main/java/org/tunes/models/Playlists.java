package org.tunes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "playlists", schema = "emotunes")
public class Playlists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ✅ Relationship with Users entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"playlists"}) // prevent recursive serialization
    private Users user;

    @Column(nullable = false)
    private String name;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column
    private String emotion; // nullable; only filled if linked with an emotion

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // ✅ Songs relationship
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    @JsonIgnoreProperties({"playlists"})// prevent circular references
    private List<Songs> songs = new ArrayList<>();

    public boolean isEmoList() {
        return emotion != null && !emotion.trim().isEmpty() && !"null".equalsIgnoreCase(emotion.trim());
    }
}
