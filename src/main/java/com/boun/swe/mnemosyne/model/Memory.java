package com.boun.swe.mnemosyne.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "memories")
public class Memory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 300)
    @NotNull
    private String title;

    @Size(max = 12000)
    private String text;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_from")
    private Date dateFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_to")
    private Date dateTo;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "is_public")
    private boolean isPublic;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "memory_location",
            joinColumns = @JoinColumn(name = "memory_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Memory{");
        sb.append("user=").append(user);
        sb.append(", title='").append(title).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", dateFrom=").append(dateFrom);
        sb.append(", dateTo=").append(dateTo);
        sb.append(", isPublished=").append(isPublished);
        sb.append(", isPublic=").append(isPublic);
        sb.append('}');
        return sb.toString();
    }
}
