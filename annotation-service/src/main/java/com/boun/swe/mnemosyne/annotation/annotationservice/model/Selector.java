package com.boun.swe.mnemosyne.annotation.annotationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "exact",
        "prefix",
        "suffix"
})
@Entity(name = "selector")
@GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = "SELECTOR_SEQUENCE"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
        }
)
public class Selector {

    @Id
    @GeneratedValue(generator = "sequenceGenerator")
    @JsonIgnore
    private Long id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("exact")
    private String exact;

    @JsonProperty("prefix")
    private String prefix;

    @JsonProperty("suffix")
    private String suffix;
}