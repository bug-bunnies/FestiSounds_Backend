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
@NoArgsConstructor
@Entity
@Table(name = "festisound_artist")
public class FestivalArtist {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "artist_name", nullable = false, length = 100)
    private String artistName;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "festival_artist",
            joinColumns = @JoinColumn(name = "festisound_artist_id"),
            inverseJoinColumns = @JoinColumn(name = "festival_id"))
    private Set<Festival> festivals = new HashSet<>();
}
