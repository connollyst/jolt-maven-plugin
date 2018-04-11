package com.github.connollyst.jolt;

import static com.bazaarvoice.jolt.JsonUtils.jsonToObject;
import static com.bazaarvoice.jolt.JsonUtils.toJsonString;
import static com.bazaarvoice.jolt.JsonUtils.toPrettyJsonString;
import static java.nio.file.Files.newInputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.logging.Log;

import com.bazaarvoice.jolt.Chainr;

/**
 * @author Sean Connolly
 */
class JoltTransformer {

    private final Log log;
    private final boolean minify;
    private final Chainr spec;


    JoltTransformer(Log log, boolean minify, Path specFile) {
        this.minify = minify;
        this.log = log;
        this.spec = readSpec(specFile);
    }

    private Chainr readSpec(Path specFile) {
        try {
            return Chainr.fromSpec(readInput(specFile));
        } catch (Exception e) {
            throw new JoltMavenException("Failed to read " + specFile + ": " + e.getMessage(), e);
        }
    }

    void execute(Path inputFile, Path outputFile) throws JoltMavenException, IOException {
        log.info("Transforming " + inputFile + " & writing to " + outputFile);
        Object inputJson = readInput(inputFile);
        Object outputJson = spec.transform(inputJson);
        writeOutput(outputFile, outputJson);
    }

    private Object readInput(Path inputFile) throws IOException {
        log.info("Reading Jolt spec " + inputFile.toFile());
        try {
            return jsonToObject(newInputStream(inputFile));
        } catch (IOException ioe) {
            throw new IOException("Failed to read " + inputFile + ": " + ioe.getMessage(), ioe);
        }
    }

    private void writeOutput(Path outputFile, Object outputJson) throws IOException {
        log.debug("Writing transformed JSON: " + outputJson);
        try {
            Files.createDirectories(outputFile.getParent());
        } catch (IOException ioe) {
            throw new IOException("Failed to create path to " + outputFile.getParent() + ":" + ioe.getMessage(), ioe);
        }
        try {
            Files.write(outputFile, stringFromObject(outputJson).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioe) {
            throw new IOException("Failed to write " + outputFile + ":" + ioe.getMessage(), ioe);
        }
    }

    private String stringFromObject(Object outputJson) {
        return minify ? toJsonString(outputJson) : toPrettyJsonString(outputJson);
    }

}