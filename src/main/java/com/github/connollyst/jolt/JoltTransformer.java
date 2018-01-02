package com.github.connollyst.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Sean Connolly
 */
class JoltTransformer {

    private final Log log;
    private final ObjectMapper reader;
    private final ObjectWriter writer;
    private final Path specFile;
    private final Path inputFile;
    private final Path outputFile;

    JoltTransformer(Log log, ObjectMapper reader, ObjectWriter writer, Path specFile, Path inputFile, Path outputFile) {
        this.log = log;
        this.reader = reader;
        this.writer = writer;
        this.specFile = specFile;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    Path execute() throws MojoExecutionException {
        log.info("Transforming " + inputFile + " using " + specFile + ", writing to " + outputFile);
        Chainr spec = readSpec();
        Object input = readInput();
        Object output = spec.transform(input);
        return writeOutput(output);
    }

    private Chainr readSpec() throws MojoExecutionException {
        try {
            return Chainr.fromSpec(readFile(specFile));
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to read " + specFile + ": " + e.getMessage(), e);
        }
    }

    private Object readInput() throws MojoExecutionException {
        try {
            return reader.readValue(inputFile.toFile(), Object.class);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Failed to read " + inputFile + ": " + ioe.getMessage(), ioe);
        }
    }

    private Object readFile(Path file) throws MojoExecutionException {
        try {
            return reader.readValue(file.toFile(), Object.class);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Failed to read " + file + ": " + ioe.getMessage(), ioe);
        }
    }

    private Path writeOutput(Object output) throws MojoExecutionException {
        log.debug("Writing transformed JSON: " + output);
        try {
            Files.createDirectories(outputFile.getParent());
        } catch (IOException ioe) {
            throw new MojoExecutionException("Failed to create directories for " + outputFile.getParent() + ":"
                    + ioe.getMessage(), ioe);
        }
        try {
            writer.writeValue(outputFile.toFile(), output);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Failed to write " + outputFile + ":" + ioe.getMessage(), ioe);
        }
        return outputFile;
    }

}