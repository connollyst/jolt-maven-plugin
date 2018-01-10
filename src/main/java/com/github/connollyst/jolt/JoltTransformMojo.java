package com.github.connollyst.jolt;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sean Connolly
 */
@Mojo(name = "transform")
public class JoltTransformMojo extends AbstractMojo {

    @Parameter(property = "jolt.transform.spec", required = true)
    private String spec;
    @Parameter(property = "jolt.transform.inputDirectory", defaultValue = "${project.basedir}/src/main/resources/json/")
    private String inputDirectory;
    @Parameter(property = "jolt.transform.outputDirectory", defaultValue = "${project.build.directory}/json/")
    private String outputDirectory;
    @Parameter(property = "jolt.transform.minify", defaultValue = "false")
    private boolean minify;

    private final Log log = getLog();

    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info("Executing Jolt transform plugin.");
        log.info("spec: " + spec);
        log.info("inputDirectory: " + inputDirectory);
        log.info("outputDirectory: " + outputDirectory);
        JoltTransformer transformer = new JoltTransformer(log, minify, asPath(spec), asPath(outputDirectory));
        Crawler crawler = new Crawler(getLog(), transformer);
        try {
            Files.walkFileTree(asPath(inputDirectory), crawler);
        } catch (IOException e) {
            throw new MojoExecutionException("???", e);
        }
    }


    private Path asPath(String filePath) throws MojoExecutionException {
        try {
            return Paths.get(getClass().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            throw new MojoExecutionException("???", e);
        }
    }

}