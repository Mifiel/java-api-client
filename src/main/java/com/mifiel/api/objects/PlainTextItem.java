package com.mifiel.api.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlainTextItem {
	@JsonProperty("text")
	private String text;

	@JsonProperty("signature")
	private String signature;

	public String getText(){
		return this.text;
	}

	public String getSignature(){
		return this.signature;
	}

	public void setSignature(String signature){
		this.signature = signature;
	}
}
