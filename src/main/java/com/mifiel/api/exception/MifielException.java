package com.mifiel.api.exception;

import com.mifiel.api.utils.MifielUtils;
import com.mifiel.api.objects.Error;

public class MifielException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String ERROR_CANONICAL_NAME = Error.class.getCanonicalName();
    private Error mifielError;

    public MifielException() {
        super();
    }

    public MifielException(final String message) {
        super(message);
    }

    public MifielException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MifielException(final Throwable cause) {
        super(cause);
    }

    public MifielException(final String message, final String httpResponse) {
        super(message);

        try {
            mifielError = (Error) MifielUtils.convertJsonToObject(httpResponse, ERROR_CANONICAL_NAME);
        } catch (final Exception e) {
        }
    }

    public Error getMifielError() {
        return mifielError;
    }
}
