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

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
}
