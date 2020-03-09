package com.mifiel.api.objects;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "email", "tax_id", "field", "signed", "widget_id", "current", "fos", "pos"})
public class Signer {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("tax_id")
    private String taxId;
    @JsonProperty("field")
    private String field;
    @JsonProperty("signed")
    private Boolean signed;
    @JsonProperty("widget_id")
    private String widgetId;
    @JsonProperty("current")
    private Boolean current;
    @JsonProperty("fos")
    private String fos;
    @JsonProperty("pos")
    private String pos;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("tax_id")
    public String getTaxId() {
        return taxId;
    }

    @JsonProperty("tax_id")
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @JsonProperty("field")
    public String getField() {
        return field;
    }

    @JsonProperty("field")
    public void setField(String field) {
        this.field = field;
    }

    @JsonProperty("signed")
    public Boolean getSigned() {
        return signed;
    }

    @JsonProperty("signed")
    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    @JsonProperty("widget_id")
    public String getWidgetId() {
        return widgetId;
    }

    @JsonProperty("widget_id")
    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    @JsonProperty("current")
    public Boolean getCurrent() {
        return current;
    }

    @JsonProperty("current")
    public void setCurrent(Boolean current) {
        this.current = current;
    }

    @JsonProperty("fos")
    public String getFos() {
        return fos;
    }

    @JsonProperty("fos")
    public void setFos(String fos) {
        this.fos = fos;
    }

    @JsonProperty("pos")
    public String getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(String pos) {
        this.pos = pos;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Signer [id = " + id + ", name = " + name + ", email = " + email + ", taxId = " + taxId + ", field = "
                + field + ", signed = " + signed + ", widgetId = " + widgetId + ", current = " + current + ", fos = "
                + fos + ", pos = " + pos + ", additionalProperties=" + additionalProperties + "]";
    }
}
