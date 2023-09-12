package com.example.festisounds.Modules.Festival.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;
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
}
