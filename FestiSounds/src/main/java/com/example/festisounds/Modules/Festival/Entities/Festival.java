package com.example.festisounds.Modules.Festival.Entities;

import com.example.festisounds.Modules.FestiSoundArtists.Entities.FestiSoundArtist;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "festival")
public class Festival {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "details", nullable = false, length = 500)
    private String details;

//    @ManyToMany
//    private festivalArtist artist; festivalId spotifyId name

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "festival_artist",
            joinColumns = @JoinColumn(name = "festival_id"),
            inverseJoinColumns = @JoinColumn(name = "festisound_artist_id"))
    private Set<FestiSoundArtist> artists = new HashSet<>();

//    @OneToMany
//    festivalGenre name

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "is_robbie_invited")
    private boolean isRobbieInvited;

    @Column(name = "image", nullable = false, length = 500)
    private String image;

    @Column(name = "created_on")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;

    @Column(name = "last_updated_on")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant lastUpdatedOn;

    @Column(name = "festival_organizer", nullable = false, length = 100)
    private String festivalOrganizer;

    public void addArtist(FestiSoundArtist artist) {
        this.artists.add(artist);
        artist.getFestivals().add(this);
    }

    public void removeArtist(FestiSoundArtist artist) {
        this.artists.remove(artist);
        artist.getFestivals().remove(this);
    }
}
