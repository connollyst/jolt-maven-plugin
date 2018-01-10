package com.github.connollyst.jolt;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Sean Connolly
 */
class JoltTransformer {

    private final Log log;
    private final ObjectMapper mapper;
    private final boolean minify;
    private final Chainr spec;
    private final Path outputFile;


    JoltTransformer(Log log, boolean minify, Path specFile, Path outputFile) {
        this.mapper = new ObjectMapper();
        this.minify = minify;
        this.log = log;
        this.spec = readSpec(specFile);
        this.outputFile = outputFile;
    }

    private Chainr readSpec(Path specFile) {
        try {
            return Chainr.fromSpec(readInput(specFile));
        } catch (Exception e) {
            throw new JoltMavenException("Failed to read " + specFile + ": " + e.getMessage(), e);
        }
    }

    Path execute(Path inputFile) throws JoltMavenException, IOException {
        log.info("Transforming " + inputFile + " & writing to " + outputFile);
        Object input = readInput(inputFile);
        Object output = spec.transform(input);
        return writeOutput(output);
    }

    private Object readInput(Path inputFile) throws IOException {
        log.info("Reading Jolt spec " + inputFile.toFile());
        try {
            return mapper.readValue(inputFile.toFile(), Object.class);
        } catch (IOException ioe) {
            throw new IOException("Failed to read " + inputFile + ": " + ioe.getMessage(), ioe);
        }
    }

    private Path writeOutput(Object output) throws IOException {
        log.debug("Writing transformed JSON: " + output);
        try {
            Files.createDirectories(outputFile.getParent());
        } catch (IOException ioe) {
            throw new IOException("Failed to create path to " + outputFile.getParent() + ":" + ioe.getMessage(), ioe);
        }
        try {
            ObjectWriter writer = minify ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
            writer.writeValue(outputFile.toFile(), output);
        } catch (IOException ioe) {
            throw new IOException("Failed to write " + outputFile + ":" + ioe.getMessage(), ioe);
        }
        return outputFile;
    }

}