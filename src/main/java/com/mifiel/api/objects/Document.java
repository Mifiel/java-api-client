package com.mifiel.api.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "original_hash", "name", "signed_by_all", "signed", "signed_at", "status", "owner",
    "callback_url", "file", "file_download", "file_signed", "file_signed_download", "file_zipped", "signatures", "signers"})
public class Document {

    @JsonProperty("id")
    private String id;
    @JsonProperty("original_hash")
    private String originalHash;
    @JsonProperty("name")
    private String fileName;
    @JsonProperty("signed_by_all")
    private Boolean signedByAll;
    @JsonProperty("signed")
    private Boolean signed;
    @JsonProperty("signed_at")
    private String signedAt;
    @JsonProperty("status")
    private List<Object> status = null;
    @JsonProperty("owner")
    private Owner owner;
    @JsonProperty("callback_url")
    private String callbackUrl;
    @JsonProperty("file")
    private String file;
    @JsonProperty("file_download")
    private String fileDownload;
    @JsonProperty("file_signed")
    private String fileSigned;
    @JsonProperty("file_signed_download")
    private String fileSignedDownload;
    @JsonProperty("file_zipped")
    private String fileZipped;
    @JsonProperty("signatures")
    private List<Signature> signatures = null;
    @JsonProperty("signers")
    private List<Signer> signers;
    @JsonProperty("viewers")
    private List<Viewer> viewers;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Document() {
        this.signers = new ArrayList<Signer>();
        this.viewers = new ArrayList<Viewer>();
        this.signatures = new ArrayList<Signature>();
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("original_hash")
    public String getOriginalHash() {
        return originalHash;
    }

    @JsonProperty("original_hash")
    public void setOriginalHash(String originalHash) {
        this.originalHash = originalHash;
    }

    @JsonProperty("name")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("name")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("signed_by_all")
    public Boolean getSignedByAll() {
        return signedByAll;
    }

    @JsonProperty("signed_by_all")
    public void setSignedByAll(Boolean signedByAll) {
        this.signedByAll = signedByAll;
    }

    @JsonProperty("signed")
    public Boolean getSigned() {
        return signed;
    }

    @JsonProperty("signed")
    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    @JsonProperty("signed_at")
    public String getSignedAt() {
        return signedAt;
    }

    @JsonProperty("signed_at")
    public void setSignedAt(String signedAt) {
        this.signedAt = signedAt;
    }

    @JsonProperty("status")
    public List<Object> getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(List<Object> status) {
        this.status = status;
    }

    @JsonProperty("owner")
    public Owner getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @JsonProperty("callback_url")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @JsonProperty("callback_url")
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @JsonProperty("file")
    public String getFile() {
        return file;
    }

    @JsonProperty("file")
    public void setFile(String file) {
        this.file = file;
    }

    @JsonProperty("file_download")
    public String getFileDownload() {
        return fileDownload;
    }

    @JsonProperty("file_download")
    public void setFileDownload(String fileDownload) {
        this.fileDownload = fileDownload;
    }

    @JsonProperty("file_signed")
    public String getFileSigned() {
        return fileSigned;
    }

    @JsonProperty("file_signed")
    public void setFileSigned(String fileSigned) {
        this.fileSigned = fileSigned;
    }

    @JsonProperty("file_signed_download")
    public String getFileSignedDownload() {
        return fileSignedDownload;
    }

    @JsonProperty("file_signed_download")
    public void setFileSignedDownload(String fileSignedDownload) {
        this.fileSignedDownload = fileSignedDownload;
    }

    @JsonProperty("file_zipped")
    public String getFileZipped() {
        return fileZipped;
    }

    @JsonProperty("file_zipped")
    public void setFileZipped(String fileZipped) {
        this.fileZipped = fileZipped;
    }

    @JsonProperty("signatures")
    public List<Signature> getSignatures() {
        return signatures;
    }

    @JsonProperty("signatures")
    public void setSignatures(List<Signature> signatures) {
        this.signatures = signatures;
    }

    @JsonProperty("signers")
    public List<Signer> getSigners() {
        return signers;
    }

    @JsonProperty("signers")
    public void setSigners(List<Signer> signers) {
        this.signers = signers;
    }

    @JsonProperty("viewers")
    public List<Viewer> getViewers() {
        return viewers;
    }

    @JsonProperty("viewers")
    public void setViewers(List<Viewer> viewers) {
        this.viewers = viewers;
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
        return "Document [id=" + id + ", originalHash=" + originalHash + ", fileName=" + fileName + ", signedByAll="
                + signedByAll + ", signed=" + signed + ", signedAt=" + signedAt + ", status=" + status + ", owner="
                + owner + ", callbackUrl=" + callbackUrl + ", file=" + file + ", fileDownload=" + fileDownload
                + ", fileSigned=" + fileSigned + ", fileSignedDownload=" + fileSignedDownload + ", fileZipped="
                + fileZipped + ", signatures=" + signatures + ", signers=" + signers + ", additionalProperties=" + additionalProperties + "]";
    }
}
