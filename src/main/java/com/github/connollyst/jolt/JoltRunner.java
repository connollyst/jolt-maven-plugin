package com.github.connollyst.jolt;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sean Connolly
 */
public class JoltRunner {

    private final Log log;
    private final String specFile;
    private final String inputDirectory;
    private final String outputDirectory;
    private final boolean minify;

    JoltRunner(Log log, String specFile, String inputDirectory, String outputDirectory, boolean minify) {
        this.log = log;
        this.specFile = specFile;
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.minify = minify;
    }

    void verify() throws MojoFailureException {
        // TODO assert all paths look legit
    }

    void execute() throws MojoExecutionException {
        log.debug("Jolt transformation spec file: " + specFile);
        log.debug("Jolt input directory: " + inputDirectory);
        log.debug("Jolt output directory: " + outputDirectory);
        try {
            Path spec = asPath(specFile, false);
            Path input = asPath(inputDirectory, false);
            Path output = asPath(outputDirectory, true);
            JoltTransformer transformer = new JoltTransformer(log, minify, spec);
            JoltCrawler crawler = new JoltCrawler(log, input, output, transformer);
            Files.walkFileTree(input, crawler);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private Path asPath(String filePath, boolean createIfMissing) throws MojoExecutionException, IOException {
        try {
            URL fileUrl = getClass().getResource(filePath);
            if (fileUrl == null) {
                if (createIfMissing) {
                    Files.createDirectories(Paths.get(filePath));
                    return asPath(filePath, false);
                } else {
                    throw new IOException("File not found: " + filePath);
                }
            }
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new MojoExecutionException("???", e);
        }
    }

}
