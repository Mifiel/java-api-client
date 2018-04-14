package com.mifiel.api.objects;
/**
 *
 * @author Francisco Daniel Perez Morales / fdanielpm@yandex.com
 */
public class Signer {
    
    private String id;
    private String name;
    private String email;
    private String taxId;
	private String field;
    private boolean signed;
    private String widgetId;
	
    public Signer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }
	
	public String toString(){
		return new StringBuilder()
						.append("id=").append(id).append(", ")
						.append("name=").append(name).append(", ")
						.append("email=").append(email).append(", ")
						.append("taxId=").append(taxId).append(", ")
						.append("field=").append(field).append(", ")
						.append("signed=").append(signed).append(", ")
						.append("widgetId=").append(widgetId)
						.toString();
	}
}
