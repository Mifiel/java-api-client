package com.mifiel.api.utils;

/**
 *
 * @author jazavala
 */
public enum DigestType {
    SHA1("HmacSHA1","HMAC-SHA1"),
    SHA256("HmacSHA256", "HMAC-SHA256");
    
    private final String algorithm;
    private final String hmac;

    DigestType(final String algorithm, final String hmac) {
        this.algorithm = algorithm;
        this.hmac = hmac;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getHmac() {
        return hmac;
    }
}
