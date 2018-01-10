package com.github.connollyst.jolt;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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

    @Parameter(property = "transform.inputDirectory", defaultValue = "${project.basedir}/src/main/resources/json/")
    private String inputDirectory;
    @Parameter(property = "transform.outputDirectory", defaultValue = "${project.build.directory}/json/")
    private String outputDirectory;
    @Parameter(property = "spec")
    private String spec;
    @Parameter(property = "minify", defaultValue = "false")
    private boolean minify;



    public void execute() throws MojoExecutionException, MojoFailureException {
        JoltTransformer transformer = new JoltTransformer(getLog(), minify, asPath(spec), asPath(outputDirectory));
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