package com.mifiel.api.objects;

import java.util.HashMap;

public class SignaturePackageSettings {

    private String widgetVersion;
    private String useSignaturePackage;
    private HashMap<String, String> onSingingPage = new HashMap<String, String>();

    public String getWidgetVersion() {
        return this.widgetVersion;
    }

    public String getUseSignaturePackage() {
        return this.useSignaturePackage;
    }

    public HashMap<String, String> getOnSingingPage() {
        return this.onSingingPage;
    }
}
