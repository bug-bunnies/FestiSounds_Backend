package com.example.festisounds.Modules.FestivalArtists.Entities;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "festival_artist")
public class FestivalArtist {
    @Id
    @Column(name = "id")
    @NonNull
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "spotify_id", nullable = false, length=100)
    private String spotifyId;

    @Column(name = "artist_name", nullable = false, length = 100)
    private String artistName;

//    @Builder.Default
    @NonNull
    @ManyToMany
    @JoinTable(
            name = "festival_artist_link",
            joinColumns = @JoinColumn(name = "festival_artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "festival_id", referencedColumnName = "uuid")
    )
    private Set<Festival> festivals = new HashSet<>();

    public FestivalArtist(String spotifyId, String name) {
        this.artistName = name;
        this.spotifyId = spotifyId;
    }

}
