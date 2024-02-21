package com.example.festisounds.Modules.FestivalArtists.Entities;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Column(name = "spotify_id", nullable = false, length = 100, unique = true)
    private String spotifyId;

    @Column(name = "artist_name", nullable = false, length = 100, unique = true)
    private String artistName;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "festival_artist_link",
            joinColumns = @JoinColumn(name = "festival_artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "festival_id", referencedColumnName = "uuid")
    )
    private Set<Festival> festivals;

    @NotNull
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    @Column(name = "spotify_genre")
    private Set<String> genres;

    public FestivalArtist(String spotifyId, String name, Festival festival, String[] genres) {
        this.spotifyId = spotifyId;
        this.artistName = name;
        this.festivals = new HashSet<>();
        this.genres = new HashSet<>();

        this.getFestivals().add(festival);
        for (String genre : genres) {
            this.getGenres().add(genre);
        }
    }

}
