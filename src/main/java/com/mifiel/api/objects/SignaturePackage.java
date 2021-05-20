package com.mifiel.api.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"status", "certificates", "plaintexts"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignaturePackage {

    @JsonProperty("status")
    private String status;

    @JsonIgnore
    private SignaturePackageSettings settings;

    @JsonProperty("certificates")
    private List<Certificate> certificates = new ArrayList<Certificate>();

    @JsonProperty("plaintexts")
    private List<PlainText> plaintexts = new ArrayList<PlainText>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return this.status;
    }

    public List<Certificate> getCertificates() {
        return this.certificates;
    }

    public List<PlainText> getPlaintexts() {
        return this.plaintexts;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public void setPlaintexts(List<PlainText> plaintexts) {
        this.plaintexts = plaintexts;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSettings(SignaturePackageSettings settings) {
        this.settings = settings;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
