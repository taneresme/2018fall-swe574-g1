package com.boun.swe.mnemosyne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
@GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = "LOCATION_SEQUENCE"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
        }
)
public class Location {
    @Id
    @GeneratedValue(generator = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    private String locationName;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @JsonIgnore
    @ManyToMany(mappedBy = "locations")
    private Set<Memory> memories;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Location{");
        sb.append("locationName='").append(locationName).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append('}');
        return sb.toString();
    }


    public String toJsonString(){
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"locationName\":");
        sb.append("\"").append(locationName).append("\"");
        sb.append(",");
        sb.append("\"latitude\":");
        sb.append("\"").append(latitude).append("\"");
        sb.append(",");
        sb.append("\"longitude\":");
        sb.append("\"").append(longitude).append("\"");
        sb.append(",");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.latitude, latitude) != 0) return false;
        if (Double.compare(location.longitude, longitude) != 0) return false;
        if (locationName != null ? !locationName.equals(location.locationName) : location.locationName != null)
            return false;
        return memories != null ? memories.equals(location.memories) : location.memories == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = locationName != null ? locationName.hashCode() : 0;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
