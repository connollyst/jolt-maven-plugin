package com.github.connollyst.jolt;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Maven plugin style logger that delegates to SLF4J.
 *
 * @author Sean Connolly
 */
class SLF4JMavenLogger implements Log {

    static SLF4JMavenLogger get(Class<?> of) {
        return new SLF4JMavenLogger(LoggerFactory.getLogger(of));
    }

    private final Logger slf4j;

    private SLF4JMavenLogger(Logger slf4j) {
        this.slf4j = slf4j;
    }

    @Override
    public boolean isDebugEnabled() {
        return slf4j.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence charSequence) {
        slf4j.debug(charSequence.toString());
    }

    @Override
    public void debug(CharSequence charSequence, Throwable throwable) {
        slf4j.debug(charSequence.toString(), throwable);
    }

    @Override
    public void debug(Throwable throwable) {
        slf4j.debug("Debug:", throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return slf4j.isInfoEnabled();
    }

    @Override
    public void info(CharSequence charSequence) {
        slf4j.info(charSequence.toString());
    }

    @Override
    public void info(CharSequence charSequence, Throwable throwable) {
        slf4j.info(charSequence.toString(), throwable);
    }

    @Override
    public void info(Throwable throwable) {
        slf4j.info("Info:", throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return slf4j.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence charSequence) {
        slf4j.warn(charSequence.toString());
    }

    @Override
    public void warn(CharSequence charSequence, Throwable throwable) {
        slf4j.warn(charSequence.toString(), throwable);
    }

    @Override
    public void warn(Throwable throwable) {
        slf4j.warn("Warn:", throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return slf4j.isErrorEnabled();
    }

    @Override
    public void error(CharSequence charSequence) {
        slf4j.error(charSequence.toString());
    }

    @Override
    public void error(CharSequence charSequence, Throwable throwable) {
        slf4j.error(charSequence.toString(), throwable);
    }

    @Override
    public void error(Throwable throwable) {
        slf4j.error("Error:", throwable);
    }

}