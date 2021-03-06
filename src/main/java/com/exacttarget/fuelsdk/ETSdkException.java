//
// ETSdkException.java -
//
//      x
//
// Copyright (C) 2013 ExactTarget
//
// @COPYRIGHT@
//

package com.exacttarget.fuelsdk;

@SuppressWarnings("serial")
public class ETSdkException extends Exception {
    public ETSdkException() {}

    public ETSdkException(Exception ex) {
        super(ex);
    }

    public ETSdkException(String message) {
        super(message);
    }

    public ETSdkException(String message, Throwable cause) {
        super(message, cause);
    }
}
