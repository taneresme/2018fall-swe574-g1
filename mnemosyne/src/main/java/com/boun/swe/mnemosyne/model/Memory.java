package com.boun.swe.mnemosyne.model;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "memories")
@GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = "MEMORY_SEQUENCE"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
        }
)
public class Memory {
    @Id
    @GeneratedValue(generator = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @NotNull
    private String title;

    private String text;

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer duration;

    private String period;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private MemoryType type;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "memory_location",
            joinColumns = @JoinColumn(name = "memory_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    private Set<User> usersLiked = new HashSet<>();

    @Override
    public String toString() {
        return "Memory{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", duration=" + duration +
                ", period='" + period + '\'' +
                ", isPublished=" + isPublished +
                ", type=" + type +
                ", locations=" + locations +
                '}';
    }
}
