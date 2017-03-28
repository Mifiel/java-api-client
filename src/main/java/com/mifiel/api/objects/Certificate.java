package com.mifiel.api.objects;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "file", "type_of", "cer_hex", "owner", "tax_id", "expires_at", "expired" })
public class Certificate {

	@JsonProperty("id")
	private String id;
	@JsonProperty("file")
	private String file;
	@JsonProperty("type_of")
	private String typeOf;
	@JsonProperty("cer_hex")
	private String cerHex;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("tax_id")
	private String taxId;
	@JsonProperty("expires_at")
	private String expiresAt;
	@JsonProperty("expired")
	private Boolean expired;
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
	
	@JsonProperty("file")
	public String getFile() {
		return file;
	}

	@JsonProperty("file")
	public void setFile(String file) {
		this.file = file;
	}

	@JsonProperty("type_of")
	public String getTypeOf() {
		return typeOf;
	}

	@JsonProperty("type_of")
	public void setTypeOf(String typeOf) {
		this.typeOf = typeOf;
	}

	@JsonProperty("cer_hex")
	public String getCerHex() {
		return cerHex;
	}

	@JsonProperty("cer_hex")
	public void setCerHex(String cerHex) {
		this.cerHex = cerHex;
	}

	@JsonProperty("owner")
	public String getOwner() {
		return owner;
	}

	@JsonProperty("owner")
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@JsonProperty("tax_id")
	public String getTaxId() {
		return taxId;
	}

	@JsonProperty("tax_id")
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	@JsonProperty("expires_at")
	public String getExpiresAt() {
		return expiresAt;
	}

	@JsonProperty("expires_at")
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	@JsonProperty("expired")
	public Boolean getExpired() {
		return expired;
	}

	@JsonProperty("expired")
	public void setExpired(Boolean expired) {
		this.expired = expired;
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
		return "Certificate [id=" + id + ", typeOf=" + typeOf + ", cerHex=" + cerHex + ", owner=" + owner + ", taxId="
				+ taxId + ", expiresAt=" + expiresAt + ", expired=" + expired + ", additionalProperties="
				+ additionalProperties + "]";
	}

}