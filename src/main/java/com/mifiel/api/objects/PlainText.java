package com.mifiel.api.objects;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlainText {

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("signer")
    private String signer;

    @JsonProperty("widget_id")
    private String widgetId;

    @JsonProperty("alg")
    private String alg;

    @JsonProperty("certificate_number")
    private String certificateNumber;

    @JsonProperty("plaintext")
    private List<PlainTextItem> plaintext = new ArrayList<PlainTextItem>();

    public String getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    public String getSigner() {
        return this.signer;
    }

    public String getWidgetId() {
        return this.widgetId;
    }

    public String getAlg() {
        return this.alg;
    }

    public String getCertificateNumber() {
        return this.certificateNumber;
    }

    public List<PlainTextItem> getPlaintext() {
        return this.plaintext;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public void setPlaintext(List<PlainTextItem> plaintext) {
        this.plaintext = plaintext;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
}
