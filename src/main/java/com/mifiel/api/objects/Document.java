package com.mifiel.api.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "original_hash", "name", "signed_by_all", "signed", "signed_at", "status", "owner",
        "callback_url", "file", "file_download", "file_signed", "file_signed_download", "file_zipped", "signatures" })
public class Document {

    @JsonProperty("id")
    private String id;
    @JsonProperty("original_hash")
    private String originalHash;
    @JsonProperty("file_file_name")
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	@JsonProperty("allow_business")
	private Boolean allowBusiness;
    @JsonProperty("sign_callback_url")
    private String signCallbackUrl;	

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

    @JsonProperty("file_file_name")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("file_file_name")
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
                + ", fileSigned=" + fileSigned + ", fileSignedDownload=" + fileSignedDownload 
				+ ", allow_business=" + allowBusiness + ", fileZipped=" + fileZipped 
				+ ", signatures=" + signatures + ", additionalProperties=" + additionalProperties + "]";
    }
	
	@JsonProperty("allow_business")
	public Boolean getAllowBusiness(){
		return allowBusiness;
	}
	@JsonProperty("allow_business")
	public void setAllowBusiness(Boolean allowBusiness){
		this.allowBusiness = allowBusiness;
	}
	@JsonProperty("sign_callback_url")
    public String getSignCallbackUrl() {
		return signCallbackUrl;
	}
	@JsonProperty("sign_callback_url")
	public void setSignCallbackUrl(String signCallbackUrl) {
		this.signCallbackUrl = signCallbackUrl;
	}
	
	public List<Signer> signers(){
		List<Signer> signers = null;
		if( additionalProperties!=null && additionalProperties.get("signers")!=null ){
			signers = new ArrayList<Signer>();
            List<Object> signersLst = (List<Object>) additionalProperties.get("signers");
            for(Object o : signersLst ){
				Signer s = new Signer();
				Map m = ( (Map) o );
				s.setId( m.get("id")!=null ? m.get("id").toString() : null );
				s.setName( m.get("name")!=null? m.get("name").toString() : null );
				s.setEmail( m.get("email")!=null ?  m.get("email").toString() : null  );
				s.setTaxId( m.get("tax_id")!=null ? m.get("tax_id").toString() : null );
				s.setField( m.get("field")!=null ?  m.get("field").toString() : null );
				s.setSigned( Boolean.parseBoolean( m.get("signed")!=null ?  m.get("signed").toString() : "false"  ) );
				s.setWidgetId( m.get("widget_id")!=null ? m.get("widget_id").toString() : null );
				signers.add( s );
			}
		}
		return signers;
	}

}