package com.boun.swe.mnemosyne.annotation.annotationservice.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "styleClass",
        "source",
        "state",
        "selector"
})
public class Target {

    @JsonProperty("type")
    private String type;
    @JsonProperty("styleClass")
    private String styleClass;
    @JsonProperty("source")
    private String source;
    @JsonProperty("state")
    private List<State> state = null;
    @JsonProperty("selector")
    private Selector selector;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("styleClass")
    public String getStyleClass() {
        return styleClass;
    }

    @JsonProperty("styleClass")
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("state")
    public List<State> getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(List<State> state) {
        this.state = state;
    }

    @JsonProperty("selector")
    public Selector getSelector() {
        return selector;
    }

    @JsonProperty("selector")
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
