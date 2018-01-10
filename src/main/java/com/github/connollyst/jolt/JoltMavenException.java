package com.github.connollyst.jolt;

/**
 * @author Sean Connolly
 */
public class JoltMavenException extends RuntimeException {

    public JoltMavenException(String message) {
        super(message);
    }

    public JoltMavenException(String message, Throwable cause) {
        super(message, cause);
    }

}